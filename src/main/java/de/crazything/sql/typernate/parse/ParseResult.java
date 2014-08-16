package de.crazything.sql.typernate.parse;

/**
 * Just to bundle Parser results.
 * 
 * @author roger
 * 
 */
public class ParseResult {
    /**
     * Ctor.
     * 
     * @param result
     *            Parse result to be set.
     * @param cursor
     *            Recent cursor position.
     */
    ParseResult(final String result, final int cursor) {
	this.result = result;
	this.cursor = cursor;
    }

    /**
     * Like a getter.
     */
    public String result;
    /**
     * Like a getter.
     */
    public int cursor;
}
