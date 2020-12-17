package com.monbattle.Util;

import com.monbattle.enums.Action;
import com.monbattle.enums.Environment;
import com.monbattle.inventory.Item;
import com.monbattle.monster.Monster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class Util {

    /**
     * @return random Environment from list of all possible environments
     */
    public static Environment randomEnv()  {
        List<Environment> envs = Collections.unmodifiableList(Arrays.asList(Environment.values()));
        return (Environment) randomFromList(envs);
    }

    /**
     * @param monsters List of monsters to choose from
     * @return random Monster from list provided
     */
    public static Monster randomMon(List<Monster> monsters) {
        return (Monster) randomFromList(monsters);
    }

    /**
     * @param list Source data to choose element from
     * @return random element from within the provided list
     */
    private static Object randomFromList (List<?> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    /**
     * @param search String to convert to Action enum
     * @return Action enum matching the input String or NONE
     */
    public static Action searchActionEnum(String search) {
        for (Action action : Action.class.getEnumConstants()) {
            if (action.name().compareToIgnoreCase(search) == 0) {
                return action;
            }
        }
        return Action.NONE;
    }

    /**
     * Converts List of Strings to lowercase in place
     * @param list List of Strings to convert to lowercase
     */
    private static void listToLower(List<String> list) {
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().toLowerCase());
        }
    }

    /**
     * Select an item from a list based on user input
     * @param validItems List of Items that can be selected from
     * @param prompt String to print in case selection did not match items in list
     * @return Item from list based on user input selection
     */
    public static Item validateItemSelection(List<Item> validItems, String prompt) {
        List<String> itemNames = new ArrayList<>();
        itemNames.add("none");
        for(Item i : validItems) {
            itemNames.add(i.getName().toLowerCase());
        }
        String input;
        do {
            System.out.println(prompt);
            input = takeInput();
        } while (!itemNames.contains(input) && !input.equalsIgnoreCase("none"));
        for(Item i : validItems) {
            if(i.getName().equalsIgnoreCase(input))
                return i;
        }
        return null;
    }

    /**
     * Prompt for input and ensure it is numeric
     * @param maxNum int Maximum number that can be selected
     * @param prompt String prompt to print until input is numeric
     * @return int value of input
     */
    public static int validateNumericSelection(int maxNum, String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = takeInput();
        } while(!isNumeric(input) && Integer.parseInt(input) <= maxNum);
        return Integer.parseInt(input);
    }

    /**
     * Check that string is number based on regex
     * @param strNum String to check that is numeric
     * @return boolean true if string is numeric
     */
    private static boolean isNumeric(String strNum) {
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    /**
     * Prompt for user input until input is not empty
     * @param prompt String to print if user input is empty
     * @return String non-empty user input
     */
    public static String validateInput(String prompt) {
        String input;
        do {
            System.out.println(prompt);
            input = takeInput();
        } while (input.isEmpty());
        return input;
    }

    /**
     * Select String from list based on user input
     * @param validInputs List of Strings to select from
     * @param prompt String to print until selection is valid
     * @return String from list based on user input
     */
    public static String validateInput(List<String> validInputs, String prompt) {
        listToLower(validInputs);
        String input;
        do {
            System.out.println(prompt);
            input = takeInput();
        } while (!validInputs.contains(input));
        return input;
    }

    /**
     * Select Action from Set based on user input
     * @param validInputs Set of Actions to select from
     * @param prompt String to print until selection is valid
     * @return Action enum from list based on user input
     */
    public static Action validateInput(Set<Action> validInputs, String prompt) {
        Action input;
        do {
            System.out.println(prompt);
            input = searchActionEnum(takeInput());
        } while (!validInputs.contains(input));
        return input;
    }

    /**
     * Uses BufferedReader to take input from user and validates that it is not empty
     * @return String non-empty user input or empty string
     */
    private static String takeInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine().trim();
            if(!input.isEmpty()) {
                return input;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
