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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "Shopify", urlPatterns = {"shopify"}, loadOnStartup = 1)
public class EventResource extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CallableStatement dCall = null;
        try {
            //Check Authid from header
            try {
                String authid = request.getHeaders("authid").nextElement();
                if (authid == null) {
                    throw new Exception("authid parameter is missing : authid (Text)");
                }

                if (!authid.equals("SRL11062021")) {
                    throw new Exception("Please enter valid authid parameter.");
                }
            } catch (NoSuchElementException e) {
                throw new Exception("authid parameter is missing : authid (Text)");
            }

            String Order_Id = null;
            String cn_number = null;
            String courier_name = null;

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            reader.mark(10000);
            String line;
            do {
                line = reader.readLine();
                if (line != null) {
                    sb.append(line);
                }
            } while (line != null);
            reader.reset();
            System.out.println("Request Parameters : " + sb.toString());
                //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(sb.toString());
            JSONObject OrdersObj = (JSONObject) obj;

            Order_Id = (String) OrdersObj.get("order_id").toString();
            cn_number = (String) OrdersObj.get("cn_number").toString();
            courier_name = (String) OrdersObj.get("courier_name").toString();

            if (Order_Id == null) {
                throw new Exception("Order ID parameter is missing : Order_ID (Int)");
            }

            /*if (this.getInt(Order_Id) == 0) {
                throw new Exception("Invalid value for Order ID  : Order_ID (Int)" + Order_Id + " - " + getInt(Order_Id));
            }*/

            if (cn_number == null) {
                throw new Exception("CN Number parameter is missing : cn_number (Text)");
            }

            if (courier_name == null) {
                throw new Exception("Courier parameter is missing : courier_name (Text)");
            }

            System.out.println("Call Procedure ");
            DB db = new DB();
            dCall = db.getDBConnection().prepareCall("begin submit_smartlan_cn(p_order_id => :p_order_id,\n"
                    + "                     p_cn_number => :p_cn_number,\n"
                    + "                     p_courier_name => :p_courier_name,\n"
                    + "                     p_message => :p_message); end;");
            dCall.setString(1, Order_Id);
            dCall.setString(2, cn_number);
            dCall.setString(3, courier_name);
            dCall.registerOutParameter(4, java.sql.Types.VARCHAR);
            dCall.execute();
            String messageOut = (String) dCall.getString(4);
            dCall.close();
            this.getConnection().close();

            System.out.println("Call Procedure End.");

            response.getWriter().print("[{\"Message\":" + "\"" + messageOut + "\"}]");

        } catch (Exception e) {
            response.getWriter().print("[{\"Error\":" + "\"" + e.getMessage().toString() + "\"}]");
            try {
                dCall.close();
            } catch (Exception ex) {
            }
        }
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
