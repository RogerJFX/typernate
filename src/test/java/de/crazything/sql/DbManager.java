package de.crazything.sql;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manager for database access. Note: we have more than one database.
 * 
 * @author roger
 * 
 */
public class DbManager {

    private static Map<String, ConnectionPool> pools = new HashMap<String, ConnectionPool>();

    private static boolean inited;

    public static void init(final Database[] databases) {
	for (final Database database : databases) {
	    final ConnectionPool candidate = new ConnectionPool(database);
	    pools.put(database.getName(), candidate);
	}
	inited = true;
    }

    public static <T> List<T> fetch(final String databaseName, final Class<T> c, final String sql,
	    final Object... params) {
	if (!inited) {
	    throw new IllegalStateException("Database configuration not initialized. Forgot to call DbManager.init?");
	}
	final Connectionable myCon = pools.get(databaseName).getConnection();
	final Connection con = myCon.getConnection();
	return SqlExecuterPrep.fetchObjectResultset(c, sql, con, myCon, params);
    }

    public static int persist(final String databaseName, final Object obj) {
	if (!inited) {
	    throw new IllegalStateException("Database configuration not initialized. Forgot to call DbManager.init?");
	}
	final Connectionable myCon = pools.get(databaseName).getConnection();
	final Connection con = myCon.getConnection();
	return SqlExecuterPrep.persist(con, myCon, obj);
    }

    // public static int executeQuery(final String databaseName, final String
    // query, final Object... params) {
    // if (!inited) {
    // throw new
    // IllegalStateException("Database configuration not initialized. Forgot to call DbManager.init?");
    // }
    // final Connectionable myCon = pools.get(databaseName).getConnection();
    // final Connection con = myCon.getConnection();
    // return SqlExecuterPrep.executeQuery(query, con, myCon);
    // }

}
