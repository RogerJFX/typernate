package de.crazything.sql.typernate.entities;

import de.crazything.sql.typernate.annotation.DbType;
import de.crazything.sql.typernate.annotation.DbTypeField;

@DbType("type_test_int_string")
public class TestInnerType {

    @DbTypeField(index = 0)
    int innerId;
    @DbTypeField(index = 1, quote = true)
    String string;

    @Override
    public String toString() {
	return '(' + this.innerId + "_:_" + this.string + ')';
    }

    public String getString() {
	return this.string;
    }

    public void setString(final String string) {
	this.string = string;
    }
}
