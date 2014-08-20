package de.crazything.sql.typernate.conf;

import de.crazything.sql.Database;

public class OraDatabaseTestImpl implements Database {
    private final String name = "mydb";
    private final String dbUser = "roger";
    private final String dbPass = "sf3";
    private final String dbConnection = "jdbc:oracle:thin:@//192.168.178.31:1521/xe";
    private final String driverString = "oracle.jdbc.OracleDriver";
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
