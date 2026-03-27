package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login implements ActionListener {

    JFrame f;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn, registerBtn;

    Login() {
        f = new JFrame("Issue Reporter - Login");
        UITheme.styleFrame(f, 400, 510);

        // Title at top
        JLabel appTitle = new JLabel("Issue Reporter", SwingConstants.CENTER);
        appTitle.setBounds(0, 30, 400, 35);
        appTitle.setForeground(UITheme.BLUE);
        appTitle.setFont(UITheme.FONT_TITLE);

        JLabel appSub = new JLabel("Community Complaint System", SwingConstants.CENTER);
        appSub.setBounds(0, 65, 400, 20);
        appSub.setForeground(UITheme.GRAY);
        appSub.setFont(UITheme.FONT_SMALL);

        // Card panel
        JPanel card = UITheme.createCard(40, 105, 320, 290);

        JLabel cardTitle = new JLabel("Login to your account");
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

        loginBtn = new JButton("Login");
        loginBtn.setBounds(20, 210, 280, 38);
        UITheme.styleButton(loginBtn, UITheme.BLUE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(cardTitle);
        card.add(line);
        card.add(userLabel);
        card.add(userField);
        card.add(passLabel);
        card.add(passField);
        card.add(loginBtn);

        // Register section below card
        JLabel orLabel = new JLabel("New user?", SwingConstants.CENTER);
        orLabel.setBounds(0, 430, 400, 20);
        UITheme.styleLabel(orLabel, UITheme.GRAY);

        registerBtn = new JButton("Create an Account");
        registerBtn.setBounds(110, 455, 180, 34);
        UITheme.styleButton(registerBtn, UITheme.GREEN);

        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);

        f.add(appTitle);
        f.add(appSub);
        f.add(card);
        f.add(orLabel);
        f.add(registerBtn);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("") || password.equals("")) {
                JOptionPane.showMessageDialog(f, "Please fill in all fields.");
                return;
            }

            try {
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM users WHERE username=? AND password=?");
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(f, "Login successful!");
                    f.dispose();
                    new Complaint(username);
                } else {
                    JOptionPane.showMessageDialog(f, "Wrong username or password.");
                }
                conn.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        if (e.getSource() == registerBtn) {
            new Register();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}