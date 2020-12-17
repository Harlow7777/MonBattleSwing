package com.monbattle.monster;

import com.monbattle.enums.Environment;
import com.monbattle.enums.ExpGroup;
import com.monbattle.enums.Type;
import com.monbattle.growth.GrowthModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class Monster implements Serializable {
    private String name;
    private String nickname;
    private Type type;
    private List<Environment> environments;
    private int HP;
    private int maxHP;
    private int speed;
    private int attack;
    private int defense;
    // Accuracy is percent chance
    private double accuracy;
    // Evasion is percent chance
    private double evasion;
    private int level;
    private int exp;
    private int expGiven;
    private ExpGroup expGroup;
    private GrowthModel growthModel;

    public Monster(String name, Type type, List<Environment> environments, int HP, int speed, int attack, int defense, double accuracy, double evasion, int expGiven, ExpGroup expGroup, GrowthModel growthModel) {
        this.name = name;
        this.nickname = "";
        this.type = type;
        this.environments = environments;
        this.HP = HP;
        this.maxHP = HP;
        this.speed = speed;
        this.attack = attack;
        this.defense = defense;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.level = 1;
        this.exp = 0;
        this.expGiven = expGiven;
        this.expGroup = expGroup;
        this.growthModel = growthModel;
    }

    /**
     * Calculates accuracy, on hit, reduces defense's HP by damage
     * @return boolean; false if defense fainted
     */
    public boolean attack(Monster defense) {
        String n = nickname.isEmpty() ? name : nickname;
        System.out.println(n + " attacks");
        // Calculate accuracy
        if(Math.random() < accuracy && Math.random() > defense.getEvasion()) {
            int damage = calculateDamage(defense);
            defense.setHP(Math.max(defense.getHP() - damage, 0));
            System.out.println((defense.getNickname().isEmpty() ? defense.getName() : defense.getNickname()) + " took " + damage + " damage!");
            return defense.getHP() > 0;
        } else {
            System.out.println(n + " missed!");
        }
        return true;
    }

    /**
     * Calculate damage, determine crit
     * @param defense Monster taking the damage
     * @return damage done to defense
     */
    private int calculateDamage(Monster defense) {
        double typeAdv = Type.typeInteraction(type, defense.getType());
        if(typeAdv == 0.5) {
            System.out.println("It's not very effective");
        } else if(typeAdv == 2) {
            System.out.println("It's super effective!");
        }
        int crit = Math.random() * 10000 < 625 ? 2 : 1;
        if (crit == 2) System.out.println((nickname.isEmpty() ? name : nickname) + " landed a critical hit!");
        double mod = crit * typeAdv;
        return (int) (((double) attack / (double) defense.getDefense()) * level * mod + 1);
    }

    /**
     * Calculate experience required to reach monster's next level based on exp group
     * @return experience required to achieve level up
     */
    public int expToNextLvl() {
        double nextLevelCubed = Math.pow(this.level+1, 3);
        double expAtLvl = 0;
        switch(expGroup) {
            // lvl 100 = 1,000,000, n^3
            case MEDIUM:
                expAtLvl = nextLevelCubed;
                break;
            // lvl 100 = 800,000, (4(n^3))/5
            case FAST:
                expAtLvl = (4*nextLevelCubed)/5;
                break;
            // lvl 100 = 1,250,000, (5(n^3))/4
            case SLOW:
                expAtLvl = (5*nextLevelCubed)/4;
                break;
            default:
                break;
        }
        return (int) expAtLvl;
    }

    /**
     * Calculate experience gained and add to exp
     * @param foundMon Monster that was defeated/captured
     * @param turnCount int; how many turns spent in combat
     */
    public void expGain(Monster foundMon, int turnCount) {
        int exp = ((foundMon.getLevel() * foundMon.getExpGiven()) + turnCount);
        System.out.println(this.nickname + " gained " + exp + " exp");
        this.exp += exp;
        checkLevelUp();
    }

    /**
     * Determine if current exp is enough to reach the next level
     */
    private void checkLevelUp() {
        double expAtLvl = expToNextLvl();
        if(expAtLvl != 0 && this.exp >= expAtLvl) {
            System.out.println("LEVEL UP! " + this.nickname + " is now level " + (this.level+1) + "!");
            System.out.println("Before HP: " + this.maxHP + ", Speed: " + this.speed + ", Attack: " + this.attack + ", Defense: " + this.defense + ", Accuracy: " + this.accuracy + ", Evasion: " + this.evasion);
            this.levelUp(1);
            System.out.println("After HP: " + this.maxHP + ", Speed: " + this.speed + ", Attack: " + this.attack + ", Defense: " + this.defense + ", Accuracy: " + this.accuracy + ", Evasion: " + this.evasion);
            printFullStats();
        }
    }

    /**
     * Increase monster level by 1 and stats using growth model
     * @param iterations number of times to level up
     */
    public void levelUp(int iterations) {
        while(iterations > 0 && this.level < 100) {
            this.level++;
            GrowthModel g = this.growthModel;
            this.maxHP += statInc(g.getHP()) * 10;
            // fully heal
            this.HP = this.maxHP;
            this.speed += statInc(g.getSpeed());
            this.attack += statInc(g.getAttack());
            this.defense += statInc(g.getDefense());
            this.accuracy += statInc(g.getAccuracy()) / 20;
            iterations--;
        }
    }

    /**
     * Determine stat increment based on growth rate
     * @param growthRate number to roll against
     * @return number to increase stat by
     */
    private double statInc(double growthRate) {
        double random = Math.random();
        if(random < growthRate) return 1;
        else if(random == growthRate) return 2;
        else return 0;
    }

    /**
     * Print summarized stats for battle
     */
    public void printBattleStats() {
        System.out.println( this.nickname + "(LVL " + this.level + " " + this.name + "), " +
                            this.type +
                            ", HP: " + this.HP + "/" + this.maxHP +
                            ", XP: " + this.exp + "/" + this.expToNextLvl());
    }

    /**
     * Print summarized battle stats for enemies(omitting exp)
     */
    public void printEnemyBattleStats() {
        System.out.println( this.name + "(LVL " + this.level + "), " +
                            this.type + ", " +
                            "HP: " + this.HP + "/" + this.maxHP + " ");
    }

    /**
     * Print full stats
     */
    public void printFullStats() {
        System.out.println( this.nickname + "(LVL " + this.level + " " + this.name + ") " +
                            this.type + ", HP: " + this.HP + "/" + this.maxHP +
                            ", XP: " + this.exp + "/" + this.expToNextLvl() +
                            ", Speed: " + this.speed +
                            ", Attack: " + this.attack +
                            ", Defense: " + this.defense +
                            ", Accuracy: " + this.accuracy +
                            ", Evasion: " + this.evasion);
    }
}