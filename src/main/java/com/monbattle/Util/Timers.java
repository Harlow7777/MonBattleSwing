package com.monbattle.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Timers {
    public static void displayText(String msgString, JTextPane pane) {
        pane.setEditable(false);
        Timer SimpleTimer = new Timer(25, new ActionListener(){
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if(counter <= msgString.length()) {
                    pane.setText(msgString.substring(0, counter));
                }
            }
        });
        SimpleTimer.start();
    }
}
