package com.monbattle.display;

import com.monbattle.Util.SwingUtil;
import com.monbattle.Util.Timers;
import com.monbattle.enums.Environment;
import com.monbattle.monster.Monster;
import com.monbattle.player.Player;
import com.monbattle.system.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import static com.monbattle.Util.Constants.SPRITE_DIR;

public class BattleWindow extends JDialog {

    private Player player;
    private Environment currEnv;
    private Monster foundMon;

    public BattleWindow(Player player, Environment currEnv) {
        this.player = player;
        this.currEnv = currEnv;
        foundMon = Game.hunt(player, currEnv);
        SwingUtil.initializeModal(this, "Fight!");

        JPanel battlePanel = new JPanel(new GridLayout(2,2));

        JPanel statusPanel = new JPanel();
        JTextArea foundMonText = new JTextArea(foundMon.getName());
        JProgressBar foundMonHealth = createHealthBar(foundMon.getHP(), foundMon.getMaxHP());
        statusPanel.add(foundMonText);
        statusPanel.add(foundMonHealth);
        battlePanel.add(statusPanel);
//        battlePanel.add(monsterStatusPanel(foundMon));

        try {
            JLabel foundMonSprite = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + foundMon.getName().toLowerCase() + "_front.jpeg"))));
            battlePanel.add(foundMonSprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Monster mon = player.getTeam().getFirst();
        try {
            JLabel monSprite = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + mon.getName().toLowerCase() + "_back.jpeg"))));
            battlePanel.add(monSprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        battlePanel.add(monsterStatusPanel(mon));

        this.add(battlePanel, BorderLayout.CENTER);

        //Bottom message panel
        //TODO: add action buttons
        JPanel messagePanel = new JPanel();
        JTextPane message = new JTextPane();
        message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        SwingUtil.centerText(message);

        JButton attackButton = new JButton("Attack");
        attackButton.addActionListener(e -> {
            //TODO: change out for real attack
            foundMon.setHP(foundMon.getHP()-1);
            foundMonHealth.setValue(foundMon.getHP());
        });
        messagePanel.add(attackButton);

        this.add(messagePanel, BorderLayout.SOUTH);
        Timers.displayText("A wild " + foundMon.getName() + " appears!", message);
        //TODO: add pause
        Timers.displayText("Go! " + mon.getName(), message);

        SwingUtil.finalizeWindow(this, 500, 500);
    }

    private JPanel monsterStatusPanel(Monster mon) {
        JPanel statusPanel = new JPanel();
        JTextArea monText = new JTextArea(mon.getName());
        JProgressBar monHealth = createHealthBar(mon.getHP(), mon.getMaxHP());
        statusPanel.add(monText);
        statusPanel.add(monHealth);
        return statusPanel;
    }

    private JProgressBar createHealthBar(int HP, int maxHP) {
        JProgressBar statusBar = new JProgressBar();
        statusBar.setBorderPainted(true);
        statusBar.setMinimum(0);
        statusBar.setMaximum(maxHP);
        statusBar.setValue(HP);
        statusBar.setStringPainted(true);
        return statusBar;
    }

}
