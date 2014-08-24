package de.crazything.sql.typernate;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class JdoPostgreTest {

    // @BeforeClass
    // private void init() throws NamingException {
    // if (!TEST_ENABLED) {
    // return;
    // }
    // final PGPoolingDataSource ds = new PGPoolingDataSource();
    // ds.setDataSourceName("A Data Source");
    // ds.setServerName("localhost");
    // ds.setDatabaseName("typernate");
    // ds.setUser("roger");
    // ds.setPassword("sf3");
    // ds.setMaxConnections(2);
    // System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
    // "com.sun.jndi.rmi.registry.RegistryContextFactory");
    // System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1099");
    // final InitialContext ic = new InitialContext();
    // ic.bind("testDS", ds);
    // }
    //
    // @AfterClass
    // public static void tearDownClass() throws Exception {
    // if (!TEST_ENABLED) {
    // return;
    // }
    // final InitialContext ic = new InitialContext();
    // ic.unbind("testDS");
    // }

    @Test
    public void test1() {
	final PersistenceManagerFactory pmf1 = JDOHelper.getPersistenceManagerFactory("jdoPostgre");
	final PersistenceManager pm1 = pmf1.getPersistenceManager();
	Assert.assertNotNull(pm1);
    }
}
