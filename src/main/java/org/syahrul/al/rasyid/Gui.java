package org.syahrul.al.rasyid;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Gui extends JFrame {
    private JTextField alamatText;
    private JTextField idText;
    private JTextField nameText;
    private JTextField numberText;
    private JPanel mainPanel;
    private JLabel labelDataKontak;
    private JLabel idKontakLabel;
    private JLabel namaKontakLabel;
    private JLabel noKontakLabel;
    private JLabel alamatLabel;
    private JButton simpanButton;
    private JButton hapusButton;
    private JButton clearButton;
    private JButton lihatButton;
    private JButton refreshButton;
    private JButton searchButton;
    private JButton editDataButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private JComboBox statusComboBox;
    private JTable resutlTable;
    private JScrollPane jScrollPane1;
    private Statement stmt;
    private Connection con;
    private DefaultTableModel dtm;

    public Gui(Connector con) throws ClassNotFoundException, SQLException {
        super("Data contact");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setSize(854, 480);
        this.stmt = con.connect().createStatement();
        this.con = con.connect();
        status();

        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simpan();
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        lihatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lihat();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hapus();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        editDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                edit();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exit();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        resutlTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = resutlTable.getSelectedRow();
                if (i == -1) {
                    return;
                }
                String code = (String) resutlTable.getValueAt(i, 0);
                String code1 = (String) resutlTable.getValueAt(i, 1);
                String code2 = (String) resutlTable.getValueAt(i, 2);
                String code3 = (String) resutlTable.getValueAt(i, 3);
                String code4 = (String) resutlTable.getValueAt(i, 4);
                idText.setText(code);
                nameText.setText(code1);
                numberText.setText(code2);
                alamatText.setText(code3);
                statusComboBox.setSelectedItem(code4);
            }
        });
    }

    private void status() {
        statusComboBox.addItem("Keluarga");
        statusComboBox.addItem("Teman");
        statusComboBox.addItem("Kenalan");
        statusComboBox.addItem("Rekan Kerja");
        statusComboBox.setSelectedIndex(-1);
    }

    private void clear() {
        idText.setText("");
        nameText.setText("");
        numberText.setText("");
        alamatText.setText("");
        statusComboBox.setSelectedIndex(-1);
        idText.requestFocus();
    }

    private void simpan() {
        try {
            stmt.executeUpdate("insert into data_kontak values ("
                    + "'" + idText.getText() + "',"
                    + "'" + nameText.getText() + "',"
                    + "'" + numberText.getText() + "',"
                    + "'" + alamatText.getText() + "',"
                    + "'" + statusComboBox.getSelectedItem() + "')");
            clear();
            JOptionPane.showMessageDialog(null, "Berhasil Menyimpan Data");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Perintah Salah : " + e);
        }
    }

    private void hapus() {
        int ok = JOptionPane.showConfirmDialog(null, "Apakah Yakin Mendelete record ini???",
                "Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);
        if (ok == 0) {
            try {
                String sql = "delete from data_kontak where id_kontak='" + idText.getText() + "'";
                stmt.executeUpdate(sql);
                JOptionPane.showMessageDialog(null, "Delete Data Sukses");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Delete Data Gagal");
            }
        }
    }

    private void exit() throws SQLException {
        this.stmt.close();
        System.exit(0);
    }

    private void refresh() {
        lihat();
    }

    private void edit() {
        int ok = JOptionPane.showConfirmDialog(null, "Apakah Yakin Untuk Update Record ini???", "Confirmation", JOptionPane.YES_NO_OPTION);
        try {
            String sql = "update data_kontak set id_kontak=?,nama_kontak=?,no_kontak=?,Alamat=?,status=? where id_kontak = '" + idText.getText() + "'";
            PreparedStatement st = con.prepareStatement(sql);
            if (ok == 0) {
                try {
                    st.setString(1, idText.getText());
                    st.setString(2, nameText.getText());
                    st.setString(3, numberText.getText());
                    st.setString(4, alamatText.getText());
                    st.setString(5, (String) statusComboBox.getSelectedItem());
                    st.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Update Data Sukses");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Update Data Gagal");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lihat() {
        try {
            Object[] rows = {"Id", "Nama ", "No Kontak", "Alamat", "Status"};
            dtm = new DefaultTableModel(null, rows);
            resutlTable.setModel(dtm);
            resutlTable.setBorder(null);
            jScrollPane1.setVisible(true);
            jScrollPane1.setViewportView(resutlTable);
            int no = 1;
            String idKontak = "", namaKontak = "", noKontak = "", alamat = "", status = "";
            try {
                String sql = "select * from data_kontak";
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    idKontak = rs.getString("id_kontak");
                    namaKontak = rs.getString("nama_kontak");
                    noKontak = rs.getString("no_kontak");
                    alamat = rs.getString("alamat");
                    status = rs.getString("status");
                    String[] tampil = {"" + idKontak, namaKontak, noKontak, alamat, status};
                    dtm.addRow(tampil);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Query Salah " + e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search() {
        try {
            ResultSet res = stmt.executeQuery("select * from data_kontak where " + "id_kontak='" + idText.getText()
                    + "'");
            while (res.next()) {
                nameText.setText(res.getString("nama_kontak"));
                numberText.setText(res.getString("no_kontak"));
                alamatText.setText(res.getString("alamat"));
                statusComboBox.setSelectedItem(res.getString("status"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
}
