package de.crazything.sql.typernate.entities;

import de.crazything.sql.typernate.annotation.DbType;
import de.crazything.sql.typernate.annotation.DbTypeField;

@DbType("type_test_single")
public class TestTypeSingle {
    @DbTypeField(index = 0)
    int id;
    @DbTypeField(index = 1, quote = true)
    String name;
    @DbTypeField(index = 2)
    TestInnerType inner;

    @Override
    public String toString() {
	return this.id + "," + this.name + ", inner: " + this.inner;
    }

    public int getId() {
	return this.id;
    }

    public void setId(final int id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(final String name) {
	this.name = name;
    }

    public TestInnerType getInner() {
	return this.inner;
    }

    public void setInner(final TestInnerType inner) {
	this.inner = inner;
    }
}
