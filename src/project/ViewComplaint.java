package project;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewComplaint implements ActionListener
{

    JFrame f;
    JButton closeBtn, refreshBtn;
    JTextField searchField;
    JButton searchBtn;
    DefaultTableModel tableModel;
    String username;
    boolean isAdmin;

    ViewComplaint(String username, boolean isAdmin)
    {
        this.username = username;
        this.isAdmin  = isAdmin;

        String windowTitle = isAdmin ? "All Complaints (Admin)" : "My Complaints - " + username;
        f = new JFrame(windowTitle);
        f.setSize(750, 560);
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
                g2.setColor(new Color(248, 113, 113, 15));
                g2.fillOval(500, -30, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 750, 560);
        f.setContentPane(bg);

        // top bar
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
        topBar.setBounds(0, 0, 750, 55);

        JLabel appName = UITheme.createLabel("Issue Reporter", UITheme.ACCENT, UITheme.FONT_BOLD);
        appName.setBounds(18, 17, 180, 22);
        topBar.add(appName);

        String pageHeading = isAdmin ? "All Complaints" : "My Complaints";
        JLabel pageTitle = UITheme.createLabel(pageHeading,
                UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        pageTitle.setBounds(20, 65, 300, 30);

        String subText = isAdmin
                ? "Showing all complaints from all users"
                : "Showing complaints submitted by: " + username;
        JLabel subLabel = UITheme.createLabel(subText, UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        subLabel.setBounds(20, 97, 500, 18);

        // search bar
        JLabel searchLabel = UITheme.createLabel("Search:", UITheme.TEXT_MUTED, UITheme.FONT_LABEL);
        searchLabel.setBounds(20, 125, 60, 26);

        searchField = UITheme.createTextField();
        searchField.setBounds(80, 123, 280, 30);

        searchBtn = UITheme.createPrimaryButton("Search", UITheme.ACCENT);
        searchBtn.setBounds(370, 123, 90, 30);
        searchBtn.setFont(UITheme.FONT_SMALL);

        JButton clearBtn = UITheme.createPrimaryButton("Clear", new Color(71, 85, 105));
        clearBtn.setBounds(470, 123, 70, 30);
        clearBtn.setFont(UITheme.FONT_SMALL);
        clearBtn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                searchField.setText("");
                loadComplaints("");
            }
        });

        // table columns: admin sees Submitted By, user does not
        String[] columns;
        if (isAdmin)
        {
            columns = new String[]{"#", "Title", "Zone", "Description", "Submitted By", "Status"};
        }
        else
        {
            columns = new String[]{"#", "Title", "Zone", "Description", "Status"};
        }

        tableModel = new DefaultTableModel(columns, 0)
        {
            public boolean isCellEditable(int row, int col)
            {
                return false;
            }
        };

        JTable table = new JTable(tableModel)
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
                    c.setBackground(new Color(99, 179, 237, 80));
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
        table.setSelectionBackground(new Color(99, 179, 237, 80));
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(UITheme.BG_INPUT);
        header.setForeground(UITheme.ACCENT);
        header.setFont(UITheme.FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 38));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, UITheme.BORDER));

        // column widths
        if (isAdmin)
        {
            table.getColumnModel().getColumn(0).setPreferredWidth(30);
            table.getColumnModel().getColumn(1).setPreferredWidth(140);
            table.getColumnModel().getColumn(2).setPreferredWidth(90);
            table.getColumnModel().getColumn(3).setPreferredWidth(200);
            table.getColumnModel().getColumn(4).setPreferredWidth(100);
            table.getColumnModel().getColumn(5).setPreferredWidth(90);
        }
        else
        {
            table.getColumnModel().getColumn(0).setPreferredWidth(35);
            table.getColumnModel().getColumn(1).setPreferredWidth(160);
            table.getColumnModel().getColumn(2).setPreferredWidth(110);
            table.getColumnModel().getColumn(3).setPreferredWidth(250);
            table.getColumnModel().getColumn(4).setPreferredWidth(90);
        }

        // left-align renderer for all columns except status
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        int totalCols = isAdmin ? 6 : 5;
        int statusCol = totalCols - 1;

        for (int i = 0; i < statusCol; i++)
        {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        // color-coded badge renderer for status column
        table.getColumnModel().getColumn(statusCol).setCellRenderer(new DefaultTableCellRenderer()
        {
            public Component getTableCellRendererComponent(JTable t, Object value,
                                                           boolean selected, boolean focused, int row, int col)
            {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setFont(UITheme.FONT_BADGE);
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                Color statusColor = UITheme.getStatusColor(value == null ? "" : value.toString());
                lbl.setForeground(statusColor);

                if (selected)
                {
                    lbl.setBackground(new Color(99, 179, 237, 80));
                }
                else
                {
                    lbl.setBackground(row % 2 == 0 ? UITheme.BG_CARD : new Color(37, 51, 71));
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 163, 705, 310);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);

        refreshBtn = UITheme.createPrimaryButton("Refresh", UITheme.ACCENT);
        refreshBtn.setBounds(150, 488, 140, 38);

        closeBtn = UITheme.createPrimaryButton("Close", UITheme.DANGER);
        closeBtn.setBounds(450, 488, 140, 38);

        refreshBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        searchBtn.addActionListener(this);

        bg.add(topBar);
        bg.add(pageTitle);
        bg.add(subLabel);
        bg.add(searchLabel);
        bg.add(searchField);
        bg.add(searchBtn);
        bg.add(clearBtn);
        bg.add(scroll);
        bg.add(refreshBtn);
        bg.add(closeBtn);

        f.setVisible(true);
        loadComplaints("");
    }

    public void loadComplaints(String keyword)
    {
        tableModel.setRowCount(0);
        try {
            Connection conn = Database.connect();
            PreparedStatement ps;

            String search = "%" + keyword + "%";

            if (isAdmin)
            {
                // admin sees everything
                if (keyword.equals(""))
                {
                    ps = conn.prepareStatement("SELECT * FROM complaints ORDER BY id DESC");
                }
                else
                {
                    ps = conn.prepareStatement(
                            "SELECT * FROM complaints WHERE title LIKE ? OR zone LIKE ?" +
                                    " OR description LIKE ? ORDER BY id DESC");
                    ps.setString(1, search);
                    ps.setString(2, search);
                    ps.setString(3, search);
                }
            }
            else
            {
                // user sees only their own
                if (keyword.equals(""))
                {
                    ps = conn.prepareStatement(
                            "SELECT * FROM complaints WHERE submitted_by=? ORDER BY id DESC");
                    ps.setString(1, username);
                }
                else
                {
                    ps = conn.prepareStatement(
                            "SELECT * FROM complaints WHERE submitted_by=?" +
                                    " AND (title LIKE ? OR zone LIKE ? OR description LIKE ?)" +
                                    " ORDER BY id DESC");
                    ps.setString(1, username);
                    ps.setString(2, search);
                    ps.setString(3, search);
                    ps.setString(4, search);
                }
            }

            ResultSet rs = ps.executeQuery();
            int count = 1;
            while (rs.next())
            {
                String status = rs.getString("status");
                if (status == null) status = "Pending";

                if (isAdmin)
                {
                    tableModel.addRow(new Object[]{
                            count,
                            rs.getString("title"),
                            rs.getString("zone"),
                            rs.getString("description"),
                            rs.getString("submitted_by"),
                            status
                    });
                }
                else
                {
                    tableModel.addRow(new Object[]{
                            count,
                            rs.getString("title"),
                            rs.getString("zone"),
                            rs.getString("description"),
                            status
                    });
                }
                count++;
            }
            conn.close();

            if (count == 1)
            {
                tableModel.addRow(new Object[]{"", "No complaints found", "", "", ""});
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
        if (e.getSource() == closeBtn)
        {
            f.dispose();
        }
    }
}