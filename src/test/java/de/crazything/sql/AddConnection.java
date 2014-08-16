package de.crazything.sql;

//CHECKSTYLE:OFF
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Additional connection, if all connections of the pool are busy. This should
 * never happen, since we monitor database access and cache results. Otherwise
 * we would some time cause some deadlock in database anyhow.
 * 
 * @author roger
 * 
 */
public class AddConnection implements Connectionable {
    Connection connection;

    public AddConnection(final Database database) {
	this.connection = this.createConnection(database);
    }

    private final Connection createConnection(final Database database) {
	Connection back = null;
	try {
	    Class.forName(database.getDbDriverString());
	    back = DriverManager.getConnection(database.getDbConnection(), database.getDbUser(), database.getDbPass());
	} catch (final ClassNotFoundException e) {
	    // Fuck it
	    e.printStackTrace();
	} catch (final SQLException e) {
	    // Fuck it
	    e.printStackTrace();
	}
	return back;
    }

    @Override
    public Connection getConnection() {

	return this.connection;
    }

    @Override
    public void setFree() {
	try {
	    this.connection.close();
	} catch (final Exception e) {

	}
    }

    @Override
    public void closeConnection() {
	this.setFree();
    }

    @Override
    public boolean isBusy() {
	return false;
    }

}
// CHECKSTYLE:ON