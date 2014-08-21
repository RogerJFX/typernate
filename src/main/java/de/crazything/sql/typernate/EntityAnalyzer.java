package de.crazything.sql.typernate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.crazything.sql.typernate.annotation.DbTypeObject;

/**
 * This class provides to things: <source>
 * <ul>
 * <li>It analyzes a passed Class (here representation of an entity) once</li>
 * <li>It caches the result of the analyzed Class</li>
 * </ul>
 * </source>
 * 
 * So it is a flyweight. It dramatically increases performance, especially when
 * dealing with huge resultsets. <br />
 * <br />
 * Note: this class is vary similar to TypeAnalyzer. We leave it this way for
 * the moment.
 * 
 * @author roger
 * 
 */
public class EntityAnalyzer {
    /**
     * The map to store AnalyzerResults.
     */
    private static final Map<Class<?>, AnalyzerResult> MAP = new HashMap<Class<?>, AnalyzerResult>();

    /**
     * Multiton method.
     * 
     * @param clazz
     *            Class to ask information for.
     * @return Instance of AnalyzerResult.
     */
    static <T> AnalyzerResult getResult(final Class<T> clazz) {
	AnalyzerResult result = MAP.get(clazz);
	if (result == null) {
	    result = analyze(clazz);
	    MAP.put(clazz, result);
	}
	return result;
    }

    /**
     * Helper method. It counts the interesting fields.
     * 
     * @param fields
     *            Fields in class.
     * @return Number of fields to handle later.
     */
    private static int fieldCount(final Field[] fields) {
	int count = 0;
	for (final Field field : fields) {
	    if (field.isAnnotationPresent(DbTypeObject.class)) {
		count++;
	    }
	}
	return count;
    }

    /**
     * Main method here.
     * 
     * @param clazz
     *            Class to examine.
     * @return Instance of AnalyzerResult. AnalyzerResult should provide every
     *         information we need.
     */
    private static <T> AnalyzerResult analyze(final Class<T> clazz) {
	final Field[] fields = clazz.getDeclaredFields();
	final int annoFieldCount = fieldCount(fields);
	final AnalyzedField[] annoFields = new AnalyzedField[annoFieldCount];
	int count = 0;
	for (final Field field : fields) {
	    if (field.isAnnotationPresent(DbTypeObject.class)) {
		try {
		    field.setAccessible(true);
		    final AnalyzedField af = new AnalyzedField();
		    af.objectField = field;

		    Field targetField;
		    final String oraArray = field.getAnnotation(DbTypeObject.class).varrayType();
		    af.oraVarray = oraArray;
		    targetField = clazz.getDeclaredField(field.getAnnotation(DbTypeObject.class).target());
		    targetField.setAccessible(true);
		    af.typeField = targetField;

		    if (List.class.isAssignableFrom(targetField.getType())) {
			af.collection = true;
			af.collType = (Class<?>) ((ParameterizedType) targetField.getGenericType())
				.getActualTypeArguments()[0];
		    } else {
			af.typeType = targetField.getType();
		    }

		    annoFields[count++] = af;
		} catch (NoSuchFieldException | SecurityException e) {
		    throw new RuntimeException("Could not analyze Entity.", e);
		}
	    }
	}
	final AnalyzerResult result = new AnalyzerResult();
	result.fields = annoFields;
	return result;
    }

    /**
     * An instance of this class should provide any further needed informations.
     * 
     * @author roger
     * 
     * @param <T>
     *            Type of class.
     */
    static class AnalyzerResult {
	private AnalyzedField[] fields;

	public AnalyzedField[] getFields() {
	    return this.fields;
	}
    }

    /**
     * Information for a Field.
     * 
     * @author roger
     * 
     */
    static class AnalyzedField {
	/**
	 * The field of the object for communication with database.
	 */
	private Field objectField;
	/**
	 * The field to serialize from or deserialize to.
	 */
	private Field typeField;
	/**
	 * Type if Object, if not a collection.
	 */
	private Class<?> typeType;
	/**
	 * Type of Collection entries, if it is a collection.
	 */
	private Class<?> collType;

	private String oraVarray;
	/**
	 * Is it a collection?
	 */
	boolean collection;

	public Field getObjectField() {
	    return this.objectField;
	}

	public Field getTypeField() {
	    return this.typeField;
	}

	public Class<?> getTypeType() {
	    return this.typeType;
	}

	public Class<?> getCollType() {
	    return this.collType;
	}

	public boolean isCollection() {
	    return this.collection;
	}

	public String getOraVarray() {
	    return this.oraVarray;
	}
    }

}
