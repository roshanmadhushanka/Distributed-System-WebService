package home.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Query {

    private static List<String> queries = new ArrayList<>();

    static {

        // Available queries
        queries.add("Twilight");
        queries.add("Jack");
        queries.add("American Idol");
        queries.add("Happy Feet");
        queries.add("Twilight saga");
        queries.add("Happy Feet");
        queries.add("Happy Feet");
        queries.add("Feet");
        queries.add("Happy Feet");
        queries.add("Twilight");
        queries.add("Windows");
        queries.add("Happy Feet");
        queries.add("Mission Impossible");
        queries.add("Twilight");
        queries.add("Windows 8");
        queries.add("The");
        queries.add("Happy");
        queries.add("Windows 8");
        queries.add("Happy Feet");
        queries.add("Super Mario");
        queries.add("Jack and Jill");
        queries.add("Happy Feet");
        queries.add("Impossible");
        queries.add("Happy Feet");
        queries.add("Turn Up The Music");
        queries.add("Adventures of Tintin");
        queries.add("Twilight saga");
        queries.add("Happy Feet");
        queries.add("Super Mario");
        queries.add("American Pickers");
        queries.add("Microsoft Office 2010");
        queries.add("Twilight");
        queries.add("Modern Family");
        queries.add("Jack and Jill");
        queries.add("Jill");
        queries.add("Glee");
        queries.add("The Vampire Diarie");
        queries.add("King Arthur");
        queries.add("Jack and Jill");
        queries.add("King Arthur");
        queries.add("Windows XP");
        queries.add("Harry Potter");
        queries.add("Feet");
        queries.add("Kung Fu Panda");
        queries.add("Lady Gaga");
        queries.add("Gaga");
        queries.add("Happy Feet");
        queries.add("Twilight");
        queries.add("Hacking");
        queries.add("King");
    }

    public static String getRandomQuery() {
        // Initiate random numner generator
        Random randomGenerator = new Random();

        // Get random index
        int randomIndex = randomGenerator.nextInt(queries.size() - 1);

        return queries.get(randomIndex);
    }

}
