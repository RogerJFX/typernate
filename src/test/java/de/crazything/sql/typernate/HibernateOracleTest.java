package de.crazything.sql.typernate;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.sql.SQLException;
import java.sql.Struct;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import oracle.jdbc.pool.OracleDataSource;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import de.crazything.sql.typernate.entities.TestEntityOra;
import de.crazything.sql.typernate.factory.TypeFactory;

//import junit.framework.Assert;

/**
 * Before running this Test, you should start your rmiregistry.
 * 
 * <pre>
 * Unix: 
 * 	rmiregistry OR (if you want to type fg afterwards) rmiregistry &
 * Windows:
 * 	I don't know...
 * 
 * 
 * Hibernate really is a pain in my ars (and it always was...). Did they once
 * really want to rule the world? I was looking for a way to push a plain
 * PGobject throw that djungle for hours. The solution:
 * 
 * <pre>
 * 	@org.hibernate.annotations.Type(type = "text").
 * </pre>
 * 
 * This can only work for Postgres, not for ORACLE, because ORACLE will give us
 * a struct. And does it work for other JPA implementations? If so, how???
 * 
 * 
 * 
 * @author roger
 * 
 */
// Does not work. I'm stuck. F... JPA!
@Test
public class HibernateOracleTest {
    private static final boolean TEST_ENABLED = false;

    @BeforeClass
    private void init() throws NamingException, SQLException {
	if (!TEST_ENABLED) {
	    return;
	}
	final OracleDataSource ds = new OracleDataSource();
	// final PGPoolingDataSource ds = new PGPoolingDataSource();
	ds.setDataSourceName("A Data Source");
	ds.setServerName("192.168.178.31");
	ds.setDatabaseName("xe");
	ds.setUser("roger");
	ds.setPassword("sf3");
	ds.setURL("jdbc:oracle:thin:@//192.168.178.31:1521/xe");
	// ds.set
	System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1099");
	final InitialContext ic = new InitialContext();
	ic.bind("testDSOra", ds);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
	if (!TEST_ENABLED) {
	    return;
	}
	final InitialContext ic = new InitialContext();
	ic.unbind("testDSOra");
    }

    static java.sql.Struct deserialize(final Object obj/* , final byte[] bytes */) throws IOException,
	    ClassNotFoundException {
	final ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) obj);
	final DataInputStream in = new DataInputStream(bis);

	return (Struct) ((ObjectInput) in).readObject();
    }

    @Test(enabled = TEST_ENABLED)
    public void test1() throws SQLException {
	final EntityManager entityManager = Persistence.createEntityManagerFactory("testPUOra").createEntityManager();
	Assert.assertNotNull(entityManager);
	final TestEntityOra testObj = TypeFactory.generateObject(TestEntityOra.class, TypeFactory.JSON_TEST_ENTITY);
	final TestEntityOra serObj = EntitySerializer.serializeEntity(TestEntityOra.class, testObj);
	entityManager.getTransaction().begin();
	entityManager.persist(serObj);
	entityManager.getTransaction().commit();
	// entityManager.flush();
	// entityManager.close();
	// TestEntityOra found = entityManager.find(TestEntityOra.class, 74);
	// Assert.assertNotNull(found);
	//
	// final byte[] b = (byte[]) found.typeTest;
	// // oracle.sql.Datum str = new oracle.sql.Datum(b);
	// // System.out.println(new String(b));
	// // System.out.println(found.getId() + "--->" + b.length);
	// // for (final byte bb : b) {
	// // System.out.print(bb);
	// // System.out.print("_");
	// // }
	// final RAW raw = new RAW(b);
	// final oracle.sql.Datum str = raw;
	// System.out.println(str.isConvertibleTo(oracle.sql.STRUCT.class));
	// found.typeTest = raw.toJdbc();
	// // final Struct str = (Struct) raw.toJdbc();
	// // found.typeTest = str;
	// System.out.println(raw.stringValue());
	// System.out.println(found.typeTest.getClass().getName());
	// // for (final byte bb : b) {
	// // System.out.print((char) bb);
	// // System.out.print("_");
	// // }
	// System.out.println(found.typeTest);
	// System.out.println(raw.isConvertibleTo(java.sql.Struct.class));
	// new Object();
	// // struct.
	// // oracle.core.
	// // try {
	// // found.typeTest = deserialize(found.typeTest);
	// // } catch (ClassNotFoundException | IOException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
	// found = EntityDeserializer.deserializeEntity(TestEntityOra.class,
	// found, true, true);
	// System.out.println(CommonJsonDao.getInstance(TestEntityOra.class).createJsonString(found));
    }
}
