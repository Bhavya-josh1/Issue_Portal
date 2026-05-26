package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminLogin implements ActionListener
{

    JFrame f;
    JTextField userField;
    JPasswordField passField;
    JButton loginBtn, backBtn;

    AdminLogin()
    {
        f = new JFrame("Admin Login");
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
                // amber glow instead of blue to signal admin mode
                g2.setColor(new Color(251, 191, 36, 20));
                g2.fillOval(-40, -40, 200, 200);
                g2.setColor(new Color(251, 191, 36, 10));
                g2.fillOval(270, 320, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 420, 480);
        f.setContentPane(bg);

        JLabel title = UITheme.createLabel("Admin Login", UITheme.WARNING, UITheme.FONT_TITLE);
        title.setBounds(130, 40, 200, 35);

        JLabel subtitle = UITheme.createLabel("Restricted access - admins only",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        subtitle.setBounds(90, 76, 260, 20);

        // card with amber border to distinguish from user login
        JPanel card = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(UITheme.WARNING);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(30, 115, 360, 295);

        JLabel cardTitle = UITheme.createLabel("Enter admin credentials",
                UITheme.TEXT_PRIMARY, UITheme.FONT_BOLD);
        cardTitle.setBounds(25, 22, 300, 22);

        card.add(UITheme.createDivider(25, 50, 310));

        JLabel userLabel = UITheme.createLabel("Admin Username", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        userLabel.setBounds(25, 62, 150, 20);

        userField = UITheme.createTextField();
        userField.setBounds(25, 85, 310, 38);

        JLabel passLabel = UITheme.createLabel("Admin Password", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        passLabel.setBounds(25, 135, 150, 20);

        passField = UITheme.createPasswordField();
        passField.setBounds(25, 158, 310, 38);

        loginBtn = UITheme.createPrimaryButton("Login as Admin", UITheme.WARNING);
        loginBtn.setBounds(25, 218, 310, 42);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // make warning button text dark so it's readable on amber
        loginBtn.setForeground(new Color(30, 41, 59));

        card.add(cardTitle);
        card.add(userLabel);
        card.add(userField);
        card.add(passLabel);
        card.add(passField);
        card.add(loginBtn);

        JLabel hintLabel = UITheme.createLabel("Default: admin / admin123",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        hintLabel.setBounds(125, 420, 200, 18);

        backBtn = UITheme.createPrimaryButton("Back to User Login", UITheme.ACCENT);
        backBtn.setBounds(105, 440, 210, 30);
        backBtn.setFont(UITheme.FONT_SMALL);

        loginBtn.addActionListener(this);
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
        bg.add(hintLabel);
        bg.add(backBtn);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == loginBtn)
        {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("") || pass.equals(""))
            {
                JOptionPane.showMessageDialog(f, "Please fill in all fields.");
                return;
            }

            if (Database.validateAdmin(user, pass))
            {
                JOptionPane.showMessageDialog(f, "Admin login successful!");
                f.dispose();
                new AdminPanel();
            }
            else
            {
                JOptionPane.showMessageDialog(f, "Invalid admin credentials.");
                passField.setText("");
            }
        }
    }
}