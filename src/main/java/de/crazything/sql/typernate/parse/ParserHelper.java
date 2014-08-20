package de.crazything.sql.typernate.parse;

/**
 * Some Parser utils.
 * 
 * @author roger
 * 
 */
public abstract class ParserHelper {
    /**
     * Null?
     */
    protected final char NULL = '\u0000';

    /**
     * Not used.
     * 
     * @param line
     *            Set of characters to look at.
     * @param offset
     *            Offset
     * @param numChars
     *            numChars to examine.
     * @return Another character array.
     */
    protected char[] lookBehind(final char[] line, final int offset, final int numChars) {
	final char[] result = new char[numChars];
	int c = 0;
	for (int i = offset; i < line.length && i < offset + numChars; i++) {
	    result[c++] = line[i];
	}
	return result;
    }

    /**
     * Deletes a char before an behind the given String, if found.
     * 
     * @param in
     *            String to be stripped.
     * @param before
     *            char before to strip.
     * @param behind
     *            char behind to strip.
     * @return Stripped.
     */
    protected String strip(final String in, final char before, final char behind) {
	final String workString = in.trim();
	final int accFirst = workString.charAt(0) == before ? 1 : 0;
	final int accLast = workString.charAt(workString.length() - 1) == behind ? 2 : 0;
	switch (accFirst | accLast) {
	case 1:
	    return workString.substring(1);
	case 2:
	    return workString.substring(0, in.length() - 1);
	case 3:
	    return workString.substring(1, in.length() - 1);
	default:
	    return workString;
	}
    }

    public abstract int findObjectDataStart(char[] in);

    /**
     * Get rid of an array indicator.
     * 
     * @param in
     *            String to be stripped.
     * @return Stripped.
     */
    public abstract String stripArray(String in);

    /**
     * Get rid of quotes.
     * 
     * @param in
     *            String to be stripped.
     * @return Stripped.
     */
    public abstract String stripQuotes(String in);

    /**
     * Is it an Array (or List)? We should then do something else :D
     * 
     * @param in
     *            String to examine
     * @return true, if an Array is found, otherwise false.
     */

    public abstract boolean isArray(String in);

    /**
     * Finds the next Object (this is Db-Type).
     * 
     * @param line
     *            char array
     * @param from
     *            offset
     * @return Data packed in ParseResult.
     */
    public abstract ParseResult nextObject(final char[] line, final int from);

    /**
     * Finds the next field within the given object.
     * 
     * @param line
     *            char array
     * @param from
     *            offset
     * @return Data packed in ParseResult.
     */

    public abstract ParseResult nextField(final char[] line, final int from);

    /**
     * Wraps some Data in a array. Important for serialization.
     * 
     * @param in
     *            String to wrap.
     * @param type
     *            db type for letting the database know where to cast, if in
     *            doubt.
     * @param oraVarray
     *            Special value for old databases like Oracle.
     * @return wrapped array, so the database might get along with it.
     */

    public abstract String wrapArray(final StringBuilder in, final String type, final String oraVarray);

    /**
     * Wraps an Object (db type).
     * 
     * @param in
     *            String to wrap.
     * @return Wrapped object. Database should know.
     */
    public abstract StringBuilder wrapObject(final StringBuilder in);

    /**
     * Appends a type to an object or array, in case, the database needs to
     * know, where to cast.
     * 
     * @param in
     *            String to append the cast information to.
     * @param type
     *            Db type to cast.
     * @return Database may be satisfied.
     */
    public abstract StringBuilder appendType(final StringBuilder in, final String type);

    /**
     * Something nasty in the String? A single quote? Ok, lets escape it.
     * 
     * @param in
     *            String to modify.
     * @return Database may be satisfied.
     */
    public abstract String escapeString(final String in);

    public abstract String doSerialize(final String[] storage, final String dbType, final boolean fromArray);

    public abstract boolean isSqlStructSupported();
}
