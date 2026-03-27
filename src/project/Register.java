package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register implements ActionListener {

    JFrame f;
    JTextField userField;
    JPasswordField passField;
    JButton registerBtn;

    Register() {
        f = new JFrame("Issue Reporter - Register");
        UITheme.styleFrame(f, 400, 420);

        JLabel appTitle = new JLabel("Create Account", SwingConstants.CENTER);
        appTitle.setBounds(0, 30, 400, 35);
        appTitle.setForeground(UITheme.GREEN);
        appTitle.setFont(UITheme.FONT_TITLE);

        JLabel appSub = new JLabel("Register to report issues in your area", SwingConstants.CENTER);
        appSub.setBounds(0, 65, 400, 20);
        appSub.setForeground(UITheme.GRAY);
        appSub.setFont(UITheme.FONT_SMALL);

        // Card
        JPanel card = UITheme.createCard(40, 105, 320, 245);

        JLabel cardTitle = new JLabel("Account Details");
        cardTitle.setBounds(20, 18, 280, 22);
        cardTitle.setForeground(UITheme.WHITE);
        cardTitle.setFont(UITheme.FONT_BOLD);

        JSeparator line = new JSeparator();
        line.setBounds(20, 44, 280, 2);
        line.setForeground(UITheme.BORDER);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(20, 58, 120, 20);
        UITheme.styleLabel(userLabel, UITheme.GRAY);

        userField = new JTextField();
        userField.setBounds(20, 80, 280, 32);
        UITheme.styleField(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(20, 126, 120, 20);
        UITheme.styleLabel(passLabel, UITheme.GRAY);

        passField = new JPasswordField();
        passField.setBounds(20, 148, 280, 32);
        UITheme.styleField(passField);

        registerBtn = new JButton("Register");
        registerBtn.setBounds(20, 195, 280, 38);
        UITheme.styleButton(registerBtn, UITheme.GREEN);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(cardTitle);
        card.add(line);
        card.add(userLabel);
        card.add(userField);
        card.add(passLabel);
        card.add(passField);
        card.add(registerBtn);

        JLabel backLabel = new JLabel("Already have an account?", SwingConstants.CENTER);
        backLabel.setBounds(0, 365, 400, 20);
        UITheme.styleLabel(backLabel, UITheme.GRAY);

        JButton backBtn = new JButton("Go to Login");
        backBtn.setBounds(130, 390, 140, 32);
        UITheme.styleButton(backBtn, UITheme.BLUE);

        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                new Login();
            }
        });

        registerBtn.addActionListener(this);

        f.add(appTitle);
        f.add(appSub);
        f.add(card);
        f.add(backLabel);
        f.add(backBtn);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(f, "Please fill in all fields.");
            return;
        }

        try {
            Connection conn = Database.connect();
            PreparedStatement check = conn.prepareStatement(
                    "SELECT * FROM users WHERE username=?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(f, "Username already taken. Try another.");
                conn.close();
                return;
            }

            PreparedStatement ps = conn.prepareStatement("INSERT INTO users VALUES(?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(f, "Account created! You can now login.");
            f.dispose();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}