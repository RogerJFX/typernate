package de.crazything.sql.typernate;

import java.util.ArrayList;
import java.util.List;

import de.crazything.sql.typernate.TypeAnalyzer.AnalyzedField;
import de.crazything.sql.typernate.TypeAnalyzer.AnalyzerResult;
import de.crazything.sql.typernate.parse.ParseResult;
import de.crazything.sql.typernate.parse.ParserHelper;
import de.crazything.sql.typernate.parse.PgParserHelper;
import de.crazything.sql.typernate.parse.PrimitiveAssignerUtil;

/**
 * Core class. Here the objects obtained from database are translated/mapped to
 * our POJOs.
 * 
 * @author roger
 * 
 */
public class TypeDeserializer {
    /**
     * Temp.. We should parameterize this, if we once want to use e.g. ORACLE as
     * well. But then again this class itself may need some modifications.
     * However probably not as many as in TypeSerializer.
     */
    private static ParserHelper parserHelper = new PgParserHelper();

    public static void setParserHelper(final ParserHelper ph) {
	parserHelper = ph;
    }

    /**
     * If it's an array, we normally have to do some work in a loop.
     * 
     * @param clazz
     *            Type of array.
     * @param rawData
     *            Data to parse.
     * @return List of Object of Type clazz.
     */
    static <T> List<T> deserializeTypeArray(final Class<T> clazz, final String rawData) {
	if (rawData.trim().length() == 0) {
	    return null;
	}
	final List<T> result = new ArrayList<T>();
	final String workData = parserHelper.stripArray(rawData);
	final char[] wData = workData.toCharArray();
	ParseResult next;
	int cursor = 0;
	while ((next = parserHelper.nextObject(wData, cursor)) != null) {
	    cursor = next.cursor;
	    result.add(deserializeType(clazz, next.result));
	}
	return result;
    }

    /**
     * Workhorse method.
     * 
     * @param clazz
     *            Type of class we want to gain.
     * @param rawData
     *            String data.
     * @return A valid object.
     */
    public static <T> T deserializeType(final Class<T> clazz, final String rawData) {
	// System.out.println(rawData.toString());
	final AnalyzerResult analyzed = TypeAnalyzer.getResult(clazz);
	final String objectData = parserHelper.stripQuotes(parserHelper.nextObject(rawData.toCharArray(), 0).result);
	final char[] cData = objectData.toCharArray();
	int cursor = parserHelper.findObjectDataStart(cData);
	T result = null;
	try {
	    result = clazz.newInstance();
	    for (final AnalyzedField anaField : analyzed.getFields()) {
		final ParseResult pr = parserHelper.nextField(cData, cursor);
		final String data = pr.result;
		cursor = pr.cursor;
		if (anaField.isPrimitive()) {
		    PrimitiveAssignerUtil.assignPrimitive(result, anaField.getObjType(), anaField.getField(), data);
		} else if (anaField.isCollection()) {
		    anaField.getField().set(result, deserializeTypeArray(anaField.getCollectionType(), data));
		} else if (anaField.isTypeObject()) {
		    anaField.getField().set(result, deserializeType(anaField.getObjType(), data));
		} else {
		    anaField.getField().set(result, data.length() > 0 ? data : null);
		}
	    }
	} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
	    throw new RuntimeException("Could not deserialize Type.", e);
	}
	return result;
    }

}
