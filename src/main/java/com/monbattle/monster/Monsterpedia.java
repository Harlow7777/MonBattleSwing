package com.monbattle.monster;

import java.io.Serializable;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class Monsterpedia implements Serializable {
    HashMap<String, Boolean> entries = new HashMap<>();

    /**
     * Initialize monsterpedia with all monsters from library with registration status false
     */
    public Monsterpedia() {
        for(Monster m : new MonLibrary().getAllMonsters()) {
            this.entries.put(m.getName(), false);
        }
    }

    /**
     * Change registration status to true for monster with specified name
     * @param name String name of monster to register
     * @throws NoSuchElementException if no monster is found by the provided name
     */
    public void registerMonster(String name) throws NoSuchElementException{
        if(entries.containsKey(name))
            this.entries.put(name, true);
        else
            throw new NoSuchElementException("A monster by this name is not in the monsterpedia");
    }

    /**
     * @return int number of monsters with registration status true
     */
    public int registeredCount() {
        int i = 0;
        for(boolean val : entries.values())
            if(val) i++;
        return i;
    }

    /**
     * @return int count of all monsters in monsterpedia regardless of registration status
     */
    public int fullCount() {
        return entries.size();
    }
}
