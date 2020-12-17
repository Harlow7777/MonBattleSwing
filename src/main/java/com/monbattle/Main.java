package com.monbattle;

import java.io.*;
import java.awt.GraphicsEnvironment;

public class Main{
    public static void main (String [] args) throws IOException{
        Console console = System.console();
        if(console == null && !GraphicsEnvironment.isHeadless()){
            String filename = GameMain.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + filename + "\""});
        }else{
            GameMain.main(new String[0]);
            System.out.println("Program has ended, please type 'exit' to close the console");
        }
    }
}