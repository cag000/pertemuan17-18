package org.syahrul.al.rasyid;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private Connection con;
    private String addr;
    private String user;
    private String pwd;

    public Connector(String addr, String user, String pwd) {
        this.addr = addr;
        this.user = user;
        this.pwd = pwd;
    }

    public Connection connect() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try {
            con = DriverManager.getConnection(addr, user, pwd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
