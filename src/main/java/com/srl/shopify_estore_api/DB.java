/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srl.shopify_estore_api;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author M.Faisal1521
 */
public class DB {

    String ConnectString;
    String ConnectUSER;
    String ConnectPassword;
    Connection ApplicationSession = null;
    Connection mssqlConnection = null;
    Connection CandelaConnection = null;

    public DB() {
    }

    /**
     * GetDBConnection
     */
    public Connection getDBConnection() {

        if (ApplicationSession != null) {

            try {
                if (!ApplicationSession.isClosed()) {
                    return ApplicationSession;
                }
            } catch (SQLException ex) {
                System.out.println("Error ! Connection is not re-opening. " + ex.getMessage());
            }
        }

        String ReturnString = null;

        try {
            //step1 load the driver class
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //step2 create  the connection object
            ApplicationSession = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.1:1521:xe", "test", "test");

        } catch (SQLException ex) {
            ReturnString = "Database connection not found : " + ex.getMessage();
        } catch (ClassNotFoundException ex) {
            ReturnString = "Java error database class not found.";
        }

        return ApplicationSession;
    }

}
