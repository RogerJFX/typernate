package de.crazything.sql.typernate;

import java.sql.Struct;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.crazything.json.dao.CommonJsonDao;
import de.crazything.sql.Database;
import de.crazything.sql.DbManager;
import de.crazything.sql.typernate.conf.OraDatabaseTestImpl;
import de.crazything.sql.typernate.entities.TestEntity;
import de.crazything.sql.typernate.entities.TestEntityWithList;
import de.crazything.sql.typernate.entities.TestType;
import de.crazything.sql.typernate.entities.TestTypeSingle;
import de.crazything.sql.typernate.factory.TypeFactory;
import de.crazything.sql.typernate.parse.OraParserHelper;
import de.crazything.sql.typernate.testwrappers.IntWrapper;
import de.crazything.sql.typernate.testwrappers.StructWrapper;

//import de.crazything.app.stock.conf.ConfiguratorMock;

@Test
public class OraTypeSerializerTest {

    @BeforeClass
    private void init() {
	DbManager.init(new Database[] { new OraDatabaseTestImpl() });
	TypeSerializer.setParserHelper(new OraParserHelper());
	TypeDeserializer.setParserHelper(new OraParserHelper());
    }

    private static String cleanStringForAssertEqual(final String in) {
	return in.replaceAll("\\\\u0027", "'");
    }

    private void doTestEntityWithList(final String jsonString) {
	final TestEntityWithList testObj = TypeFactory.generateObject(TestEntityWithList.class, jsonString);
	final TestEntityWithList serObj = EntitySerializer.serializeEntity(TestEntityWithList.class, testObj);
	final int rows = DbManager.persist("mydb", serObj);
	System.out.println(rows + " rows persisted");

	final List<TestEntityWithList> res = DbManager.fetch("mydb", TestEntityWithList.class,
		"select * from tbl_typetest_with_list where id=(select max(id) from tbl_typetest_with_list)");
	final TestEntityWithList deserObj = EntityDeserializer.deserializeEntity(TestEntityWithList.class, res.get(0),
		true, true);
	// System.out.println(deserObj.getTestType().get(0).getName());
	final String jsonResult = CommonJsonDao.getInstance(TestEntityWithList.class).createJsonString(deserObj);
	System.out.println(jsonResult);
	Assert.assertEquals(deserObj.getTestType().size(), serObj.getTestType().size());
	Assert.assertEquals(deserObj.getTestType().toString(), serObj.getTestType().toString());
    }

    private void doTestSimpleEntity(final String jsonString) {
	final TestEntity testObj = TypeFactory.generateObject(TestEntity.class, jsonString);
	final TestEntity serObj = EntitySerializer.serializeEntity(TestEntity.class, testObj);
	final int rows = DbManager.persist("mydb", serObj);
	System.out.println(rows + "rows persisted");

	final List<TestEntity> res = DbManager.fetch("mydb", TestEntity.class,
		"select * from tbl_typetest where id=(select max(id) from tbl_typetest)");
	// final TestEntity te = res.get(0);
	// final byte[] ba = ((oracle.sql.STRUCT) te.typeTest).getBytes();
	// for (final byte b : ba) {
	// System.out.print(b);
	// System.out.print('_');
	// }
	final TestEntity deserObj = EntityDeserializer.deserializeEntity(TestEntity.class, res.get(0), true, true);
	Assert.assertEquals(deserObj.getTestType().toString(), serObj.getTestType().toString());
    }

    private void doTestType(final String jsonString) {
	final TestTypeSingle testObj = TypeFactory.generateObject(TestTypeSingle.class, jsonString);
	final String test = TypeSerializer.serializeType(TestTypeSingle.class, testObj);
	// Put it to database
	final List<IntWrapper> res = DbManager.fetch("mydb", IntWrapper.class, "select func_typeinsertsingle(" + test
		+ ") as result from dual");
	final int rowId = res.get(0).getResult();
	// Fetch it back from database
	final List<StructWrapper> wrappers = DbManager.fetch("mydb", StructWrapper.class,
		"select itype as rawType from tbl_typetest_single where id=" + rowId);
	final Struct testStruct = wrappers.get(0).rawType;
	final TestTypeSingle from = TypeStructMapper.deserializeType(TestTypeSingle.class, testStruct);
	final String jsonResult = cleanStringForAssertEqual(CommonJsonDao.getInstance(TestTypeSingle.class)
		.createJsonString(from));
	// Something changed???
	Assert.assertEquals(jsonResult, jsonString);

    }

    private void doTestTypeWithList(final String jsonString, final boolean verbose) {
	final TestType testObj = TypeFactory.generateObject(TestType.class, jsonString);
	final String test = TypeSerializer.serializeType(TestType.class, testObj);
	System.out.println(test);
	// Put it to database
	final List<IntWrapper> res = DbManager.fetch("mydb", IntWrapper.class, "select func_typeinsert(" + test
		+ ") as result from dual");
	final int rowId = res.get(0).getResult();
	// Fetch it back from database
	final List<StructWrapper> wrappers = DbManager.fetch("mydb", StructWrapper.class,
		"select itype as rawType from tbl_typetest where id=" + rowId);
	final Struct testStruct = wrappers.get(0).rawType;
	final TestType from = TypeStructMapper.deserializeType(TestType.class, testStruct);
	if (verbose) {
	    System.out.println("###### begin should equal");
	    System.out.println(testObj.toString());
	    System.out.println(from.toString());
	    System.out.println("String EQUALS: " + (testObj.toString().equals(from.toString())));
	    System.out.println("If it's equal, but Assertion failes, there is some problem elsewhere.");
	    System.out
		    .println("Are you sure your escapes are set correctly? Was it the Json-Parser swallowing some errors?");
	    System.out.println("###### end should equal");
	}
	final String jsonResult = cleanStringForAssertEqual(CommonJsonDao.getInstance(TestType.class).createJsonString(
		from));
	// Something changed???
	Assert.assertEquals(jsonResult, jsonString);

    }

    @Test(enabled = true)
    public void testSimpleEntity() {
	this.doTestSimpleEntity(TypeFactory.JSON_TEST_ENTITY);

    }

    @Test(enabled = true)
    public void testEntityWithList() {
	this.doTestEntityWithList(TypeFactory.JSON_TEST_ENTITY_WITH_LIST);

    }

    @Test(enabled = true)
    public void testType() {
	this.doTestType(TypeFactory.JSON_TEST_TYPE);
    }

    @Test(enabled = true)
    public void testTypeNull() {
	this.doTestType(TypeFactory.JSON_TEST_TYPE_NULL);

    }

    @Test(enabled = true)
    public void testTypeWithList() {
	this.doTestTypeWithList(TypeFactory.JSON_TEST_TYPE_WITH_LIST, true);

    }

    @Test(enabled = true)
    public void testTypeWithNastyList() {
	this.doTestTypeWithList(TypeFactory.JSON_TEST_NASTY_LIST, false);

    }

    @Test(enabled = true)
    public void testTypeWithVeryNastyList() {
	this.doTestTypeWithList(TypeFactory.JSON_TEST_VERY_NASTY_LIST, true);

    }

    @Test(enabled = true)
    public void testTypeWithListNull() {
	this.doTestTypeWithList(TypeFactory.JSON_TEST_TYPE_WITH_LIST_NULL, false);

    }

}
