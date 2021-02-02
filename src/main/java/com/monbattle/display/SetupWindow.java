package com.monbattle.display;

import com.monbattle.Util.SwingUtil;
import com.monbattle.Util.Timers;
import com.monbattle.monster.MonLibrary;
import com.monbattle.monster.Monster;
import com.monbattle.player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import static com.monbattle.Util.Constants.*;

public class SetupWindow extends JDialog {
    private final Player player;
    private final Font font = new Font("SansSerif", Font.BOLD, 20);

    public SetupWindow(Player player) {
        this.player = player;
        MonLibrary.initializeLibrary();
        SwingUtil.initializeModal(this, "Choose your name");

        try {
            JLabel profImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + PROF_SPRITE))));
            this.add(profImg, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Wrap message and image in panel to stack vertically
        JPanel p = new JPanel(new BorderLayout());
        JTextPane message = new JTextPane();
        message.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        SwingUtil.centerText(message);
        p.add(message, BorderLayout.CENTER);
        Timers.displayText("Hello there, I'm Dr. Octopus. What's your name? ", message);

        // Wrap textField in panel to allow sizing
        JTextField textField = new JTextField();
        textField.setPreferredSize( new Dimension( 100, 24 ) );
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    player.setName(textField.getText());
                    starterSelection();
                }
            }
        });
        p.add(textField, BorderLayout.SOUTH);
        this.add(p, BorderLayout.SOUTH);

        SwingUtil.finalizeWindow(this, 300, 300);
    }

    private void starterSelection() {
        SwingUtil.resetModal(this);
        this.setTitle("Choose your starter");
        this.setLayout(new GridLayout(2, 1));

        JTextPane messageTop = new JTextPane();
        messageTop.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        SwingUtil.centerText(messageTop);
        String msgString = "Hello " + player.getName() + ", I study wild monsters. " +
                "My life's goal is to gather information on all species of monsters out there. " +
                "You can help by taking one of these young monsters with you and hunting down other monsters throughout the world.";
        messageTop.setFont(font);
        Timers.displayText(msgString, messageTop);

        JPanel messageBottomPanel = new JPanel(new BorderLayout());
        JTextPane messageBottom = new JTextPane();
        messageBottom.setText("Choose your starting monster ");
        SwingUtil.centerText(messageBottom);
        messageBottomPanel.add(messageBottom, BorderLayout.NORTH);
        try {
            JLabel profImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + PROF_SPRITE))));
            messageBottomPanel.add(profImg, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel p = new JPanel(new GridLayout(2, 1));
        p.add(messageTop);
        p.add(messageBottomPanel);

        this.add(p);

        JPanel selectionPanel = new JPanel(new GridLayout(2, 3));
        try {
            JLabel taterImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + TATER_SPRITE))));
            selectionPanel.add(taterImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JLabel blitzImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + BLITZ_SPRITE))));
            selectionPanel.add(blitzImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JLabel sprinkImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + SPRINK_SPRITE))));
            selectionPanel.add(sprinkImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: add event handlers
        JButton taterButton = new JButton("Tater(Ground)");
        JDialog jd = this;
        taterButton.addActionListener(e -> {
            player.getTeam().addMember(MonLibrary.getMonsterByName("Tater"));
            jd.dispose();
        });
        JButton sprinkButton = new JButton("Sprink(Water)");
        sprinkButton.addActionListener(e -> {
            player.getTeam().addMember(MonLibrary.getMonsterByName("Sprink"));
            jd.dispose();
        });
        JButton blitzButton = new JButton("Blitz(Electric)");
        blitzButton.addActionListener(e -> {
            player.getTeam().addMember(MonLibrary.getMonsterByName("Blitz"));
            jd.dispose();
        });
        selectionPanel.add(taterButton);
        selectionPanel.add(sprinkButton);
        selectionPanel.add(blitzButton);
        this.add(selectionPanel);

        SwingUtil.finalizeWindow(this, 1000, 1000);

        //TODO: prompt for nickname

    }
}
