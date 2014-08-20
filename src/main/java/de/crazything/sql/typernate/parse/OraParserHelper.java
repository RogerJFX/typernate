package de.crazything.sql.typernate.parse;

public class OraParserHelper extends ParserHelper {

    /**
     * insert into tbl_typetest_single (id, itype) values (1,
     * type_test_single(123456,'admin', type_test_int_string(1,'foo'))); insert
     * into tbl_typetest (id, itype) values (1, type_test(123456,'admin',
     * type_test_int_string_varray(type_test_int_string(1,'foo'))));
     * 
     * ROGER.TYPE_TEST_SINGLE(123456,'admin',ROGER.TYPE_TEST_INT_STRING(1,'foo')
     * ) ROGER.TYPE_TEST(123456,
     * 'admin',ROGER.TYPE_TEST_INT_STRING_VARRAY(ROGER.TYPE_TEST_INT_STRING(1,'f
     * o o ' ) ) )
     * 
     * 
     */
    // ################################################################
    // NOT NEEDED METHODS, SINCE ORACLE-DRIVER SUPPORTS java.sql.struct
    // SEE de.crazything.sql.typernate.TypeStructDeserializer
    // ################################################################
    @Override
    public int findObjectDataStart(final char[] in) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public String stripArray(final String in) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String stripQuotes(final String in) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean isArray(final String in) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public ParseResult nextObject(final char[] line, final int from) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public ParseResult nextField(final char[] line, final int from) {
	// TODO Auto-generated method stub
	return null;
    }

    // ################################################################
    // ################################################################
    @Override
    public String escapeString(final String in) {
	return in.replaceAll("'", "''");
    }

    @Override
    public String wrapArray(final StringBuilder in, final String type, final String oraVarray) {
	final StringBuilder builder = new StringBuilder();
	builder.append(oraVarray).append("(").append(in).append(")");
	return builder.toString();
    }

    @Override
    public StringBuilder wrapObject(final StringBuilder in) {
	final StringBuilder builder = new StringBuilder();
	builder.append('(').append(in).append(')');
	return builder;
    }

    @Override
    public StringBuilder appendType(final StringBuilder in, final String type) {
	final StringBuilder builder = new StringBuilder();
	builder.append(type).append(in);
	return builder;
    }

    @Override
    public String doSerialize(final String[] values, final String dbType, final boolean fromArray) {
	StringBuilder builder = new StringBuilder();
	builder.append(values[0]);
	for (int i = 1; i < values.length; i++) {
	    builder.append("," + values[i]);
	}
	builder = this.wrapObject(builder);
	builder = this.appendType(builder, dbType);
	return builder.toString();
    }

    @Override
    public boolean isSqlStructSupported() {
	return true;
    }

}
