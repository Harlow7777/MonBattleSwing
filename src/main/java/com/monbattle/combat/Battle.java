package com.monbattle.combat;

import com.monbattle.Util.Util;
import com.monbattle.enums.Action;
import com.monbattle.inventory.Inventory;
import com.monbattle.inventory.Item;
import lombok.Data;
import com.monbattle.monster.Monster;
import com.monbattle.player.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

@Data
public class Battle {

    private Monster offense, defense, wildMon, teamMon;
    private Action action;
    private int turnCount, escapeCount;
    private boolean caught, escape, fainted;
    private Item selectedItem = null;
    private Player player;

    public Battle(Monster foundMon, Monster leadMon, Player player) {
        this.wildMon = foundMon;
        this.teamMon = leadMon;
        this.turnCount = 0;
        this.escapeCount = 0;
        this.caught = false;
        this.escape = false;
        this.fainted = false;
        this.player = player;
    }

    /**
     * Calculates priority based on speed, mon1 wins if tied
     * @param mon1 Monster
     * @param mon2 Monster
     */
    private void priority(Monster mon1, Monster mon2) {
        if(mon1.getSpeed() >= mon2.getSpeed()) {
            offense = mon1;
            defense = mon2;
        } else {
            offense = mon2;
            defense = mon1;
        }
    }

    /**
     * Print monster stats, then prompt for action
     */
    public void battleLoop() {
        System.out.println("A wild " + wildMon.getName() + " appeared");
        do {
            teamMon.printBattleStats();
            System.out.println("................");
            wildMon.printEnemyBattleStats();

            action = Util.validateInput(new HashSet<>(Arrays.asList(Action.ATTACK, Action.BAG, Action.TEAM, Action.RUN)), "Attack, Bag, Team, Run");
            switch (action) {
                case ATTACK:
                    turn();
                    break;
                case BAG:
                    selectedItem = player.openBag(wildMon);
                    boolean used = selectedItem.use();
                    caught = selectedItem.getName().contains("Capture") && used;
                    break;
                case TEAM:
                    teamMon = player.getTeam().selectTeamLead();
                    break;
                case RUN:
                    escapeCount++;
                    escape = escape(wildMon, escapeCount);
                    if (escape) System.out.println("You successfully escaped");
                    else System.out.println("You couldn't run");
                    break;
                default:
                    break;
            }
            // If caught, escaped, attack or bag was open but not used
            if (caught || escape || action.equals(Action.ATTACK) || (selectedItem != null && selectedItem.getName().equalsIgnoreCase("none"))) {

            } else {
                // Follow up attack after failed capture/escape, item usage or team lead switch
                wildMon.attack(teamMon);
            }
            fainted = faintCheck(teamMon);
        } while (!escape && !caught && wildMon.getHP() > 0 && !fainted);
        checkCaught();
        generateRewards();
    }

    /**
     * Determines if currMon has fainted, select new team lead or heals and exits game loop
     * @param currMon Monster to check
     * @return true if all monsters on team fainted or false if currMon is healthy/new team lead is available
     */
    private boolean faintCheck(Monster currMon) {
        if(currMon.getHP() <= 0) {
            System.out.println(currMon.getNickname() + " fainted");
            if(player.getTeam().hasFitForBattle()) {
                teamMon = player.getTeam().selectTeamLead();
                System.out.println("You send out ");
                return false;
            } else {
                System.out.println("You're out of conscious monsters, sending to healing center");
                player.getTeam().healTeam();
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Check to see if wildMon is caught, if so add to team
     */
    private void checkCaught() {
        if(caught) {
            System.out.println("Congrats! You caught " + wildMon.getName());
            String nameChoice;
            do {
                nameChoice = Util.validateInput("What will you name this " + wildMon.getName() + "?");
            } while(player.getTeam().memberNicknames().contains(nameChoice));
            wildMon.setNickname(nameChoice);
            player.getTeam().addMember(wildMon);
            wildMon.printFullStats();
        }
    }

    /**
     * Generate rewards(exp, money and random item) for winning battle
     */
    private void generateRewards() {
        if(wildMon.getHP() <= 0)
            System.out.println("Congrats you beat " + wildMon.getName());
        //TODO: add exp gain for other monsters who participated
        if(!fainted && !action.equals(Action.RUN)) {
            // exp gain
            teamMon.expGain(wildMon, turnCount);
            // random item drop
            Item randomDrop = Inventory.generateRandomItem();
            if(randomDrop != null) {
                String itemFate = wildMon.getHP() <= 0 ? " dropped " : " was holding ";
                System.out.println(wildMon.getName() + itemFate + randomDrop.getName());
                player.getBag().addItem(randomDrop, 1);
            }
            // money drop
            player.addMoney(wildMon.getLevel() * 40);
            System.out.println("You earned $" + wildMon.getLevel() * 40);
        }
    }

    /**
     * Determines if you were able to escape wild monster
     * @param foundMon Monster to escape from
     * @param escapeCount Number of times escape attempt has been made
     * @return true if escaped, false otherwise
     */
    private boolean escape(Monster foundMon, int escapeCount) {
        if(foundMon.getSpeed() < teamMon.getSpeed()) {
            return true;
        } else {
            int b = (foundMon.getSpeed() / 4) % 256;
            if(b == 0) return true;
            int f = ((teamMon.getSpeed() * 32) / b) + 30 * escapeCount;
            if(f > 255) {
                return true;
            } else {
                return new Random().nextInt(255) < f;
            }
        }
    }

    /**
     * Faster monster attacks first then reverse offense and defense for second attack
     */
    public void turn() {
        turnCount++;
        priority(wildMon, teamMon);
        // first attack
        if(!offense.attack(defense)) return;

        Monster tempMon = offense;
        offense = defense;
        defense = tempMon;
        // second attack
        offense.attack(defense);
    }
}
