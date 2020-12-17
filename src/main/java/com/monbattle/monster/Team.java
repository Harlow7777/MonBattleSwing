package com.monbattle.monster;

import com.monbattle.Util.Util;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
public class Team implements Serializable {

    private Collection<Monster> team = new ArrayList<>();

    /**
     * Get monster from team with matching nickname
     * @param nickname String nickname to search team
     * @return Monster with nickname matching provided nickname
     * @throws NoSuchElementException if no Monster is found by that nickname
     */
    public Monster getMemberByNickname(String nickname) throws NoSuchElementException{
        for(Monster m : team) {
            if(m.getNickname().equalsIgnoreCase(nickname)) {
                return m;
            }
        }
        throw new NoSuchElementException("Monster named " + nickname + " is not in your team");
    }

    /**
     * @return int size of team
     */
    public int getSize() {
        return this.team.size();
    }

    /**
     * @return Monster first monster on team aka team lead
     */
    public Monster getFirst() {
        return this.team.iterator().next();
    }

    /**
     * @return true if any team member has HP > 0
     */
    public boolean hasFitForBattle() {
        for(Monster m : team) {
            if(m.getHP() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Fully heal all team members to their MaxHP
     */
    public void healTeam() {
        for(Monster m : team) {
            m.setHP(m.getMaxHP());
        }
        System.out.println("Team fully healed");
    }

    /**
     * @return highest level of all monsters from team
     */
    public int highestLvl() {
        int highestLvl = 0;
        for(Monster m : team) {
            highestLvl = Math.max(m.getLevel(), highestLvl);
        }
        return highestLvl;
    }

    /**
     * @return an average of all monster levels from team
     */
    public int averageLvl() {
        int totalLvl = 0;
        for(Monster m : team) {
            totalLvl += m.getLevel();
        }
        return totalLvl / getSize();
    }

    /**
     * @return List of all team member's nicknames
     */
    public List<String> memberNicknames(){
        List<String> names = new ArrayList<>();
        for(Monster m : team) {
            names.add(m.getNickname().toLowerCase());
        }
        return names;
    }

    /**
     * Prompt for selection based on team names
     * @return Monster, selected by its name
     */
    public Monster selectTeamMember() {
        for(Monster m : this.getTeam()) {
            m.printFullStats();
        }
        return getMemberByNickname(Util.validateInput(memberNicknames(),
                "Choose monster " + memberNicknames()));
    }

    /**
     * Displays team members and prompts for a selection
     * @return selected team member
     */
    public Monster selectTeamLead() {
        Monster choice = selectTeamMember();
        if(choice.getHP() == 0) {
            System.out.println("Monster with 0 HP can't lead");
            selectTeamLead();
        }
        return choice;
    }

    /**
     * Add member to team if there are less than 6 current members;
     * @param member Monster to be added to team
     * @return response from team.add(member)
     */
    public boolean addMember(Monster member) {
        if(this.team.size() < 6) {
            return this.team.add(member);
        } else {
            System.out.println("Team is full");
            if (Util.validateInput(Arrays.asList("y", "n"), "Would you like to switch out monsters?(Y or N) ").equalsIgnoreCase("y")) {
                replaceMember(selectTeamLead(), member);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Remove monster, if present
     * @param member Monster to be removed
     */
    public void removeMember(Monster member) {
        if(this.team.contains(member)) {
            this.team.remove(member);
        } else {
            throw new NoSuchElementException(member.getName() + " not found in current team");
        }
    }

    /**
     * Remove currMember if present and add newMember.
     * @param currMember Monster to be replaced
     * @param newMember Monster to be added
     */
    public void replaceMember(Monster currMember, Monster newMember) {
        this.removeMember(currMember);
        this.addMember(newMember);
    }
}
