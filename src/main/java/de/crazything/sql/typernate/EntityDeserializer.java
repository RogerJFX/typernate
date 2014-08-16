package de.crazything.sql.typernate;

import java.lang.reflect.Field;
import java.util.List;

import de.crazything.sql.typernate.EntityAnalyzer.AnalyzedField;
import de.crazything.sql.typernate.EntityAnalyzer.AnalyzerResult;

/**
 * A user defined databse type is received as Java-Object. This class takes
 * these Objects and maps data to the targeted members.
 * 
 * @See de.crazything.sql.typernate.annotation.DbTypeObject.target()
 * @author roger
 * 
 */
public class EntityDeserializer {
    /**
     * Modifies a List of Entities.
     * 
     * @param clazz
     *            The type of List entries.
     * @param list
     *            The list.
     * @param obj2null
     *            If true, the Objects obtained from database will be set to
     *            null after modification. Normally one may pass true, since the
     *            object should not be used a second time.
     * @return A modified List of Entities. All type based objects will be
     *         mapped for all entries.
     */
    public static <T> List<T> deserializeEntities(final Class<T> clazz, final List<T> list, final boolean obj2null) {
	for (T t : list) {
	    t = deserializeEntity(clazz, t, obj2null);
	}
	return list;
    }

    /**
     * Modifies an Entity.
     * 
     * @param clazz
     *            Type of class.
     * @param obj
     *            The object itself.
     * @param obj2null
     *            If true, the object field (gathered from database) will set to
     *            null. Normally one may pass true, since the object should not
     *            be used a second time.
     * @return A valid entity for further use.
     */
    public static <T> T deserializeEntity(final Class<T> clazz, final T obj, final boolean obj2null) {
	final AnalyzerResult<T> analyzed = EntityAnalyzer.getResult(clazz);
	try {
	    for (final AnalyzedField anaField : analyzed.getFields()) {
		final Field targetField = anaField.getTypeField();
		if (anaField.isCollection()) {
		    targetField.set(
			    obj,
			    TypeDeserializer.deserializeTypeArray(anaField.getCollType(), anaField.getObjectField()
				    .get(obj).toString()));
		} else {
		    targetField.set(
			    obj,
			    TypeDeserializer.deserializeType(anaField.getTypeType(), anaField.getObjectField().get(obj)
				    .toString()));
		}
		if (obj2null) {
		    anaField.getObjectField().set(obj, null);
		}
	    }
	} catch (final IllegalAccessException | SecurityException e) {
	    throw new RuntimeException("Could not deserialize Entity.", e);
	}
	return obj;
    }
}
