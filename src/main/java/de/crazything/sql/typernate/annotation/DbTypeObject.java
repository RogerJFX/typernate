/**
 * 
 */
package de.crazything.sql.typernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates within an entity (e.g. JPA), that it shall be
 * serialized/deserialized by typernate.
 * 
 * Important: we need a target here. It must point to the targeted field in the
 * entity. So normally a Field of type object should be annotated, which targets
 * to a Type or Collection of types, that is modified by typernate.
 * 
 * @author roger
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbTypeObject {
    /**
     * We need two members in our entity:
     * <ol>
     * <li>An object for communication with database</li>
     * <li>An entity, the object data is mapped to.</li>
     * </ol>
     * 
     * @return Name of Entity field.
     */
    String target();

    /**
     * Only for ORACLE. And only if there is an Array of types as column in
     * table. Postgres will ignore it.
     * 
     * @return Name of ORACLE specific varray type.
     */
    String varrayType() default "";
}
