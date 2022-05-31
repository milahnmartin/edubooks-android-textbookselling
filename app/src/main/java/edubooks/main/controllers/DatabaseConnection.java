package edubooks.main.controllers;

import java.sql.*;

public class DatabaseConnection {
    final private static String UserName = "doadmin";
    final private static String Password = "AVNS_iFgMFL58Qnl6NqD";
    final private static String HostName = "edu-books-db-do-user-10572576-0.b.db.ondigitalocean.com";
    final private static int PortNumber = 25060;
    final private static String DatabaseNameDevelopment = "development_edubooks";
    final private String DatabaseNameProduction = "production_edubooks";

    public static Connection getConnection() throws Exception {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://"+HostName+":"+PortNumber+"/"+DatabaseNameDevelopment,UserName,Password);
        }catch(Exception e){
            throw new Exception(e);
        }


    }
}