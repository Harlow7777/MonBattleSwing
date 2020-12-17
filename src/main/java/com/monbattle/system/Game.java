package com.monbattle.system;

import com.monbattle.combat.Battle;
import com.monbattle.Util.Util;
import com.monbattle.enums.Action;
import com.monbattle.enums.Environment;
import com.monbattle.enums.State;
import com.monbattle.inventory.Item;
import lombok.Data;
import com.monbattle.monster.MonLibrary;
import com.monbattle.monster.Monster;
import com.monbattle.player.Player;
import com.monbattle.shop.Shop;

import java.io.*;
import java.util.*;

@Data
public class Game {

    private Player player = new Player();
    private MonLibrary lib = new MonLibrary();
    private Environment currEnv;
    private State currState;
    private Monster leadMon;

    /**
     * Provide opening prompts, setup and initialize game state
     * New or Load
     * Setup player name and starter monster
     * Give player 5 capture cards
     */
    public void init() {
        String action = Util.validateInput(Arrays.asList("new", "load"), "New or Load? ");
        if (action.equalsIgnoreCase("New")) {
            String name = Util.validateInput("Hello there, I'm Dr. Octopus. What's your name? ");
            player.setName(name);
            System.out.println("Hello " + name + ", I study wild monsters.");
            System.out.println("My life's goal is to gather information on all species of monsters out there.");
            System.out.println("You can help by taking one of these young monsters with you and hunting down other monsters throughout the world.");
            String starters = "Please choose your starting monster: " + '\n' + "Tater the Ground type " + '\n' + "Sprinkle the Water type" + '\n' + "Blitz the Electric type";
            Monster starterChoice = lib.getMonsterByName(Util.validateInput(Arrays.asList("tater", "sprinkle", "blitz"), starters));
            starterChoice.setNickname(Util.validateInput("What will you name this " + starterChoice.getName()));
            player.getTeam().addMember(starterChoice);
            currState = State.NEW;
            leadMon = player.getTeam().getFirst();
            player.getMonsterpedia().registerMonster(leadMon.getName());
            System.out.println("Have some Capture Cards on me, these are devices that will virtualize wild monsters and add them to your team!");
            player.getBag().addItem("Capture Card", 5);
        } else if (action.equalsIgnoreCase("Load")) {
            loadGame();
            currState = State.LOADED;
        }
    }

    /**
     * Generate a random environment then prompt for input
     */
    public void gameLoop() {
        while(!currState.equals(State.TERMED)) {
            if(!currState.equals(State.LOADED) && !currState.equals(State.SAVED))
                currEnv = Util.randomEnv();

            currState = State.RESUMED;

            Action action = Action.NONE;
            do {
                System.out.println("You find yourself in the " + currEnv);
                switch(currEnv) {
                    case HOSPITAL:
                        player.getTeam().healTeam();
                        break;
                    case SHOP:
                        shop();
                        break;
                    default:
                        action = performAction(Arrays.asList(Action.HUNT, Action.BAG, Action.TEAM, Action.PLAYER, Action.SAVE, Action.LOAD, Action.LEAVE, Action.EXIT),
                                "What will you do?(Hunt, Bag, Team, Player, Save, Leave, Exit) ", null);
                        break;
                }
            } while(!currEnv.equals(Environment.HOSPITAL) && !currEnv.equals(Environment.SHOP) && !action.equals(Action.LEAVE) && !action.equals(Action.EXIT) && !action.equals(Action.HUNT));
        }
    }

    /**
     * Initialize a new shop and prompt for action
     */
    private void shop() {
        System.out.println("Welcome to my shop!");
        Action action;
        Shop s = new Shop(player);
        do {
            action = performAction(Arrays.asList(Action.BUY, Action.SELL, Action.LEAVE), "Buy, Sell, Leave", s);
        } while(!action.equals(Action.LEAVE));
    }

    /**
     * Prompt user for Action, initiate action
     * @param actions List of Actions user can select from
     * @param prompt String prompt to print
     * @return Action enum selected from List based on user input
     */
    private Action performAction(List<Action> actions, String prompt, Shop s) {
        Action action = Util.validateInput(new HashSet<>(actions),
                prompt);

        switch (action) {
            case BUY:
                s.purchaseItem();
                break;
            case SELL:
                s.sellItem(player.openBag(leadMon));
                break;
            case HUNT:
                hunt();
                break;
            case BAG:
                Item choice = player.openBag(leadMon);
                if(!choice.getName().contains("Capture")) choice.use();
                else System.out.println("This is not the time to use that");
                break;
            case TEAM:
                leadMon = player.getTeam().selectTeamLead();
                break;
            case PLAYER:
                player.printStats();
                break;
            case SAVE:
                saveGame();
                currState = State.SAVED;
                break;
            case LOAD:
                loadGame();
                currState = State.LOADED;
            case EXIT:
                savePrompt();
                currState = State.TERMED;
            default:
                break;
        }
        return action;
    }

    /**
     * Generate random Monster and initiate battle loop
     */
    private void hunt() {
        int lowLvl = player.getTeam().averageLvl(); // average of all team levels
        int highLvl = player.getTeam().highestLvl() + player.getTeam().getSize(); // highest level on team +1 for each member
        try {
            Monster foundMon = Util.randomMon(lib.getMonstersByEnv(currEnv));
            player.getMonsterpedia().registerMonster(foundMon.getName());
            // generate level for wild monster
            int level = (int) Math.min(100, ((Math.random() * (highLvl-lowLvl)) + lowLvl));
            // decrement to represent number of lvl ups needed from lvl 1
            level--;
            if(level > 0) foundMon.levelUp(level);
            new Battle(foundMon, leadMon, player).battleLoop();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            System.out.println("No monsters here");
        }

    }

    /**
     * Ask to save the game
     */
    private void savePrompt() {
        if (Util.validateInput(Arrays.asList("y", "n"), "Will you save?(Y or N) ").equalsIgnoreCase("Y"))
            saveGame();
    }

    /**
     * Save the game state by writing to file
     */
    private void saveGame() {
        SaveState save = new SaveState(player, currEnv, leadMon);
        try (FileOutputStream fos   = new FileOutputStream("save.dat");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(save);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Game saved");
    }

    /**
     * Load the game state by reading from save.dat file
     */
    private void loadGame() {
        try (FileInputStream fis   = new FileInputStream("save.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            SaveState save = (SaveState) ois.readObject();
            this.player = save.getPlayer();
            this.currEnv = save.getCurrEnv();
            this.leadMon = save.getLeadMon();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
