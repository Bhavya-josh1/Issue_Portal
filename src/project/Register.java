package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register implements ActionListener
{

    JFrame f;
    JTextField userField;
    JPasswordField passField;
    JButton registerBtn, backBtn;

    Register()
    {
        f = new JFrame("Register");
        f.setSize(420, 480);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel bg = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, UITheme.BG_DARK,
                        getWidth(), getHeight(), new Color(17, 24, 39));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(74, 222, 128, 20));
                g2.fillOval(280, -40, 200, 200);
                g2.setColor(new Color(99, 179, 237, 15));
                g2.fillOval(-50, 330, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 420, 480);
        f.setContentPane(bg);

        JLabel title = UITheme.createLabel("Create Account", UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        title.setBounds(105, 35, 240, 35);

        JLabel subtitle = UITheme.createLabel("Register to report issues in your area",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        subtitle.setBounds(75, 72, 280, 20);

        JPanel card = UITheme.createCard(30, 108, 360, 270);

        JLabel cardTitle = UITheme.createLabel("Account Details",
                UITheme.TEXT_PRIMARY, UITheme.FONT_BOLD);
        cardTitle.setBounds(25, 22, 300, 22);

        card.add(UITheme.createDivider(25, 50, 310));

        JLabel userLabel = UITheme.createLabel("Username", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        userLabel.setBounds(25, 62, 120, 20);

        userField = UITheme.createTextField();
        userField.setBounds(25, 85, 310, 38);

        JLabel passLabel = UITheme.createLabel("Password", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        passLabel.setBounds(25, 135, 120, 20);

        passField = UITheme.createPasswordField();
        passField.setBounds(25, 158, 310, 38);

        registerBtn = UITheme.createPrimaryButton("Register", UITheme.SUCCESS);
        registerBtn.setBounds(25, 210, 310, 42);
        registerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(cardTitle);
        card.add(userLabel);
        card.add(userField);
        card.add(passLabel);
        card.add(passField);
        card.add(registerBtn);

        JLabel alreadyLabel = UITheme.createLabel("Already have an account?",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        alreadyLabel.setBounds(90, 398, 180, 20);

        backBtn = UITheme.createPrimaryButton("Go to Login", UITheme.ACCENT);
        backBtn.setBounds(105, 420, 210, 36);

        registerBtn.addActionListener(this);
        backBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                f.dispose();
                new Login();
            }
        });

        bg.add(title);
        bg.add(subtitle);
        bg.add(card);
        bg.add(alreadyLabel);
        bg.add(backBtn);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (username.equals("") || password.equals(""))
        {
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
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users VALUES(?,?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();
            conn.close();
            JOptionPane.showMessageDialog(f, "Account created! You can now login.");
            f.dispose();
        } catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
