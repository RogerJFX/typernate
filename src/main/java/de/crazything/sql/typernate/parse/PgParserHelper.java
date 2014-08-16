package de.crazything.sql.typernate.parse;

public class PgParserHelper extends ParserHelper {

    @Override
    public boolean isArray(final String in) {
	final String workString = in.trim();
	return workString.charAt(0) == '{' && workString.charAt(workString.length() - 1) == '}';
    }

    @Override
    public String stripQuotes(final String in) {
	return this.strip(in, '"', '"');
    }

    @Override
    public String stripArray(final String in) {
	return this.strip(in, '{', '}');
    }

    @Override
    public String escapeString(final String in) {
	return in.replaceAll("'", "''");
    }

    @Override
    public ParseResult nextObject(final char[] line, final int from) {
	int cursor = 0;
	int bracketCounter = 0;
	boolean escaped = false;
	int firstBracket = from;
	for (int i = from; i < line.length; i++) {
	    if (line[i] == '(') {
		firstBracket = i;
		break;
	    }
	}
	final char[] result = new char[line.length - firstBracket];
	for (int i = firstBracket; i < line.length; i++) {
	    switch (line[i]) {
	    case '"':
		result[cursor++] = line[i];
		if (i + 1 < line.length && line[i + 1] == '"') {
		    result[cursor++] = line[++i];
		    break;
		} else if (i != from) {
		    escaped = !escaped;
		}
		break;
	    case '(':
		if (!escaped) {
		    ++bracketCounter;
		}
		result[cursor++] = line[i];
		break;
	    case ')':
		if (!escaped) {
		    --bracketCounter;
		}
		result[cursor++] = line[i];
		if (!escaped && bracketCounter == 0) {
		    return new ParseResult(new String(result, 0, cursor).trim(), i + 1);
		}
		// final char[] lookBehind = lookBehind(line, i, 2);
		// if (escaped && lookBehind[1] == '"') {
		// return new ParseResult(new String(result, 0, cursor).trim(),
		// i);
		// }
		break;
	    default:
		result[cursor++] = line[i];
	    }
	}
	return null;
    }

    @Override
    public ParseResult nextField(final char[] line, final int from) {
	final char[] result = new char[line.length - from];
	int cursor = 0;
	boolean escaped = false;
	for (int i = from; i < line.length; i++) {
	    switch (line[i]) {
	    case '"':
		if (i + 1 < line.length && line[i + 1] == '"') {
		    result[cursor++] = line[i++];
		} else {
		    escaped = !escaped;
		}
		break;
	    case '\\':
		break;
	    case ',':
	    case ')':
		if (!escaped) {
		    return new ParseResult(new String(result, 0, cursor).trim(), i + 1);
		}
	    default:
		result[cursor++] = line[i];
	    }
	}
	return null;
    }

    @Override
    public String wrapArray(final StringBuilder in, final String type) {
	final StringBuilder builder = new StringBuilder();
	builder.append("array[").append(in).append("]::").append(type).append("[]");
	return builder.toString();
    }

    @Override
    public StringBuilder wrapObject(final StringBuilder in) {
	final StringBuilder builder = new StringBuilder();
	builder.append('(').append(in).append(')');
	return builder;
    }

    @Override
    public StringBuilder appendType(final StringBuilder in, final String type) {
	final StringBuilder builder = new StringBuilder();
	builder.append(in).append("::").append(type);
	return builder;
    }
}
