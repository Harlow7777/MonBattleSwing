package com.monbattle.display;

import com.monbattle.Util.SwingUtil;
import com.monbattle.Util.Timers;
import com.monbattle.Util.Util;
import com.monbattle.enums.Action;
import com.monbattle.enums.Environment;
import com.monbattle.enums.State;
import com.monbattle.player.Player;
import com.monbattle.system.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.monbattle.Util.Constants.BG_DIR;

public class GameWindow extends JDialog {
    private Player player;
    private Environment currEnv;

    public GameWindow(Player player, State currState) {
        this.player = player;
        SwingUtil.initializeModal(this, "MonBattle");
        gameLoop(currState);
    }

    private void gameLoop(State currState) {
        SwingUtil.resetModal(this);
        if(currState != State.LOADED)
            currEnv = Util.randomEnv();
        currState = State.RESUMED;

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
        this.add(message, BorderLayout.SOUTH);
        Timers.displayText("You find yourself in the " + currEnv, message);

        JPanel systemButtonPanel = new JPanel();

        switch(currEnv) {
            case HOSPITAL:
                this.player.getTeam().healTeam();
                Timers.displayText("Your team is fully healed!", message);
                break;
            case SHOP:
//                ShopWindow shopWindow = new ShopWindow();
//                shop();
                break;
            default:
                //setup buttons
                //TODO: add action buttons
                JButton huntButton = new JButton("Hunt");
                huntButton.addActionListener(e -> {
                    BattleWindow battleWindow = new BattleWindow(player, currEnv);
                });
                systemButtonPanel.add(huntButton);
//                action = performAction(Arrays.asList(com.monbattle.enums.Action.HUNT, com.monbattle.enums.Action.BAG, com.monbattle.enums.Action.TEAM, com.monbattle.enums.Action.PLAYER, com.monbattle.enums.Action.SAVE, com.monbattle.enums.Action.LOAD, com.monbattle.enums.Action.LEAVE, Action.EXIT),
//                        "What will you do?(Hunt, Bag, Team, Player, Save, Leave, Exit) ", null);
                break;
        }

        JButton leaveButton = new JButton(Action.LEAVE.toString());
        leaveButton.addActionListener(e -> gameLoop(State.RESUMED));
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
