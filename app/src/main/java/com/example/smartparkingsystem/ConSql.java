package com.example.smartparkingsystem;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConSql {

    Connection con;

    @SuppressLint("NewApi")
    public Connection conclass() {
        String ip = "127.0.0.1", port = "1433", db = "Workshop2", username = "SQLWorkshop", password = "SQLWorkshop123";
        StrictMode.ThreadPolicy a = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(a);
        String connectURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectURL = "jdbc:jtds:sqlserver://" +ip+ ":" +port+ ";databasename=" + db + ";user=" + username + ";"+"password=" + password + ";";
            con = DriverManager.getConnection(connectURL);
        } catch (Exception e) {
            Log.e("Error :", e.getMessage());
        }
        return con;
    }

}
