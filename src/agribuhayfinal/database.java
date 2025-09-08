/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agribuhayfinal;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author PC
 */
class database {
    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/products", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
