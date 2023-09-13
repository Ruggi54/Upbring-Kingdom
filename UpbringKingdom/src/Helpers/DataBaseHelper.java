/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

import java.sql.*;

/**
 *
 * @author simon
 */
public class DataBaseHelper {
    public static void InitConnection() {
        
        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;
        
        try {
            
            // 1. Creiamo la connessione al database
            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop?user=root");
            
            // 2. Creiamo lo statement
            myStmt = myConn.createStatement();

            // 3. Eseguiamo la query
            myRs = myStmt.executeQuery("select * from prodotto");

            // 4. Diamo in output la ciò di cui abbiamo bisogno
            while (myRs.next()) {
                System.out.println(myRs.getString("nome") + ", " + myRs.getString("descrizione"));
            }
            
            myConn.close();

            // work around per il database
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }
    }
}
