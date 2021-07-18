package org.syahrul.al.rasyid;

import config.Config;

import javax.swing.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Config cfg = new Config();
        Connector con = new Connector(cfg.getConfig());

        JFrame gui = new Gui(con);
        gui.setVisible(true);
    }
}
