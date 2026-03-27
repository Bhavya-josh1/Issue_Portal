package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Complaint implements ActionListener {

    JFrame f;
    JTextField titleField, zoneField;
    JTextArea descArea;
    JButton submitBtn, viewBtn, logoutBtn;
    String username;

    Complaint(String username) {
        this.username = username;

        f = new JFrame("Issue Reporter - Submit Complaint");
        UITheme.styleFrame(f, 460, 530);

        // Top bar
        JPanel topBar = new JPanel(null);
        topBar.setBackground(UITheme.BG_CARD);
        topBar.setBounds(0, 0, 460, 55);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER));

        JLabel appName = new JLabel("Issue Reporter");
        appName.setBounds(15, 15, 180, 24);
        appName.setForeground(UITheme.BLUE);
        appName.setFont(UITheme.FONT_BOLD);

        JLabel userLabel = new JLabel("Logged in: " + username);
        userLabel.setBounds(200, 18, 160, 18);
        UITheme.styleLabel(userLabel, UITheme.GRAY);
        userLabel.setFont(UITheme.FONT_SMALL);

        logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(370, 13, 75, 28);
        UITheme.styleButton(logoutBtn, UITheme.RED);
        logoutBtn.setFont(UITheme.FONT_SMALL);

        topBar.add(appName);
        topBar.add(userLabel);
        topBar.add(logoutBtn);

        // Page heading
        JLabel pageTitle = new JLabel("Submit a Complaint");
        pageTitle.setBounds(20, 70, 300, 28);
        pageTitle.setForeground(UITheme.WHITE);
        pageTitle.setFont(UITheme.FONT_TITLE);

        // Form card
        JPanel card = UITheme.createCard(20, 110, 415, 340);

        JLabel titleLabel = new JLabel("Issue Title");
        titleLabel.setBounds(20, 18, 120, 20);
        UITheme.styleLabel(titleLabel, UITheme.GRAY);

        titleField = new JTextField();
        titleField.setBounds(20, 40, 375, 32);
        UITheme.styleField(titleField);

        JLabel zoneLabel = new JLabel("Zone / Area");
        zoneLabel.setBounds(20, 86, 120, 20);
        UITheme.styleLabel(zoneLabel, UITheme.GRAY);

        zoneField = new JTextField();
        zoneField.setBounds(20, 108, 375, 32);
        UITheme.styleField(zoneField);

        JLabel descLabel = new JLabel("Description");
        descLabel.setBounds(20, 154, 120, 20);
        UITheme.styleLabel(descLabel, UITheme.GRAY);

        descArea = new JTextArea();
        descArea.setBackground(UITheme.BG_INPUT);
        descArea.setForeground(UITheme.WHITE);
        descArea.setCaretColor(UITheme.WHITE);
        descArea.setFont(UITheme.FONT_INPUT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(20, 176, 375, 100);
        descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));

        submitBtn = new JButton("Submit Complaint");
        submitBtn.setBounds(20, 292, 180, 36);
        UITheme.styleButton(submitBtn, UITheme.BLUE);

        viewBtn = new JButton("View All Complaints");
        viewBtn.setBounds(215, 292, 180, 36);
        UITheme.styleButton(viewBtn, UITheme.GREEN);

        card.add(titleLabel);
        card.add(titleField);
        card.add(zoneLabel);
        card.add(zoneField);
        card.add(descLabel);
        card.add(descScroll);
        card.add(submitBtn);
        card.add(viewBtn);

        submitBtn.addActionListener(this);
        viewBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        f.add(topBar);
        f.add(pageTitle);
        f.add(card);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            String title = titleField.getText();
            String zone = zoneField.getText();
            String desc = descArea.getText();

            if (title.equals("") || zone.equals("") || desc.equals("")) {
                JOptionPane.showMessageDialog(f, "Please fill in all fields.");
                return;
            }

            try {
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO complaints(title, description, zone) VALUES(?,?,?)");
                ps.setString(1, title);
                ps.setString(2, desc);
                ps.setString(3, zone);
                ps.executeUpdate();
                conn.close();

                JOptionPane.showMessageDialog(f, "Complaint submitted successfully!");
                titleField.setText("");
                zoneField.setText("");
                descArea.setText("");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (e.getSource() == viewBtn) {
            new ViewComplaint();
        }

        if (e.getSource() == logoutBtn) {
            f.dispose();
            new Login();
        }
    }
}