package com.monbattle.inventory;

import java.util.Random;

public class Capture extends Item {
    protected boolean capture(int captureCardRating) {
        if (new Random().nextInt(255) < (target.getMaxHP() * 255 * 4) / (target.getHP() * captureCardRating)) {
            return true;
        } else {
            System.out.println(target.getName() + " broke free");
            return false;
        }
    }
}
