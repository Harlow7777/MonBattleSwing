package com.monbattle.display;

import com.monbattle.system.Game;

import javax.swing.*;

public class MainPanel extends JFrame {

    private JSplitPane itemPane;

    private Game game;

    public MainPanel() {
        setTitle("MonBattle");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        addComponents();
    }

    public void addComponents() {
        JPanel p = new JPanel();
        JTextArea message = new JTextArea();
        p.add(message);
        JButton newButton = new JButton("New");
        newButton.addActionListener(e -> game = new Game("New"));
        JButton loadButton = new JButton("Load");
        loadButton.addActionListener(e -> game = new Game("Load"));
        this.add(p);
        p.add(newButton);
        p.add(loadButton);
        pack();
    }
}
