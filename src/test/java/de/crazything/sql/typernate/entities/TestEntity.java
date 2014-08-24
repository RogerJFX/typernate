package de.crazything.sql.typernate.entities;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import de.crazything.sql.annotation.Column;
import de.crazything.sql.annotation.NotPersistent;
import de.crazything.sql.annotation.Serial;
import de.crazything.sql.annotation.Table;
import de.crazything.sql.typernate.annotation.DbTypeObject;

/**
 * Important: we need an Object with the name of the DB column on one hand. This
 * Object must be annotated with @DbTypeObject. The target parameter must point
 * to the targeted field. So EntitySerializer/EntityDeserializer knows, where to
 * translate to or from.
 * 
 * @author roger
 * 
 */
@Table("tbl_typetest")
@Entity
@Access(AccessType.FIELD)
@javax.persistence.Table(name = "tbl_typetest")
public class TestEntity {
    /**
     * This field is generated by a database sequence. We can't set it thus here
     * in our java layer due to a pk in db table.
     */
    @Serial
    @Id
    @GeneratedValue
    int id;
    /**
     * Will be saved/fetched to/from database. Note: using Postgres, we could
     * even have PGobject instead of Object.
     */
    // Column is named itype in db.
    @Column(name = "itype")
    // It represents a db type and points to field testType of Type TestType.
    // Class TestType already knows the db type it is representing.
    @DbTypeObject(target = "testType")
    @javax.persistence.Column(name = "itype", columnDefinition = "type_test")
    // This will fail with ORACLE, since we get a struct here.
    // @Type(type = "text")
    // @ElementCollection
    // @Transient
    public Object typeTest;
    /**
     * Our nested entity. EntitySerializer will translate this to Object
     * typeTest before persisting the entity. EntityDeserializer will fill it
     * (mapping) after fetching the Entity. The source then is Object typeTest
     * of course. This may be a bit weird. At the moment I don't see another way
     * to make it compatible to JPA.
     */
    @NotPersistent
    @Transient
    TestType testType;

    public int getId() {
	return this.id;
    }

    public void setId(final int id) {
	this.id = id;
    }

    public TestType getTestType() {
	return this.testType;
    }

    public void setTestType(final TestType testType) {
	this.testType = testType;
    }

    public void setTypeTest(final Object typeTest) {
	this.typeTest = typeTest;
    }

    public Object getTypeTest() {
	return this.typeTest;
    }

}
