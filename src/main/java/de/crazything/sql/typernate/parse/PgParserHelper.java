package de.crazything.sql.typernate.parse;

public class PgParserHelper extends ParserHelper {
    /**
     * Postgres char for start of object.
     */
    private static final char OBJECT_OPEN = '(';
    /**
     * Postgres char for end of object.
     */
    private static final char OBJECT_CLOSE = ')';
    /**
     * Postgres char for start of array.
     */
    private static final char ARRAY_OPEN = '{';
    /**
     * Postgres char for end of array.
     */
    private static final char ARRAY_CLOSE = '}';
    /**
     * Wrapper like CSV.
     */
    private static final char WRAPPER = '"';
    /**
     * Additional escape.
     */
    private static final char ESCAPE = '\\';
    /**
     * The CSV like comma in PostgreSQL.
     */
    private static final char COMMA = ',';

    @Override
    public int findObjectDataStart(final char[] in) {
	for (int i = 0; i < in.length; i++) {
	    if (in[i] == OBJECT_OPEN) {
		return i + 1;
	    }
	}
	return -1;
    }

    @Override
    public boolean isArray(final String in) {
	final String workString = in.trim();
	return workString.charAt(0) == ARRAY_OPEN && workString.charAt(workString.length() - 1) == ARRAY_CLOSE;
    }

    @Override
    public String stripQuotes(final String in) {
	return this.strip(in, WRAPPER, WRAPPER);
    }

    @Override
    public String stripArray(final String in) {
	return this.strip(in, ARRAY_OPEN, ARRAY_CLOSE);
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
	    if (line[i] == OBJECT_OPEN) {
		firstBracket = i;
		break;
	    }
	}
	final char[] result = new char[line.length - firstBracket];
	for (int i = firstBracket; i < line.length; i++) {
	    switch (line[i]) {
	    case WRAPPER:
		result[cursor++] = line[i];
		if (i + 1 < line.length && line[i + 1] == '"') {
		    result[cursor++] = line[++i];
		    break;
		} else if (i != from) {
		    escaped = !escaped;
		}
		break;
	    case OBJECT_OPEN:
		if (!escaped) {
		    ++bracketCounter;
		}
		result[cursor++] = line[i];
		break;
	    case OBJECT_CLOSE:
		if (!escaped) {
		    --bracketCounter;
		}
		result[cursor++] = line[i];
		if (!escaped && bracketCounter == 0) {
		    return new ParseResult(new String(result, 0, cursor).trim(), i + 1);
		}
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
	    case WRAPPER:
		if (i + 1 < line.length && line[i + 1] == '"') {
		    result[cursor++] = line[i++];
		} else {
		    escaped = !escaped;
		}
		break;
	    case ESCAPE:
		final char[] behind = this.lookBehind(line, i, 3);
		if (behind[1] == ESCAPE) {
		    if (behind[2] == WRAPPER) {
			i++;
		    } else {
			result[cursor++] = line[i++];
		    }
		}
		break;
	    case COMMA:
	    case OBJECT_CLOSE:
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
    public String wrapArray(final StringBuilder in, final String type, final String oraVarray) {
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

    @Override
    public String doSerialize(final String[] values, final String dbType, final boolean fromArray) {
	StringBuilder builder = new StringBuilder();
	builder.append(values[0]);
	for (int i = 1; i < values.length; i++) {
	    builder.append("," + values[i]);
	}
	builder = this.wrapObject(builder);
	if (!fromArray) {
	    builder = this.appendType(builder, dbType);
	}
	return builder.toString();
    }

    @Override
    public boolean isSqlStructSupported() {
	return false;
    }
}
