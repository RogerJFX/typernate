package de.crazything.sql.typernate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.crazything.sql.typernate.annotation.DbType;
import de.crazything.sql.typernate.annotation.DbTypeField;

/**
 * This class provides to things: <source>
 * <ul>
 * <li>It analyzes a passed Class (here representation of db type) once</li>
 * <li>It caches the result of the analyzed Class</li>
 * </ul>
 * </source>
 * 
 * So it is a flyweight. It dramatically increases performance, especially when
 * dealing with huge resultsets. <br />
 * <br />
 * Note: this class is vary similar to EntityAnalyzer. We leave it this way for
 * the moment.
 * 
 * @author roger
 * 
 */
public class TypeAnalyzer {
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
     * Helper method. It validates the given fields and returns the number of
     * interesting fields.
     * 
     * @param fields
     *            Fields in class.
     * @return Number of fields to handle later.
     */
    private static int fieldCount(final Field[] fields) {
	int count = 0;
	final boolean[] checkArr = new boolean[fields.length];
	for (final Field field : fields) {
	    if (field.isAnnotationPresent(DbTypeField.class)) {
		final int index = field.getAnnotation(DbTypeField.class).index();
		if (index >= fields.length) {
		    throw new IllegalArgumentException("Index of field " + field.getName()
			    + " seems to exceed the range.");
		} else if (checkArr[index]) {
		    throw new IllegalArgumentException("Duplicate index in class? Make up your mind!");
		} else {
		    checkArr[index] = true;
		}
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

	    if (field.isAnnotationPresent(DbTypeField.class)) {
		field.setAccessible(true);
		final Class<?> type = field.getType();
		final AnalyzedField af = new AnalyzedField();
		af.field = field;
		af.quote = field.getAnnotation(DbTypeField.class).quote();
		if (type.isPrimitive()) {
		    af.primitive = true;
		    af.objType = type;
		} else if (type.isAnnotationPresent(DbType.class)) {
		    af.typeObj = true;
		    af.objType = type;
		} else if (List.class.isAssignableFrom(type)) {
		    af.collection = true;
		    af.oraVarray = field.getAnnotation(DbTypeField.class).varrayType();
		    af.collType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		} else {
		    final boolean string = field.getType() == String.class;
		    af.string = string;
		    if (string) {
			af.quote = true;
		    }
		}
		annoFields[count++] = af;
	    }
	}
	Arrays.sort(annoFields, new FieldIndexComparator());
	final AnalyzerResult result = new AnalyzerResult();
	result.fields = annoFields;
	final DbType clazzAnno = clazz.getAnnotation(DbType.class);
	if (clazzAnno == null) {
	    throw new IllegalArgumentException("Class " + clazz.getName() + " isn't annotated.");
	}
	result.dbType = clazzAnno;
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
	/**
	 * Interesting fields.
	 */
	private AnalyzedField[] fields;
	/**
	 * Holds the db type, before all the name.
	 */
	private DbType dbType;

	public AnalyzedField[] getFields() {
	    return this.fields;
	}

	public DbType getType() {
	    return this.dbType;
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
	 * The field itself.
	 */
	private Field field;
	/**
	 * Is it a primitive like int?
	 */
	private boolean primitive;
	/**
	 * Is it another "nested" db type?
	 */
	private boolean typeObj;
	/**
	 * Or is it a collection?
	 */
	private boolean collection;
	/**
	 * If a collection: what Type is it? What's the T in List&lt;T&gt;?
	 */
	private Class<?> collType;

	/**
	 * Oracle does not know Array types.
	 */
	private String oraVarray;

	/**
	 * If nested type: what class is it?
	 */
	private Class<?> objType;
	/**
	 * Shall we quote it when serializing? Should soon be deprecated, since
	 * we already know, whether it a String or not.
	 */
	private boolean quote;
	/**
	 * Is it a String?
	 */
	private boolean string;

	public Field getField() {
	    return this.field;
	}

	public boolean isTypeObject() {
	    return this.typeObj;
	}

	public boolean isCollection() {
	    return this.collection;
	}

	public Class<?> getCollectionType() {
	    return this.collType;
	}

	public boolean isPrimitive() {
	    return this.primitive;
	}

	public Class<?> getObjType() {
	    return this.objType;
	}

	public boolean isQuote() {
	    return this.quote;
	}

	public boolean isString() {
	    return this.string;
	}

	public String getOraVarray() {
	    return this.oraVarray;
	}
    }

    /**
     * We need a Comparator, because Class.getFields does not guarantee the
     * proper order. But we need to know the order for serialization to give the
     * database proper information. That's what the index() in Annotation
     * DbTypeField is for.
     * 
     * @author roger
     * 
     */
    private static class FieldIndexComparator implements Comparator<AnalyzedField> {
	private static final Class<DbTypeField> CL = DbTypeField.class;

	@Override
	public int compare(final AnalyzedField af1, final AnalyzedField af2) {
	    final Field field1 = af1.getField();
	    final Field field2 = af2.getField();
	    if (!field1.isAnnotationPresent(CL) && field2.isAnnotationPresent(CL)) {
		return 1;
	    } else if (field1.isAnnotationPresent(CL) && !field2.isAnnotationPresent(CL)) {
		return -1;
	    }
	    return field1.getAnnotation(CL).index() > field2.getAnnotation(CL).index() ? 1 : -1;
	}

    }
}
