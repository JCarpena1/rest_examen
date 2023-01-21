/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.eldelbit.rest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author virtualbox
 */
public class DB {

    public static void registerDriver() throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");

    }

    public static Connection getConnection() throws SQLException {

        String protocol = "jdbc:mysql://";
        String hosts = "127.0.0.1";
        String port = "3306";
        String database = "restful";

        String url = protocol + hosts + ":" + port + "/" + database;

        Properties properties = new Properties();

        properties.put("user", "root"); // usuario
        properties.put("password", "changeme"); // contraseña

        properties.put("useUnicode", "true");
        properties.put("characterEncoding", "UTF-8");

        // properties.put("serverTimezone", "UTC");
        
        properties.put("useSSL", "false");
        properties.put("allowPublicKeyRetrieval", "true");

        // String properties = "?zeroDateTimeBehavior=convertToNull&useGmtMillisForDatetimes=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false";
        
        return DriverManager.getConnection(url, properties);

    }

    public static void closeConnection(Connection conn) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sqlEx) {
            }
        }

    }

    public static void closeStatement(Statement stmt) {

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException sqlEx) {
            }
        }

    }

    public static void closeResultSet(ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sqlEx) {
            }
        }

    }

    public static Integer getInt(ResultSet rs, String field) throws SQLException {
        
        Integer value = rs.getInt(field);
        if (rs.wasNull()) {
            value = null;
        }
        return value;
        
    }
}
