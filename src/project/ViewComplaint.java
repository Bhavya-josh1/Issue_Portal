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
    DefaultTableModel tableModel;

    ViewComplaint()
    {
        f = new JFrame("View Complaints");
        f.setSize(600, 520);
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
                g2.fillOval(400, -30, 200, 200);
                g2.dispose();
            }
        };
        bg.setBounds(0, 0, 600, 520);
        f.setContentPane(bg);

        JPanel topBar = new JPanel(null)
        {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(UITheme.BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(UITheme.BORDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        topBar.setOpaque(false);
        topBar.setBounds(0, 0, 600, 55);

        JLabel appName = UITheme.createLabel("Issue Reporter", UITheme.ACCENT, UITheme.FONT_BOLD);
        appName.setBounds(18, 17, 180, 22);
        topBar.add(appName);

        JLabel pageTitle = UITheme.createLabel("All Complaints",
                UITheme.TEXT_PRIMARY, UITheme.FONT_TITLE);
        pageTitle.setBounds(20, 68, 300, 30);

        JLabel subLabel = UITheme.createLabel("Showing all submitted complaints",
                UITheme.TEXT_MUTED, UITheme.FONT_SMALL);
        subLabel.setBounds(20, 100, 300, 18);

        String[] columns = {"#", "Title", "Zone", "Description"};
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
                if (isRowSelected(row))
                {
                    c.setBackground(new Color(99, 179, 237, 80));
                }
                else
                {
                    c.setBackground(row % 2 == 0 ? UITheme.BG_CARD : new Color(37, 51, 71));
                }
                c.setForeground(UITheme.TEXT_PRIMARY);
                return c;
            }
        };

        table.setFont(UITheme.FONT_LABEL);
        table.setRowHeight(34);
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

        table.getColumnModel().getColumn(0).setPreferredWidth(35);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        for (int i = 0; i < 4; i++)
        {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 128, 555, 300);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);

        refreshBtn = UITheme.createPrimaryButton("Refresh", UITheme.ACCENT);
        refreshBtn.setBounds(100, 445, 140, 38);

        closeBtn = UITheme.createPrimaryButton("Close", UITheme.DANGER);
        closeBtn.setBounds(355, 445, 140, 38);

        refreshBtn.addActionListener(this);
        closeBtn.addActionListener(this);

        bg.add(topBar);
        bg.add(pageTitle);
        bg.add(subLabel);
        bg.add(scroll);
        bg.add(refreshBtn);
        bg.add(closeBtn);

        f.setVisible(true);
        loadComplaints();
    }

    public void loadComplaints()
    {
        tableModel.setRowCount(0);
        try {
            Connection conn = Database.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM complaints");
            int count = 1;
            while (rs.next())
            {
                tableModel.addRow(new Object[]{
                        count,
                        rs.getString("title"),
                        rs.getString("zone"),
                        rs.getString("description")
                });
                count++;
            }
            conn.close();
            if (count == 1)
            {
                tableModel.addRow(new Object[]{"", "No complaints found", "", ""});
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
            loadComplaints();
        }
        if (e.getSource() == closeBtn)
        {
            f.dispose();
        }
    }
}

