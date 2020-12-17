package com.monbattle.monster;

import com.monbattle.enums.Environment;
import com.monbattle.enums.ExpGroup;
import com.monbattle.enums.Type;
import com.monbattle.growth.Crit;
import com.monbattle.growth.Dmg;
import com.monbattle.growth.Fast;
import com.monbattle.growth.Tank;

import java.io.*;
import java.util.*;

public class MonLibrary {
    public MonLibrary() {
        this.initializeLibrary();
    }

    /**
     * Initialize the library with all monster's base stats
     */
    private void initializeLibrary() {
        try (FileOutputStream fos   = new FileOutputStream("monLibrary.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            Collection<Monster> woi = new ArrayList<>();
            woi.add(new Monster("Tater",
                    Type.GROUND,
                    Arrays.asList(Environment.MOUNTAINS, Environment.VOLCANO, Environment.SWAMP),
                    6,
                    3,
                    4,
                    6,
                    0.75,
                    0.10,
                    5,
                    ExpGroup.MEDIUM,
                    new Tank()));
            woi.add(new Monster("Blitz",
                    Type.ELECTRIC,
                    Collections.singletonList(Environment.POWER_PLANT),
                    4,
                    6,
                    6,
                    3,
                    0.75,
                    0.10,
                    5,
                    ExpGroup.MEDIUM,
                    new Fast()));
            woi.add(new Monster("Sprink",
                    Type.WATER,
                    Arrays.asList(Environment.SWAMP, Environment.LAKE),
                    4,
                    3,
                    6,
                    6,
                    0.75,
                    0.10,
                    5,
                    ExpGroup.MEDIUM,
                    new Dmg()));
            woi.add(new Monster("Thronze",
                    Type.METAL,
                    Arrays.asList(Environment.POWER_PLANT, Environment.GYMNASIUM),
                    6,
                    4,
                    4,
                    6,
                    0.75,
                    0.10,
                    5,
                    ExpGroup.SLOW,
                    new Tank()));
            woi.add(new Monster("Okiku",
                    Type.GHOST,
                    Arrays.asList(Environment.GRAVEYARD, Environment.SWAMP),
                    4,
                    3,
                    7,
                    3,
                    0.75,
                    0.10,
                    5,
                    ExpGroup.MEDIUM,
                    new Crit()));
            oos.writeObject(woi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return List of all monsters
     */
    public List<Monster> getAllMonsters() {
        try (FileInputStream fis   = new FileInputStream("monLibrary.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Monster>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NoSuchElementException("There are no monsters");
    }

    /**
     * Search for monster by name
     * @param name String name to search
     * @return Monster with name matching input parameter
     * @throws NoSuchElementException if there is no monster by the provided name
     */
    public Monster getMonsterByName(String name) throws NoSuchElementException{
        for(Monster m : getAllMonsters()) {
            if(m.getName().toLowerCase().equals(name.toLowerCase()))
                return m;
        }
        throw new NoSuchElementException("No monster by name " + name);
    }

    /**
     * Get all monsters that are associated with specified environment
     * @param env Environment to search
     * @return List of Monsters that can show up in provided environment
     * @throws NoSuchElementException if there are no monsters associated with environment
     */
    public List<Monster> getMonstersByEnv(Environment env) throws NoSuchElementException{
        List<Monster> monsters = new ArrayList<>();
        for(Monster m : getAllMonsters()) {
            if(m.getEnvironments().contains(env))
                monsters.add(m);
        }
        if(!monsters.isEmpty())
            return monsters;
        throw new NoSuchElementException("No monster in the " + env);
    }
}
