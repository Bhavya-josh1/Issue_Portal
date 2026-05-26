package project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPanel implements ActionListener
{

    JFrame f;
    JButton refreshBtn, logoutBtn, updateBtn;
    JTextField searchField;
    JButton searchBtn;
    JComboBox statusBox;
    DefaultTableModel tableModel;
    JTable table;
    JLabel totalLbl, pendingLbl, progressLbl, resolvedLbl;

    AdminPanel()
    {
        f = new JFrame("Admin Panel - Issue Reporter");
        f.setSize(800, 640);
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
                g2.setColor(new Color(251, 191, 36, 12));
                g2.fillOval(600, -40, 250, 250);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 800, 640);
        f.setContentPane(bg);

        // top navbar
        JPanel topBar = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(UITheme.WARNING);
                g2.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
                g2.dispose();
            }
        };
        topBar.setOpaque(false);
        topBar.setBounds(0, 0, 800, 55);

        JLabel appName = UITheme.createLabel("Issue Reporter", UITheme.ACCENT, UITheme.FONT_BOLD);
        appName.setBounds(18, 17, 180, 22);

        JLabel adminBadge = UITheme.createLabel("ADMIN", UITheme.WARNING, UITheme.FONT_BADGE);
        adminBadge.setBounds(175, 19, 55, 18);

        logoutBtn = UITheme.createPrimaryButton("Logout", UITheme.DANGER);
        logoutBtn.setBounds(695, 13, 88, 28);
        logoutBtn.setFont(UITheme.FONT_SMALL);

        topBar.add(appName);
        topBar.add(adminBadge);
        topBar.add(logoutBtn);

        // stats panel
        JPanel statsPanel = buildStatsPanel();
        statsPanel.setBounds(20, 65, 755, 70);

        JLabel pageTitle = UITheme.createLabel("Manage All Complaints",
                UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        pageTitle.setBounds(20, 148, 350, 30);

        // search bar
        JLabel searchLabel = UITheme.createLabel("Search:", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        searchLabel.setBounds(20, 188, 60, 26);

        searchField = UITheme.createTextField();
        searchField.setBounds(80, 186, 280, 30);

        searchBtn = UITheme.createPrimaryButton("Search", UITheme.ACCENT);
        searchBtn.setBounds(370, 186, 85, 30);
        searchBtn.setFont(UITheme.FONT_SMALL);

        JButton clearBtn = UITheme.createPrimaryButton("Clear", new Color(71, 85, 105));
        clearBtn.setBounds(465, 186, 65, 30);
        clearBtn.setFont(UITheme.FONT_SMALL);
        clearBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                searchField.setText("");
                loadComplaints("");
            }
        });

        // table
        String[] columns = {"ID", "Title", "Zone", "Description", "Submitted By", "Status"};
        tableModel = new DefaultTableModel(columns, 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };

        table = new JTable(tableModel)
        {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col)
            {
                Component c = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row))
                {
                    c.setBackground(row % 2 == 0 ? UITheme.BG_CARD : new Color(37, 51, 71));
                }
                else
                {
                    c.setBackground(new Color(251, 191, 36, 60));
                }
                c.setForeground(UITheme.TEXT_PRIMARY);
                return c;
            }
        };

        table.setFont(UITheme.FONT_LABEL);
        table.setRowHeight(36);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(UITheme.BG_CARD);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setSelectionBackground(new Color(251, 191, 36, 60));
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UITheme.BG_INPUT);
        header.setForeground(UITheme.WARNING);
        header.setFont(UITheme.FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.WARNING));

        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(210);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);

        // left-align all except status
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        for (int i = 0; i < 5; i++)
        {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        // color badge renderer for status column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer()
        {
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean selected, boolean focused, int row, int col)
            {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setFont(UITheme.FONT_BADGE);
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setForeground(UITheme.getStatusColor(value == null ? "" : value.toString()));
                if (selected)
                {
                    lbl.setBackground(new Color(251, 191, 36, 60));
                }
                else
                {
                    lbl.setBackground(row % 2 == 0 ? UITheme.BG_CARD : new Color(37, 51, 71));
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 224, 755, 300);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);

        // update status row at the bottom
        JLabel selectLabel = UITheme.createLabel("Select a row, then change status:",
                UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        selectLabel.setBounds(20, 537, 250, 22);

        String[] statuses = {"Pending", "In Progress", "Resolved"};
        statusBox = new JComboBox(statuses);
        statusBox.setBackground(UITheme.BG_INPUT);
        statusBox.setForeground(UITheme.TEXT_PRIMARY);
        statusBox.setFont(UITheme.FONT_LABEL);
        statusBox.setBounds(275, 535, 140, 28);

        updateBtn = UITheme.createPrimaryButton("Update Status", UITheme.WARNING);
        updateBtn.setBounds(425, 534, 150, 30);
        updateBtn.setFont(UITheme.FONT_SMALL);
        updateBtn.setForeground(new Color(30, 41, 59));

        refreshBtn = UITheme.createPrimaryButton("Refresh", UITheme.ACCENT);
        refreshBtn.setBounds(590, 534, 95, 30);
        refreshBtn.setFont(UITheme.FONT_SMALL);

        searchBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        refreshBtn.addActionListener(this);
        logoutBtn.addActionListener(this);

        bg.add(topBar);
        bg.add(statsPanel);
        bg.add(pageTitle);
        bg.add(searchLabel);
        bg.add(searchField);
        bg.add(searchBtn);
        bg.add(clearBtn);
        bg.add(scroll);
        bg.add(selectLabel);
        bg.add(statusBox);
        bg.add(updateBtn);
        bg.add(refreshBtn);

        f.setVisible(true);
        loadComplaints("");
    }

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
        Color[]  colors = {UITheme.ACCENT, UITheme.WARNING, UITheme.TEXT_PRIMARY, UITheme.SUCCESS};

        int boxW = 755 / 4;
        for (int i = 0; i < 4; i++)
        {
            int bx = i * boxW;

            JLabel num = UITheme.createLabel(String.valueOf(counts[i]), colors[i],
                    new Font("Segoe UI", Font.BOLD, 22));
            num.setBounds(bx + 10, 8, boxW - 20, 28);
            num.setHorizontalAlignment(SwingConstants.CENTER);

            JLabel lbl = UITheme.createLabel(labels[i], UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
            lbl.setBounds(bx + 10, 36, boxW - 20, 18);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(num);
            panel.add(lbl);
        }
        return panel;
    }

    void loadComplaints(String keyword)
    {
        tableModel.setRowCount(0);
        try {
            Connection conn = Database.connect();
            PreparedStatement ps;
            String search = "%" + keyword + "%";

            if (keyword.equals(""))
            {
                ps = conn.prepareStatement("SELECT * FROM complaints ORDER BY id DESC");
            }
            else
            {
                ps = conn.prepareStatement(
                        "SELECT * FROM complaints WHERE title LIKE ? OR zone LIKE ?" +
                                " OR description LIKE ? OR submitted_by LIKE ? ORDER BY id DESC");
                ps.setString(1, search);
                ps.setString(2, search);
                ps.setString(3, search);
                ps.setString(4, search);
            }

            ResultSet rs = ps.executeQuery();
            int count = 0;
            while (rs.next())
            {
                String status = rs.getString("status");
                if (status == null) status = "Pending";
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("zone"),
                        rs.getString("description"),
                        rs.getString("submitted_by"),
                        status
                });
                count++;
            }
            conn.close();

            if (count == 0)
            {
                tableModel.addRow(new Object[]{"", "No complaints found", "", "", "", ""});
            }
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == refreshBtn)
        {
            searchField.setText("");
            loadComplaints("");
        }

        if (e.getSource() == searchBtn)
        {
            loadComplaints(searchField.getText().trim());
        }

        if (e.getSource() == updateBtn)
        {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1)
            {
                JOptionPane.showMessageDialog(f, "Please select a complaint row first.");
                return;
            }

            Object idObj = tableModel.getValueAt(selectedRow, 0);
            if (idObj == null || idObj.toString().equals(""))
            {
                JOptionPane.showMessageDialog(f, "Please select a valid complaint.");
                return;
            }

            int id = Integer.parseInt(idObj.toString());
            String newStatus = statusBox.getSelectedItem().toString();

            boolean ok = Database.updateStatus(id, newStatus);
            if (ok)
            {
                JOptionPane.showMessageDialog(f, "Status updated to: " + newStatus);
                loadComplaints(searchField.getText().trim());
            }
            else
            {
                JOptionPane.showMessageDialog(f, "Update failed. Please try again.");
            }
        }

        if (e.getSource() == logoutBtn)
        {
            f.dispose();
            new Login();
        }
    }
}