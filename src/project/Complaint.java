package project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Complaint implements ActionListener
{

    JFrame f;
    JTextField titleField, zoneField;
    JTextArea descArea;
    JButton submitBtn, viewBtn, logoutBtn;
    String username;

    Complaint(String username)
    {
        this.username = username;

        f = new JFrame("Submit Complaint");
        f.setSize(480, 560);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel bg = new JPanel(null) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, UITheme.BG_DARK,
                        getWidth(), getHeight(), new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(99, 179, 237, 15));
                g2.fillOval(-40, 380, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 480, 560);
        f.setContentPane(bg);

        JPanel topBar = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(UITheme.BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        topBar.setOpaque(false);
        topBar.setBounds(0, 0, 480, 55);

        JLabel appName = UITheme.createLabel("Issue Reporter", UITheme.ACCENT, UITheme.FONT_BOLD);
        appName.setBounds(18, 17, 180, 22);

        JLabel userLabel = UITheme.createLabel("Logged in: " + username,
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        userLabel.setBounds(200, 20, 180, 18);

        logoutBtn = UITheme.createPrimaryButton("Logout", UITheme.DANGER);
        logoutBtn.setBounds(385, 13, 80, 28);
        logoutBtn.setFont(UITheme.FONT_SMALL);

        topBar.add(appName);
        topBar.add(userLabel);
        topBar.add(logoutBtn);

        JLabel pageTitle = UITheme.createLabel("Submit a Complaint",
                UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        pageTitle.setBounds(20, 68, 300, 30);

        JPanel card = UITheme.createCard(20, 110, 438, 370);

        JLabel titleLabel = UITheme.createLabel("Issue Title", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        titleLabel.setBounds(22, 20, 120, 20);

        titleField = UITheme.createTextField();
        titleField.setBounds(22, 43, 394, 38);

        JLabel zoneLabel = UITheme.createLabel("Zone / Area", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        zoneLabel.setBounds(22, 95, 120, 20);

        zoneField = UITheme.createTextField();
        zoneField.setBounds(22, 118, 394, 38);

        JLabel descLabel = UITheme.createLabel("Description", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        descLabel.setBounds(22, 170, 120, 20);

        descArea = new JTextArea();
        descArea.setBackground(UITheme.BG_INPUT);
        descArea.setForeground(UITheme.TEXT_PRIMARY);
        descArea.setCaretColor(UITheme.ACCENT);
        descArea.setFont(UITheme.FONT_INPUT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBounds(22, 193, 394, 100);
        descScroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        descScroll.getViewport().setBackground(UITheme.BG_INPUT);

        submitBtn = UITheme.createPrimaryButton("Submit Complaint", UITheme.ACCENT);
        submitBtn.setBounds(22, 315, 190, 40);

        viewBtn = UITheme.createPrimaryButton("View All Complaints", new Color(71, 85, 105));
        viewBtn.setBounds(226, 315, 190, 40);

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

        bg.add(topBar);
        bg.add(pageTitle);
        bg.add(card);

        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == submitBtn)
        {
            String title = titleField.getText();
            String zone = zoneField.getText();
            String desc = descArea.getText();

            if (title.equals("") || zone.equals("") || desc.equals(""))
            {
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
            } catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        if (e.getSource() == viewBtn)
        {
            new ViewComplaint();
        }
        if (e.getSource() == logoutBtn)
        {
            f.dispose();
            new Login();
        }
    }
}
