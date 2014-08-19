package de.crazything.sql.typernate.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import de.crazything.sql.typernate.annotation.DbType;
import de.crazything.sql.typernate.annotation.DbTypeField;

@DbType("type_test")
public class TestType {
    @DbTypeField(index = 0)
    int id;
    @DbTypeField(index = 1, quote = true)
    String name;
    @DbTypeField(index = 2)
    @SerializedName("list")
    List<TestInnerType> list;

    @Override
    public String toString() {
	final StringBuilder listBuilder = new StringBuilder();
	if (this.list != null) {
	    for (final TestInnerType t : this.list) {
		listBuilder.append(t.toString());
	    }
	}
	return this.id + "," + this.name + "," + listBuilder.toString();
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

    public List<TestInnerType> getList() {
	return this.list;
    }

    public void setList(final List<TestInnerType> list) {
	this.list = list;
    }
}
