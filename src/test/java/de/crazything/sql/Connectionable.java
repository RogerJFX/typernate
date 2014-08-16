package de.crazything.sql;

//CHECKSTYLE:OFF

import java.sql.Connection;

public interface Connectionable {

    public Connection getConnection()/* throws java.sql.SQLException */;

    public void setFree();

    boolean isBusy();

    void closeConnection();
}

// CHECKSTYLE:ON