package de.crazything.sql;

//CHECKSTYLE:OFF
public class ConnectionSlayer extends Thread {

    private final ConnectionPool pool;

    public ConnectionSlayer(final ConnectionPool pool) {
	this.pool = pool;
    }

    @Override
    public void run() {
	this.pool.closeAllConnections();
    }
}
// CHECKSTYLE:ON