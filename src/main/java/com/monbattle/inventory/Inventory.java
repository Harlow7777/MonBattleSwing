package com.monbattle.inventory;

import com.monbattle.Util.Util;
import lombok.Data;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Data
public class Inventory implements Serializable {

    private List<Item> bag = new ArrayList<>();

    /**
     * Get Item from bag matching name parameter
     * @param name String name to match item
     * @return Item matching provided name parameter
     * @throws NoSuchElementException if Item with matching name was not found
     */
    public Item getItemByName(String name) throws NoSuchElementException {
        for(Item i : bag) {
            if(i.getName().equalsIgnoreCase(name))
                return i;
        }
        throw new NoSuchElementException("Item named " + name + " is not in your bag");
    }

    /**
     * @return List of all item names from bag
     */
    public List<String> itemNames() {
        List<String> itemNames = new ArrayList<>();
        for(Item i : bag) {
            if(i.getQuantity() > 0)
                itemNames.add(i.getName());
        }
        return itemNames;
    }

    /**
     * @return int, number of unique items in bag
     */
    public int bagSize() {
        int bagSize = 0;
        for(Item i : this.bag) {
            if(i.getQuantity() > 0)
                bagSize++;
        }
        return bagSize;
    }

    /**
     * Print items in bag and prompt for selection
     * @return Item matching selection based on user input
     * @throws NoSuchElementException if Item selected has quantity less than or equal to 0
     */
    public Item selectItem() throws NoSuchElementException {
        printItems();
        List<String> itemNames = itemNames();
        itemNames.add("none");
        String name = Util.validateInput(itemNames,
                "Choose item " + itemNames);
        if(name.equalsIgnoreCase("none")) return null;
        Item choice = getItemByName(name);
        if(choice.getQuantity() > 0)
            return choice;
        else
            throw new NoSuchElementException("There are no more " + choice.getName() + "s");
    }

    /**
     * Check to see if item is already in bag
     * If so, increase quantity by amount
     * If not, add as new object instantiated by name
     * @param amount int, number of items to add
     * @param name String, name of item
     */
    public void addItem(String name, int amount) {
        try {
            getItemByName(name).add(amount);
        } catch(NoSuchElementException e) {
            try {
                //If generating from name, must use classname by removing whitespace
                Item item = generateItemFromName(name.replace(" ", ""));
                item.setQuantity(amount);
                this.getBag().add(item);
            } catch(NoSuchElementException e2) {
                e2.printStackTrace();
                System.out.println("No item by name: " + name);
            }
        }
    }

    /**
     * Check to see if item is already in bag
     * If so, increase quantity by 1
     * If not, add as new
     * @param item Item to be added
     * @param amount int amount to be added
     */
    public void addItem(Item item, int amount) {
        try {
            getItemByName(item.getName()).add(amount);
        } catch(NoSuchElementException e) {
            this.getBag().add(item);
        }
    }

    /**
     * Remove an amount of items
     * @param item Item to be removed
     * @param amount Quantity to be removed
     */
    public void removeItem(Item item, int amount) {
        try {
            getItemByName(item.getName()).remove(amount);
            if(item.getQuantity() <= 0) {
                this.getBag().remove(item);
            }
        } catch(NoSuchElementException e) {
            e.printStackTrace();
            System.out.println(item.getName() + " is not in your bag");
        }
    }

    /**
     * @return random Item from all possible Item subclasses
     */
    public static Item generateRandomItem() {
        Set<Class<? extends Item>> allClasses =
                new Reflections("com/monbattle/items").getSubTypesOf(Item.class);
        // Subtract 1 to account for Capture
        allClasses.remove(Capture.class);
        int randInt = new Random().nextInt(allClasses.size());
        int i = 0;
        for(Class<? extends Item> item : allClasses)
        {
            if (i == randInt) {
                try {
                    Item it = item.newInstance();
                    it.setQuantity(1);
                    return it;
                } catch(IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }
        return null;
    }

    /**
     * Get new instance of item class matching className parameter
     * @param className name of the class to generate
     * @return Item new instance
     * @throws NoSuchElementException if there was no item class matching className
     */
    public static Item generateItemFromName(String className) throws NoSuchElementException {
        Set<Class<? extends Item>> allClasses =
                new Reflections("com.monbattle.items").getSubTypesOf(Item.class);
        for(Class<? extends Item> item : allClasses)
        {
            if (item.getName().equalsIgnoreCase("com.monbattle.items." + className)) {
                try {
                    return item.getDeclaredConstructor().newInstance();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new NoSuchElementException("No item by name " + className);
    }

    /**
     * Print name, quantity and price of all items in bag
     */
    public void printShopInventory() {
        for(Item i : this.bag) {
            System.out.println(i.getName() + " x" + i.getQuantity() + " $" + i.getPrice());
        }
    }

    /**
     * Print name and quantity of all items in bag
     */
    public void printItems() {
        for(Item i : bag) {
            if(i.getQuantity() > 0)
                System.out.println(i.getName() + " x" + i.getQuantity());
        }
    }
}
