package project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewComplaint implements ActionListener {

    JFrame f;
    JButton closeBtn, refreshBtn;
    DefaultTableModel tableModel;

    ViewComplaint() {
        f = new JFrame("Issue Reporter - View Complaints");
        UITheme.styleFrame(f, 580, 520);

        // Top bar
        JPanel topBar = new JPanel(null);
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBounds(0, 0, 580, 55);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

        JLabel appName = new JLabel("Issue Reporter");
        appName.setBounds(15, 15, 200, 24);
        appName.setForeground(UITheme.BLUE);
        appName.setFont(UITheme.FONT_BOLD);
        topBar.add(appName);

        // Page heading
        JLabel pageTitle = new JLabel("All Complaints");
        pageTitle.setBounds(20, 70, 300, 28);
        pageTitle.setForeground(UITheme.WHITE);
        pageTitle.setFont(UITheme.FONT_TITLE);

        JLabel subLabel = new JLabel("Showing all submitted complaints");
        subLabel.setBounds(20, 100, 300, 18);
        UITheme.styleLabel(subLabel, UITheme.GRAY);
        subLabel.setFont(UITheme.FONT_SMALL);

        // Table
        String[] columns = {"#", "Title", "Zone", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.WHITE);
        table.setFont(UITheme.FONT_LABEL);
        table.setRowHeight(32);
        table.setGridColor(UITheme.BORDER);
        table.setSelectionBackground(UITheme.BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(false);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(230);

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(UITheme.BG_INPUT);
        header.setForeground(UITheme.BLUE);
        header.setFont(UITheme.FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 36));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 128, 535, 300);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(100, 445, 130, 36);
        UITheme.styleButton(refreshBtn, UITheme.BLUE);

        closeBtn = new JButton("Close");
        closeBtn.setBounds(310, 445, 130, 36);
        UITheme.styleButton(closeBtn, UITheme.RED);

        refreshBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        f.add(topBar);
        f.add(pageTitle);
        f.add(subLabel);
        f.add(scroll);
        f.add(refreshBtn);
        f.add(closeBtn);

        f.setVisible(true);

        loadComplaints();
    }

    public void loadComplaints() {
        tableModel.setRowCount(0);
        try {
            Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM complaints");

            int count = 1;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        count,
                        rs.getString("title"),
                        rs.getString("zone"),
                        rs.getString("description")
                });
                count++;
            }
            conn.close();

            if (count == 1) {
                // no rows added
                tableModel.addRow(new Object[]{"", "No complaints found", "", ""});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == refreshBtn) {
            loadComplaints();
        }
        if (e.getSource() == closeBtn) {
            f.dispose();
        }
    }
}