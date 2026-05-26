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
        f.setSize(480, 640);
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
                        getWidth(), getHeight(), new Color(15, 23, 42));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(99, 179, 237, 15));
                g2.fillOval(-40, 380, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 480, 640);
        f.setContentPane(bg);

        // top navbar
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

        // stats bar panel
        JPanel statsPanel = buildStatsPanel();
        statsPanel.setBounds(20, 65, 438, 70);

        JLabel pageTitle = UITheme.createLabel("Submit a Complaint",
                UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        pageTitle.setBounds(20, 145, 300, 30);

        // form card
        JPanel card = UITheme.createCard(20, 185, 438, 390);

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

        viewBtn = UITheme.createPrimaryButton("View My Complaints", new Color(71, 85, 105));
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
        bg.add(statsPanel);
        bg.add(pageTitle);
        bg.add(card);

        f.setVisible(true);
    }

    // builds the 4-box stats panel
    JPanel buildStatsPanel()
    {
        JPanel panel = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UITheme.BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(UITheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
            }
        };
        panel.setOpaque(false);

        int[] counts = {
                Database.getCount("Total"),
                Database.getCount("Pending"),
                Database.getCount("In Progress"),
                Database.getCount("Resolved")
        };
        String[] labels = {"Total", "Pending", "In Progress", "Resolved"};
        Color[] colors  = {UITheme.ACCENT, UITheme.WARNING, UITheme.TEXT_PRIMARY, UITheme.SUCCESS};

        int boxW = 438 / 4;
        for (int i = 0; i < 4; i++)
        {
            int bx = i * boxW;

            JLabel num = UITheme.createLabel(String.valueOf(counts[i]), colors[i],
                    new Font("Segoe UI", Font.BOLD, 22));
            num.setBounds(bx + 10, 8, boxW - 20, 28);
            num.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lbl = UITheme.createLabel(labels[i], UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
            lbl.setBounds(bx + 10, 36, boxW - 20, 16);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(num);
            panel.add(lbl);
        }
        return panel;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == submitBtn)
        {
            String title = titleField.getText();
            String zone  = zoneField.getText();
            String desc  = descArea.getText();

            if (title.equals("") || zone.equals("") || desc.equals(""))
            {
                JOptionPane.showMessageDialog(f, "Please fill in all fields.");
                return;
            }
            try {
                Connection conn = Database.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO complaints(title, description, zone, status, submitted_by)" +
                                " VALUES(?,?,?,'Pending',?)");
                ps.setString(1, title);
                ps.setString(2, desc);
                ps.setString(3, zone);
                ps.setString(4, username);
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
            new ViewComplaint(username, false);
        }

        if (e.getSource() == logoutBtn)
        {
            f.dispose();
            new Login();
        }
    }
}