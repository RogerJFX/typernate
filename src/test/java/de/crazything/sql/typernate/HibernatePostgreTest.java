package de.crazything.sql.typernate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import junit.framework.Assert;

import org.postgresql.ds.PGPoolingDataSource;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import de.crazything.json.dao.CommonJsonDao;
import de.crazything.sql.typernate.entities.TestEntity;

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

@Test
public class HibernatePostgreTest {
    @BeforeTest
    private void init() throws NamingException {
	final PGPoolingDataSource ds = new PGPoolingDataSource();
	ds.setDataSourceName("A Data Source");
	ds.setServerName("localhost");
	ds.setDatabaseName("typernate");
	ds.setUser("roger");
	ds.setPassword("sf3");
	ds.setMaxConnections(2);
	System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1099");
	final InitialContext ic = new InitialContext();
	ic.bind("testDS", ds);
    }

    @AfterTest
    public static void tearDownClass() throws Exception {
	final InitialContext ic = new InitialContext();
	ic.unbind("testDS");
    }

    @Test(enabled = false)
    public void test1() {
	final EntityManager entityManager = Persistence.createEntityManagerFactory("testPU").createEntityManager();
	Assert.assertNotNull(entityManager);
	TestEntity found = entityManager.find(TestEntity.class, 12);
	Assert.assertNotNull(found);
	// System.out.println(found.getId() + "--->" + found.typeTest);
	found = EntityDeserializer.deserializeEntity(TestEntity.class, found, true);
	System.out.println(CommonJsonDao.getInstance(TestEntity.class).createJsonString(found));
    }
}
