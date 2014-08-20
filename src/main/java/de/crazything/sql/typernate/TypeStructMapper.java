package de.crazything.sql.typernate;

import java.sql.Array;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import de.crazything.sql.typernate.TypeAnalyzer.AnalyzedField;
import de.crazything.sql.typernate.TypeAnalyzer.AnalyzerResult;
import de.crazything.sql.typernate.parse.PrimitiveAssignerUtil;

/**
 * Core class. Here the objects obtained from database are translated/mapped to
 * our POJOs.
 * 
 * Currently under development. No clue, why all this is already working. :D
 * 
 * But really nice (or crazy): we can cast back and forth as we want to do.
 * Seems the api was not developed for years. Reminds me of the good old times,
 * or did I miss something? :D:D:D
 * 
 * @author roger
 * 
 */
public class TypeStructMapper {

    /**
     * If it's an array, we normally have to do some work in a loop.
     * 
     * @param clazz
     *            Type of array.
     * @param array
     *            Array to be translated.
     * @return List of Object of Type clazz.
     */
    static <T> List<T> deserializeTypeArray(final Class<T> clazz, final Array array) {
	final List<T> result = new ArrayList<T>();
	try {
	    final Object[] entries = (Object[]) array.getArray();
	    for (final Object e : entries) {
		result.add(deserializeType(clazz, (Struct) e));
	    }
	} catch (final SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return result;
    }

    /**
     * Workhorse method.
     * 
     * @param clazz
     *            Type of class we want to gain.
     * @param struct
     *            Representation of the db type as java.sql.struct.
     * @return A valid object.
     */
    public static <T> T deserializeType(final Class<T> clazz, final Struct struct) {
	final AnalyzerResult analyzed = TypeAnalyzer.getResult(clazz);
	T result = null;
	Object[] atts = null;
	try {
	    atts = struct.getAttributes();
	} catch (final SQLException e1) {
	    throw new RuntimeException("What's up? F... crap!", e1);
	}
	int c = 0;
	try {
	    result = clazz.newInstance();
	    for (final AnalyzedField anaField : analyzed.getFields()) {
		if (anaField.isPrimitive()) {
		    PrimitiveAssignerUtil.assignPrimitive(result, anaField.getObjType(), anaField.getField(),
			    atts[c++].toString());
		} else if (anaField.isCollection()) {
		    anaField.getField().set(result,
			    deserializeTypeArray(anaField.getCollectionType(), (Array) atts[c++]));
		} else if (anaField.isTypeObject()) {
		    anaField.getField().set(result, deserializeType(anaField.getObjType(), (Struct) atts[c++]));
		} else {
		    anaField.getField().set(result, atts[c++]);
		}
	    }
	} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
	    throw new RuntimeException("Could not deserialize Type.", e);
	}
	return result;
    }

}
