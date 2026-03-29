package project;

import javax.swing.*;
import java.awt.*;

public class UITheme
{

    public static final Color BG_DARK      = new Color(15, 23, 42);
    public static final Color BG_CARD      = new Color(30, 41, 59);
    public static final Color BG_INPUT     = new Color(51, 65, 85);
    public static final Color ACCENT       = new Color(99, 179, 237);
    public static final Color SUCCESS      = new Color(74, 222, 128);
    public static final Color DANGER       = new Color(248, 113, 113);
    public static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    public static final Color TEXT_MUTED   = new Color(148, 163, 184);
    public static final Color BORDER       = new Color(71, 85, 105);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    public static JPanel createCard(int x, int y, int w, int h)
    {
        JPanel card = new JPanel(null)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBounds(x, y, w, h);
        return card;
    }

    public static JTextField createTextField()
    {
        JTextField tf = new JTextField();
        tf.setBackground(BG_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT);
        tf.setFont(FONT_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        return tf;
    }

    public static JPasswordField createPasswordField()
    {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(BG_INPUT);
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(ACCENT);
        pf.setFont(FONT_INPUT);
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));
        return pf;
    }

    public static JButton createPrimaryButton(String text, Color bg)
    {
        JButton btn = new JButton(text)
        {
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isPressed() ? bg.darker()
                        : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static JLabel createLabel(String text, Color color, Font font)
    {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(color);
        lbl.setFont(font);
        return lbl;
    }

    public static JSeparator createDivider(int x, int y, int w)
    {
        JSeparator sep = new JSeparator();
        sep.setBounds(x, y, w, 1);
        sep.setForeground(BORDER);
        sep.setBackground(BORDER);
        return sep;
    }
}
