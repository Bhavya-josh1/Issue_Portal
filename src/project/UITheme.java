package project;

import javax.swing.*;
import java.awt.*;

public class UITheme {

    // Colors
    public static final Color BG_DARK   = new Color(20, 25, 45);
    public static final Color BG_CARD   = new Color(30, 38, 65);
    public static final Color BG_INPUT  = new Color(45, 55, 85);
    public static final Color BLUE      = new Color(70, 130, 210);
    public static final Color GREEN     = new Color(50, 180, 110);
    public static final Color RED       = new Color(210, 70, 70);
    public static final Color YELLOW    = new Color(230, 180, 50);
    public static final Color WHITE     = new Color(225, 230, 245);
    public static final Color GRAY      = new Color(140, 150, 175);
    public static final Color BORDER    = new Color(60, 75, 120);

    // Fonts
    public static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);

    public static void styleFrame(JFrame f, int w, int h) {
        f.setSize(w, h);
        f.setLayout(null);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().setBackground(BG_DARK);
    }

    public static void styleButton(JButton b, Color bg) {
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BOLD);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleField(JTextField tf) {
        tf.setBackground(BG_INPUT);
        tf.setForeground(WHITE);
        tf.setCaretColor(WHITE);
        tf.setFont(FONT_INPUT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    public static void styleLabel(JLabel l, Color c) {
        l.setForeground(c);
        l.setFont(FONT_LABEL);
    }

    // Simple card panel - colored background with border
    public static JPanel createCard(int x, int y, int w, int h) {
        JPanel card = new JPanel(null);
        card.setBackground(BG_CARD);
        card.setBounds(x, y, w, h);
        card.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        return card;
    }
}