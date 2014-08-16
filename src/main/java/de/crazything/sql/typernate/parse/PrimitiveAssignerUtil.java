package de.crazything.sql.typernate.parse;

import java.lang.reflect.Field;

/**
 * Primitives are a bit hard to handle within the reflection api. Thus we need
 * some particular methods.
 * 
 * @author roger
 * 
 */
public class PrimitiveAssignerUtil {
    /**
     * Primitive fields a set here. It's a pure helper method and should not be
     * used from outside.
     * 
     * @param obj
     *            The instance, the field is member of.
     * @param type
     *            Given type of value.
     * @param field
     *            The field itself.
     * @param value
     *            The value to set.
     * @throws NumberFormatException
     *             If a field is a numeric type, but a given String cannot be
     *             parsed.
     * @throws IllegalArgumentException
     *             Should not happen.
     * @throws IllegalAccessException
     *             If a field isn't accessible.
     */
    public static void assignPrimitive(final Object obj, final Class<?> type, final Field field, final String value)
	    throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
	if (value.length() == 0) {
	    return;
	}
	if (Long.TYPE.equals(type)) {
	    field.setLong(obj, Long.parseLong(value));
	} else if (Integer.TYPE.equals(type)) {
	    field.setInt(obj, Integer.parseInt(value));
	} else if (Byte.TYPE.equals(type)) {
	    field.setByte(obj, Byte.parseByte(value));
	} else if (Double.TYPE.equals(type)) {
	    field.setDouble(obj, Double.parseDouble(value));
	} else if (Float.TYPE.equals(type)) {
	    field.setFloat(obj, Float.parseFloat(value));
	} else if (Boolean.TYPE.equals(type)) {
	    field.setBoolean(obj, Boolean.parseBoolean(value));
	}
    }

}
