package com.monbattle.player;

import com.monbattle.inventory.Inventory;
import com.monbattle.inventory.Item;
import lombok.Data;
import com.monbattle.monster.Monster;
import com.monbattle.monster.Monsterpedia;
import com.monbattle.monster.Team;

import java.io.Serializable;
import java.util.NoSuchElementException;

@Data
public class Player implements Serializable {
    private String name;
    private Team team = new Team();
    private Inventory bag = new Inventory();
    private int money = 0;
    private Monsterpedia monsterpedia = new Monsterpedia();

    public void addMoney(int amount) {
        this.money += amount;
    }

    public void removeMoney(int amount) {
        if(this.money >= amount)
            this.money -= amount;
        else
            throw new IndexOutOfBoundsException("Not enough money");
    }

    /**
     * Prompts for selection from bag, Sets target
     * @param target Monster to use item on
     * @return Item selected from bag or Item named none
     */
    public Item openBag(Monster target) {
        Item none = new Item();
        none.setName("none");
        if (bag.bagSize() > 0) {
            try {
                Item i = bag.selectItem();
                if (i == null) return none;
                if (i.getName().contains("Capture"))
                    i.setTarget(target);
                else
                    i.setTarget(team.selectTeamMember());
                return i;
            } catch(NoSuchElementException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("No items in bag");
        }
        return none;
    }

    public void printStats() {
        System.out.println(name + " has seen " + this.monsterpedia.registeredCount() + " monster(s) out of " + this.monsterpedia.fullCount());
        System.out.println(team.getSize() + " monster(s) on team");
        System.out.println("$" + this.money + " in wallet");
    }
}
