package com.monbattle.display;

import com.monbattle.Util.SwingUtil;
import com.monbattle.Util.Timers;
import com.monbattle.Util.Util;
import com.monbattle.enums.Action;
import com.monbattle.enums.Environment;
import com.monbattle.player.Player;
import com.monbattle.system.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.monbattle.Util.Constants.BG_DIR;

public class GameWindow extends JDialog {
    private Player player;
    private Environment currEnv;

    public GameWindow(Player player) {
        this.player = player;
        SwingUtil.initializeModal(this, "MonBattle");
        gameLoop();
    }

    private void gameLoop() {
        SwingUtil.resetModal(this);
        //TODO: Check for load action before generating new env
        currEnv = Util.randomEnv();

        // background image
        //TODO: try extending jpanel and overriding paintComponent
        try {
            this.setContentPane(new JPanel() {
                BufferedImage image = ImageIO.read(new File(BG_DIR + currEnv + ".jpeg"));

                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, 1000, 1000, this);
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }

        //Header text
        JTextPane message = new JTextPane();
        SwingUtil.centerText(message);
        this.add(message, BorderLayout.NORTH);
        Timers.displayText("You find yourself in the " + currEnv, message);

        switch(currEnv) {
            case HOSPITAL:
                this.player.getTeam().healTeam();
                JTextPane update = new JTextPane();
                SwingUtil.centerText(update);
                this.add(update, BorderLayout.NORTH);
                Timers.displayText("Your team is fully healed!", message);
                break;
            case SHOP:
//                ShopWindow shopWindow = new ShopWindow();
//                shop();
                break;
            default:
                //setup buttons
                JPanel buttonPanel = new JPanel();
                //TODO: add action buttons
                this.add(buttonPanel, BorderLayout.CENTER);
//                action = performAction(Arrays.asList(com.monbattle.enums.Action.HUNT, com.monbattle.enums.Action.BAG, com.monbattle.enums.Action.TEAM, com.monbattle.enums.Action.PLAYER, com.monbattle.enums.Action.SAVE, com.monbattle.enums.Action.LOAD, com.monbattle.enums.Action.LEAVE, Action.EXIT),
//                        "What will you do?(Hunt, Bag, Team, Player, Save, Leave, Exit) ", null);
                break;
        }

        JPanel systemButtonPanel = new JPanel();
        JButton leaveButton = new JButton(Action.LEAVE.toString());
        leaveButton.addActionListener(e -> gameLoop());
        systemButtonPanel.add(leaveButton);

        JButton saveButton = new JButton(Action.SAVE.toString());
        saveButton.addActionListener(e -> Game.saveGame(this.player, this.currEnv));
        systemButtonPanel.add(saveButton);

        JButton exitButton = new JButton(Action.EXIT.toString());
        exitButton.addActionListener(e -> System.exit(1));
        systemButtonPanel.add(exitButton);

        this.add(systemButtonPanel, BorderLayout.CENTER);

        SwingUtil.finalizeWindow(this, 1000, 1000);
    }
}
