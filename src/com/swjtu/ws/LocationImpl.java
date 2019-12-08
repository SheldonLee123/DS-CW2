package com.swjtu.ws;

import javax.jws.WebService;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//Service Implementation
@WebService(endpointInterface = "com.swjtu.ws.Location")
public class LocationImpl implements Location {

    @Override
    public String getLocation(String location) throws ClassNotFoundException {

        if(is_alpha(location) && checkCity(location)){
//            System.out.println("The city you are looking for is " + location);
            return location;
        }else{
//            System.out.println("Your input is invalid!");
            return null;
        }
    }

    /**
     * judge is alpha
     * @param str
     * @return
     */
    public boolean is_alpha(String str) {
        if(str==null) return false;
        return str.matches("[a-zA-Z]+");
    }

    /**
     * check if city exist in the database
     * @param location
     * @return
     * @throws ClassNotFoundException
     */
    public boolean checkCity(String location) throws ClassNotFoundException{
        Connection connection = null;

        try
        {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            //search the city in the table city
            ResultSet rs = statement.executeQuery("select 1 from city where CITY_EN = '" + location + "'");
            while(rs.next())
            {
                // read the result set
                String result = rs.getString(1);
                if(result.equals("1")){
                    return true;
                }else{
                    return false;
                }
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                // connection close failed.
                System.err.println(e);
            }
        }
        return false;
    }

}
