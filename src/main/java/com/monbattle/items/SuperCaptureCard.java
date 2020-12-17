package com.monbattle.items;

import com.monbattle.inventory.Capture;

public class SuperCaptureCard extends Capture {
    public SuperCaptureCard() {
        this.setName("Super Capture Card");
        this.setQuantity(1);
        this.setPrice(600);
    }

    @Override
    public boolean use() {
        super.use();
        return capture(8);
    }
}
