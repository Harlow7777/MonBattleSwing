package com.monbattle.Util;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class SwingUtil {
    public static void centerText(JTextPane pane) {
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public static void finalizeWindow(JDialog dialog, int width, int height) {
        dialog.pack();
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static void initializeModal(JDialog dialog, String title) {
        dialog.setLayout(new BorderLayout());
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public static void resetModal(JDialog dialog) {
        dialog.getContentPane().removeAll();
        dialog.revalidate();
        dialog.repaint();
        dialog.setModal(true);
    }
}
