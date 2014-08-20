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
import de.crazything.sql.typernate.entities.TestType;
import de.crazything.sql.typernate.factory.TypeFactory;
import de.crazything.sql.typernate.parse.OraParserHelper;
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

    private void doTestSimpleEntity(final String jsonString) {
	final TestEntity testObj = TypeFactory.generateObject(TestEntity.class, jsonString);
	final TestEntity serObj = EntitySerializer.serializeEntity(TestEntity.class, testObj);
	final int rows = DbManager.persist("mydb", serObj);
	System.out.println(rows + "rows persisted");

	final List<TestEntity> res = DbManager.fetch("mydb", TestEntity.class,
		"select * from tbl_typetest where id=(select max(id) from tbl_typetest)");
	final TestEntity deserObj = EntityDeserializer.deserializeEntity(TestEntity.class, res.get(0), true, true);
	Assert.assertEquals(deserObj.getTestType().toString(), serObj.getTestType().toString());
    }

    private void doTestType(final String jsonString) {
	TypeFactory.generateObject(TestType.class, jsonString);

	// Fetch it back from database
	final List<StructWrapper> wrappers = DbManager.fetch("mydb", StructWrapper.class,
		"select itype as rawType from tbl_typetest where id=(select max(id) from tbl_typetest)");
	final Struct testString = wrappers.get(0).rawType;
	final TestType from = TypeStructMapper.deserializeType(TestType.class, testString);
	final String jsonResult = cleanStringForAssertEqual(CommonJsonDao.getInstance(TestType.class).createJsonString(
		from));
	System.out.println(jsonResult);
	// Something changed???
	Assert.assertEquals(jsonResult, jsonString);

    }

    @Test(enabled = true)
    public void testSimpleEntity() {
	this.doTestSimpleEntity(TypeFactory.JSON_TEST_ENTITY);

    }

    @Test(enabled = true, dependsOnMethods = { "testSimpleEntity" })
    public void testType() {
	this.doTestType(TypeFactory.JSON_TEST_ENTITY_TYPE);

    }

}
