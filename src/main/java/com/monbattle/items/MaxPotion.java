package com.monbattle.items;

import com.monbattle.inventory.Item;

public class MaxPotion extends Item {
    public MaxPotion() {
        this.setName("Max Potion");
        this.setQuantity(1);
        this.setPrice(2500);
    }

    @Override
    public boolean use() {
        super.use();
        if(target.getHP() == target.getMaxHP())
            return false;
        else
            target.setHP(target.getMaxHP());
        System.out.println(target.getNickname() + " fully healed!");
        return true;
    }
}
