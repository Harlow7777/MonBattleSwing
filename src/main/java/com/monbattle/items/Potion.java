package com.monbattle.items;

import com.monbattle.inventory.Item;

public class Potion extends Item {
    public Potion() {
        this.setName("Potion");
        this.setQuantity(1);
        this.setPrice(200);
    }

    @Override
    public boolean use() {
        super.use();
        int prevHP = target.getHP();
        if(target.getHP() == target.getMaxHP())
            return false;
        else
            target.setHP(Math.min(target.getHP() + 20, target.getMaxHP()));
        System.out.println(target.getNickname() + " healed " + (target.getHP()-prevHP));
        return true;
    }
}
