package org.syahrul.al.rasyid;

import javax.swing.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connector con = new Connector("jdbc:mysql://localhost:3306/kuliah",
                "root", "rahasia123");

        JFrame gui = new Gui(con);
        gui.setVisible(true);
    }
}
