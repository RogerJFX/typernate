package de.crazything.sql;

/**
 * Interface to pass Impl to the DbManager.
 * 
 * @author roger
 * 
 */
public interface Database {

    public String getName();

    public String getDbConnection();

    public String getDbUser();

    public String getDbPass();

    public String getDbDriverString();

    public String getDbType();

    public int getDbConnections();

}
