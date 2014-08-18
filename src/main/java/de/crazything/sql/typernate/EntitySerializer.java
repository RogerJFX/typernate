package de.crazything.sql.typernate;

import java.lang.reflect.Field;
import java.util.List;

import de.crazything.sql.typernate.EntityAnalyzer.AnalyzedField;
import de.crazything.sql.typernate.EntityAnalyzer.AnalyzerResult;

/**
 * Serializer for complete Entities as gained from e.g. Hibernate or other JPA
 * implementations. EntitySerializer should modify an Entity before persisting
 * or merging it. <source>
 * 
 * The mechanism is as follows:
 * 
 * <ol>
 * <li>We have an entity with another nested entity representing a database
 * type.</li>
 * <li>EntitySerializer translates the nested entity to an object.</li>
 * <li>The translated object is now ready for persisting.</li>
 * </ol>
 * </source> Note: we have to ensure, that only the translated object is
 * relevant for database. Our mapped entity must never reach the database.
 * 
 * @See de.crazything.sql.typernate.annotation.DbTypeObject.target()
 * @author roger
 * 
 */
public class EntitySerializer {
    /**
     * If it's a List of Entities, use this method.
     * 
     * @param clazz
     *            Type tag.
     * @param list
     *            The list itself.
     * @return Modified Collection. Any db type should be mapped in any entry of
     *         the list.
     */
    public static <T> List<T> serializeEntities(final Class<T> clazz, final List<T> list) {
	for (T t : list) {
	    t = serializeEntity(clazz, t);
	}
	return list;
    }

    /**
     * Serializes any db type within an entity object.
     * 
     * @param clazz
     *            Type of entity object.
     * @param obj
     *            The entity.
     * @return Ready entity for persist or merge.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T serializeEntity(final Class<T> clazz, final T obj) {
	final AnalyzerResult analyzed = EntityAnalyzer.getResult(clazz);
	try {
	    for (final AnalyzedField anaField : analyzed.getFields()) {
		final Field targetField = anaField.getTypeField();
		if (anaField.isCollection()) {
		    anaField.getObjectField().set(obj,
			    TypeSerializer.serializeTypeArray(anaField.getCollType(), (List) targetField.get(obj)));
		} else {
		    anaField.getObjectField().set(obj,
			    TypeSerializer.serializeType((anaField.getTypeType()), targetField.get(obj)));
		}
	    }
	} catch (final IllegalAccessException | SecurityException e) {
	    throw new RuntimeException("Could not serialize Entity.", e);
	}
	return obj;
    }
}
