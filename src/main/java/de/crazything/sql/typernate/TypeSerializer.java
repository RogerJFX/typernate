package de.crazything.sql.typernate;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import de.crazything.sql.typernate.TypeAnalyzer.AnalyzedField;
import de.crazything.sql.typernate.TypeAnalyzer.AnalyzerResult;
import de.crazything.sql.typernate.annotation.DbType;
import de.crazything.sql.typernate.parse.ParserHelper;
import de.crazything.sql.typernate.parse.PgParserHelper;

/**
 * Core class. If we want to store an Java object as user defined db type, we
 * have to translate it to the database specific semantic. This is done here,
 * both: for single types and arrays of types as well.
 * 
 * @author roger
 * 
 */
public class TypeSerializer {
    /**
     * Temp.. We should parameterize this, if we once want to use e.g. ORACLE as
     * well. But then again this class itself may need some modifications.
     */
    private static final ParserHelper parserHelper = new PgParserHelper();

    /**
     * Is there an array, maybe as a db type wrapped in another one?
     * 
     * @param clazz
     *            Type of List entries.
     * @param coll
     *            The List.
     * @return Serialized List as array.
     */
    static <T> String serializeTypeArray(final Class<T> clazz, final List<T> coll) {
	if (coll == null) {
	    return "";
	}
	final int size = coll.size();
	int counter = 0;
	final StringBuilder builder = new StringBuilder();
	for (final T obj : coll) {
	    builder.append(serializeType(clazz, obj, true));
	    if (++counter < size) {
		builder.append(',');
	    }
	}
	final DbType clazzAnno = clazz.getAnnotation(DbType.class);
	return parserHelper.wrapArray(builder, clazzAnno.value());
    }

    /**
     * Public method to serialize everything. The method detects first, whether
     * it's a collection or not an then calls serializeType or
     * serializeTypeArray.
     * 
     * @param clazz
     *            The Type of Object.
     * @param obj
     *            Object to be serialized.
     * @return Serialized object.
     */
    @SuppressWarnings("unchecked")
    public static <T> String serializeType(final Class<T> clazz, final Object obj) {
	if (Collection.class.isAssignableFrom(clazz)) {
	    final Class<T> g = (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	    return serializeTypeArray(g, (List<T>) obj);
	}
	return serializeType(clazz, obj, false);
    }

    /**
     * Workhorse method. Everything important is happening here.
     * 
     * @param clazz
     *            The Type of Object.
     * @param obj
     *            Object to be serialized.
     * @param fromArray
     *            Determines, if the method is called from serializeTypeArray
     *            (true) or not.
     * @return Serialized object.
     */
    @SuppressWarnings("unchecked")
    private static <T> String serializeType(final Class<T> clazz, final Object obj, final boolean fromArray) {
	final AnalyzerResult analyzed = TypeAnalyzer.getResult(clazz);
	final AnalyzedField[] fields = analyzed.getFields();
	final String[] storage = new String[fields.length];
	int c = 0;
	try {
	    for (final AnalyzedField anaField : fields) {
		final boolean quote = anaField.isQuote();
		if (anaField.isCollection()) {
		    final Object o = anaField.getField().get(obj);
		    if (o == null) {
			storage[c++] = null;
		    } else {
			storage[c++] = serializeTypeArray((Class<T>) anaField.getCollectionType(), (List<T>) o);
		    }
		} else if (anaField.isTypeObject()) {
		    final Object o = anaField.getField().get(obj);
		    if (o == null) {
			storage[c++] = null;
		    } else {
			storage[c++] = serializeType(anaField.getObjType(), o, false);
		    }
		} else {
		    if (quote && anaField.getField().get(obj) != null) {
			// anaField.getField().get(obj);
			if (anaField.isString()) {
			    storage[c++] = "'" + parserHelper.escapeString((String) (anaField.getField().get(obj)))
				    + "'";
			} else {
			    storage[c++] = "'" + anaField.getField().get(obj) + "'";
			}
		    } else {
			storage[c++] = anaField.getField().get(obj) + "";
		    }
		}
	    }
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException("Could not serialize Type.", e);
	}
	return toString(storage, analyzed.getType().value(), fromArray);
    }

    /**
     * All is put together to String here.
     * 
     * @param storage
     *            Lines of entries.
     * @param dbType
     *            Db type of surrounding object. May be important for e.g.
     *            Postgres casts (Use of "anyelement"???).
     * @param fromArray
     *            Determines, if the method is called from serializeTypeArray
     *            (true) or not.
     * @return String.
     */
    public static String toString(final String[] storage, final String dbType, final boolean fromArray) {
	StringBuilder builder = new StringBuilder();
	builder.append(storage[0]);
	for (int i = 1; i < storage.length; i++) {
	    builder.append("," + storage[i]);
	}
	builder = parserHelper.wrapObject(builder);
	if (!fromArray) {
	    builder = parserHelper.appendType(builder, dbType);
	}
	return builder.toString();
    }
}
