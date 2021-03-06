package de.crazything.sql.typernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides three informations: first, that it is a field in the database type,
 * second the position there (index), and if it should be quoted when
 * serializing.
 * 
 * @author roger
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbTypeField {
    /**
     * Very important!!! Indicates the position of field in db type.
     * 
     * @return Postion.
     */
    int index();

    boolean quote() default false;

    /**
     * Only for ORACLE.
     * 
     * @return Name of ORACLE specific varray type.
     */
    String varrayType() default "";
}
