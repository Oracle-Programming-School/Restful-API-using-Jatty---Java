/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.srl.shopify_estore_api;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "Shopify", urlPatterns = {"shopify"}, loadOnStartup = 1)
public class EventResource extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.getWriter().print("[{\"Message\":" + "\"" + "Hello" + "\"}]");
    }

    protected Connection getConnection() throws SQLException, NamingException {
        InitialContext ic = new InitialContext();
        DataSource ds = (DataSource) ic.lookup("jdbc/DSTix");
        return ds.getConnection();
    }

    public int getInt(String p) {
        try {
            return Integer.valueOf(p);
        } catch (Exception e) {
            return 0;
        }
    }

}
