package de.crazything.sql.typernate.entities;

import java.util.List;

import de.crazything.sql.annotation.Column;
import de.crazything.sql.annotation.NotPersistent;
import de.crazything.sql.annotation.Serial;
import de.crazything.sql.annotation.Table;
import de.crazything.sql.typernate.annotation.DbTypeObject;

@Table("tbl_typetest_with_list")
public class TestEntityWithList {
    @Serial
    int id;
    @Column(name = "itype")
    @DbTypeObject(target = "testType", varrayType = "type_test_varray")
    Object typeTest;
    @NotPersistent
    List<TestType> testType;

    public int getId() {
	return this.id;
    }

    public void setId(final int id) {
	this.id = id;
    }

    public List<TestType> getTestType() {
	return this.testType;
    }

    public void setTestType(final List<TestType> testType) {
	this.testType = testType;
    }
}
