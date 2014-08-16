package de.crazything.sql;

//CHECKSTYLE:OFF
public class ConnectionPool {
    private Connectionable[] connectionPool;
    private boolean[] connectionsFree;
    // private static ConnectionPool me;
    private final Database database;
    // private static final int NUM_CONS =
    // DbProperties.getIntProperty(DbProperties.PROP_NUM_DB_CONS);
    private final int numCons;

    public ConnectionPool(final Database database) {
	// me = this;
	this.database = database;
	this.numCons = database.getDbConnections();
	Runtime.getRuntime().addShutdownHook(new ConnectionSlayer(this));
	this.createConnectionPool();
    }

    private final/* synchronized */void createConnectionPool() {
	this.closeAllConnections();
	final int numCons = this.database.getDbConnections();
	this.connectionPool = new PersistentConnection[numCons];
	this.connectionsFree = new boolean[numCons];
	for (int i = 0; i < numCons; i++) {
	    this.connectionsFree[i] = false;
	    this.connectionPool[i] = new PersistentConnection(this, this.database, i);

	    System.out.println("CONNECTION " + i);
	}
	// notifyAll();
	try {
	    Thread.sleep(200);
	} catch (final Exception e) {
	}

	for (int i = 0; i < numCons; i++) {
	    ((PersistentConnection) this.connectionPool[i]).begin();
	}
    }

    public Connectionable getConnection() {

	try {
	    return this.connectionPool[this.getFreeIndex()];
	} catch (final PoolBusyException pbe) {
	    return new AddConnection(this.database);
	}
    }

    public void closeAllConnections() {
	if (this.connectionPool == null) {
	    return;
	}
	try {
	    for (int i = 0; i < this.connectionPool.length; i++) {
		if (this.connectionPool[i] != null) {
		    this.connectionPool[i].closeConnection();
		}
	    }
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    protected void setConnectionReady(final int index) {
	this.setConnectionFree(index);
    }

    void setConnectionFree(final int index) {
	this.connectionsFree[index] = true;
    }

    private int getFreeIndex() throws PoolBusyException {
	for (int i = 0; i < this.numCons; i++) {
	    if (this.connectionsFree[i] && !this.connectionPool[i].isBusy()) {
		return i;
	    }
	}
	throw new PoolBusyException();
    }
}
// CHECKSTYLE:ON
