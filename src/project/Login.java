package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login implements ActionListener
{

    JFrame f;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn, registerBtn;

    Login()
    {
        f = new JFrame("Login");
        f.setSize(420, 600);
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
                g2.setColor(new Color(99, 179, 237, 25));
                g2.fillOval(-60, -60, 200, 200);
                g2.setColor(new Color(56, 189, 248, 15));
                g2.fillOval(250, 300, 220, 220);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 420, 600);
        f.setContentPane(bg);

        JLabel title = UITheme.createLabel("Issue Reporter", UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        title.setBounds(110, 40, 220, 35);

        JLabel subtitle = UITheme.createLabel("Community Complaint System",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        subtitle.setBounds(105, 76, 220, 20);

        JPanel card = UITheme.createCard(30, 115, 360, 310);

        JLabel cardTitle = UITheme.createLabel("Login to your account",
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

        loginBtn = UITheme.createPrimaryButton("Login", UITheme.ACCENT);
        loginBtn.setBounds(25, 218, 310, 42);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        card.add(cardTitle);
        card.add(userLabel);
        card.add(userField);
        card.add(passLabel);
        card.add(passField);
        card.add(loginBtn);

        JLabel newUser = UITheme.createLabel("New user?", UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        newUser.setBounds(120, 500, 70, 20);

        registerBtn = UITheme.createPrimaryButton("Create Account", UITheme.SUCCESS);
        registerBtn.setBounds(105, 522, 210, 36);

        loginBtn.addActionListener(this);
        registerBtn.addActionListener(this);

        bg.add(title);
        bg.add(subtitle);
        bg.add(card);
        bg.add(newUser);
        bg.add(registerBtn);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == loginBtn)
        {
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
                } else
                {
                    JOptionPane.showMessageDialog(f, "Wrong username or password.");
                }
                conn.close();
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        if (e.getSource() == registerBtn)
        {
            new Register();
        }
    }

    public static void main(String[] args)
    {
        new Login();
    }
}