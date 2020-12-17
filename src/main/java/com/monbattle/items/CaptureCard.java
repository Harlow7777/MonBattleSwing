package com.monbattle.items;

import com.monbattle.inventory.Capture;

public class CaptureCard extends Capture {
    public CaptureCard() {
        this.setName("Capture Card");
        this.setQuantity(1);
        this.setPrice(200);
    }

    @Override
    public boolean use() {
        super.use();
        return capture(12);
    }
}
