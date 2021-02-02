package com.monbattle.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Timers {
    public static Timer displayText(String msgString, JTextPane pane) {
        pane.setEditable(false);
        Timer simpleTimer = new Timer(25, null);
        simpleTimer.addActionListener(new ActionListener(){
            int counter = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                counter++;
                if(counter <= msgString.length()) {
                    pane.setText(msgString.substring(0, counter));
                } else {
                    simpleTimer.stop();
                }
            }
        });
        simpleTimer.start();
        return simpleTimer;
    }
}
