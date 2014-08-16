package de.crazything.sql;

//CHECKSTYLE:OFF
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Persistent stands for all the time open. Those connections are in
 * ConnectionPool.
 * 
 * @author roger
 * 
 */
public class PersistentConnection implements Runnable, Connectionable {

    Connection connection;
    ConnectionPool pool;
    int myIndex;
    boolean isBusy = false;
    Database database;

    PersistentConnection(final ConnectionPool pool, final Database database, final int myIndex) {
	this.pool = pool;
	this.database = database;
	this.myIndex = myIndex;
	new Thread(this).start();
	pool.setConnectionReady(myIndex);
	// setFree();
    }

    private final Connection createConnection() {
	Connection back = null;
	try {
	    Class.forName(this.database.getDbDriverString());
	    back = DriverManager.getConnection(this.database.getDbConnection(), this.database.getDbUser(),
		    this.database.getDbPass());
	} catch (final ClassNotFoundException e) {
	    // Fuck it
	    e.printStackTrace();
	} catch (final SQLException e) {
	    // Fuck it
	    e.printStackTrace();
	}
	return back;
    }

    /**
     * 
     * @return Connection
     * @throws SQLException
     *             Falls connection.isClosed() Mist baut. Aber dann sollte man
     *             den Server einfach neu starten...
     */
    @Override
    public Connection getConnection() {
	synchronized (this) {
	    try {
		if (this.connection == null || this.connection.isClosed()) {
		    throw new RuntimeException("Database not inited");
		}

	    } catch (final Exception e) {
	    }
	    this.isBusy = true;
	    return this.connection;
	}
    }

    @Override
    public void closeConnection() {
	try {
	    if (this.connection != null && !this.connection.isClosed()) {
		this.connection.close();
	    }
	} catch (final Exception e) {
	}
    }

    public synchronized void begin() {
	// LoggerFactory.getLogger(this.getClass()).info("notify");
	this.waiting = false;
	this.notify();
    }

    private boolean waiting = true;

    @Override
    public synchronized void run() {
	// //System.out.println("CONNECTION " + myIndex +" waiting");
	try {
	    while (this.waiting) {
		this.wait();
	    }
	    this.connection = this.createConnection();
	    // //System.out.println("CONNECTION " + myIndex +" running");
	    // notifyAll();
	} catch (final Exception e) {
	    e.printStackTrace();
	    // und fahre am besten den Server wieder runter
	}
    }

    @Override
    public boolean isBusy() {
	synchronized (this) {
	    return this.isBusy;
	}
    }

    @Override
    public void setFree() {
	synchronized (this) {
	    /*
	     * try { connection.commit(); } catch (Exception e) { try {
	     * connection.rollback(); } catch (Exception e2) {
	     * 
	     * } }
	     */
	    this.pool.setConnectionFree(this.myIndex);
	    this.isBusy = false;
	}
    }

}
// CHECKSTYLE:ON