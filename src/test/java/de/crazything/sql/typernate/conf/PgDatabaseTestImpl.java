package de.crazything.sql.typernate.conf;

import de.crazything.sql.Database;

public class PgDatabaseTestImpl implements Database {
    private final String name = "mydb";
    private final String dbUser = "roger";
    private final String dbPass = "sf3";
    private final String dbConnection = "jdbc:postgresql://localhost:5432/typernate";
    private final String driverString = "org.postgresql.Driver";
    private final int dbConnections = 2;

    @Override
    public String getName() {
	return this.name;
    }

    @Override
    public String getDbConnection() {
	return this.dbConnection;
    }

    @Override
    public String getDbUser() {
	return this.dbUser;
    }

    @Override
    public String getDbPass() {
	// TODO Auto-generated method stub
	return this.dbPass;
    }

    @Override
    public String getDbDriverString() {
	return this.driverString;
    }

    @Override
    public String getDbType() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getDbConnections() {
	return this.dbConnections;
    }

}
