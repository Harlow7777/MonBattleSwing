package com.monbattle.inventory;

import lombok.Data;
import com.monbattle.monster.Monster;

import java.io.Serializable;

@Data
public class Item implements Serializable, Cloneable {
    protected String name;
    protected int quantity;
    protected Monster target;
    public int price;

    /**
     * @return clone of Item to avoid pass by reference issues
     * @throws CloneNotSupportedException if clone failed
     */
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public int add(int amount) {
        quantity+=amount;
        return quantity;
    }

    public int remove(int amount) {
        quantity-=amount;
        return quantity;
    }

    public boolean use() {
        quantity--;
        return true;
    }
}
