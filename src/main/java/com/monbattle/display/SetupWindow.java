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
        SwingUtil.initializeModal(this, "Choose your name");

        //Wrap message and image in panel to stack vertically
        JPanel p = new JPanel(new BorderLayout());
        JTextPane message = new JTextPane();
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
        this.add(p, BorderLayout.NORTH);

        try {
            JLabel profImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + PROF_SPRITE))));
            this.add(profImg, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtil.finalizeWindow(this, 300, 300);
    }

    private void starterSelection() {
        SwingUtil.resetModal(this);
        this.setTitle("Choose your starter");
        this.setLayout(new GridLayout(2, 1));

        JTextPane messageTop = new JTextPane();
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
            JLabel sprinkleImg = new JLabel(new ImageIcon(ImageIO.read(new File(SPRITE_DIR + SPRINKLE_SPRITE))));
            selectionPanel.add(sprinkleImg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO: add event handlers
        JButton taterButton = new JButton("Tater(Ground)");
        JDialog jd = this;
        taterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jd.dispose();
            }
        });
        JButton sprinkleButton = new JButton("Sprinkle(Water)");
        JButton blitzButton = new JButton("Blitz(Electric)");
        selectionPanel.add(taterButton);
        selectionPanel.add(sprinkleButton);
        selectionPanel.add(blitzButton);
        this.add(selectionPanel);

        SwingUtil.finalizeWindow(this, 1000, 1000);

        Monster starter = MonLibrary.getMonsterByName("Blitz");
        //TODO: prompt for nickname

        player.getTeam().addMember(starter);
    }
}
