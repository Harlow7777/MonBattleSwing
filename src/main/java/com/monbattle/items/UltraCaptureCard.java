package com.monbattle.items;

import com.monbattle.inventory.Capture;

public class UltraCaptureCard extends Capture {
    public UltraCaptureCard() {
        this.setName("Super Capture Card");
        this.setQuantity(1);
        this.setPrice(1200);
    }

    @Override
    public boolean use() {
        super.use();
        return capture(6);
    }
}
