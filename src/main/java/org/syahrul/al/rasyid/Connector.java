package org.syahrul.al.rasyid;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private Connection con;
    private String addr;
    private String user;
    private String pwd;

    public Connector(Dotenv cfg) {
        this.addr = String.format("jdbc:mysql://%s:%s/%s",
                cfg.get("DB_ADDRESS"),
                cfg.get("DB_PORT"),
                cfg.get("DB_NAME"));
        this.user = cfg.get("DB_USER");
        this.pwd = cfg.get("DB_PASSWORD");
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
