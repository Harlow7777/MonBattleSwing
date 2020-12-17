package com.monbattle.shop;

import com.monbattle.Util.Util;
import com.monbattle.inventory.Inventory;
import com.monbattle.inventory.Item;
import lombok.Data;
import com.monbattle.player.Player;

@Data
public class Shop {
    private Inventory inventory = new Inventory();
    private Player player;

    /**
     * Initialize new shop with inventory of up to 10 random items
     * @param player Player container for inventory
     */
    public Shop(Player player) {
        this.player = player;
        for(int i = 0; i < (Math.random()*10); i++) {
            Item item = Inventory.generateRandomItem();
            if(item != null)
                inventory.addItem(item, 1);
        }
    }

    public void purchaseItem() {
        inventory.printShopInventory();
        System.out.println("You have $" + player.getMoney());
        Item i = Util.validateItemSelection(inventory.getBag(), "Purchase which item?(Item name or none) ");
        if (i != null) {
            int quantity = i.getQuantity() > 1 ? Util.validateNumericSelection(i.getQuantity(), "How many do you want?") : 1;
            try {
                player.removeMoney(i.getPrice() * quantity);
                Item purchased = (Item) i.clone();
                purchased.setQuantity(quantity);
                player.getBag().addItem(purchased, quantity);
                inventory.removeItem(i, quantity);
                System.out.println(purchased.getQuantity() + " " + purchased.getName() + "(s) added to your bag");
                System.out.println("You now have $" + player.getMoney());
            } catch (IndexOutOfBoundsException | CloneNotSupportedException e) {
                System.out.println("You don't have enough money: $" + player.getMoney());
            }
        }
    }

    public void sellItem(Item item) {
        if(item != null && !item.getName().equalsIgnoreCase("none")) {
            int quantity = item.getQuantity() > 1 ? Util.validateNumericSelection(item.getQuantity(), "How many do you want to sell?") : 1;
            // clone item(s) and add to shop inventory
            try {
                Item itemClone = (Item) item.clone();
                itemClone.setQuantity(quantity);
                inventory.addItem(itemClone, quantity);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            // remove original item from player's bag and pay for it
            player.getBag().removeItem(item, quantity);
            player.addMoney(item.getPrice() * quantity);
            System.out.println("You now have $" + player.getMoney());

        }
    }
}
