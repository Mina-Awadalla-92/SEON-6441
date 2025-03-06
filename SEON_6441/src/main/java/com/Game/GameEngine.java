package com.Game;

import java.util.Scanner;

/**
 * The {@code GameEngine} class is responsible for managing the game's main loop and handling user commands.
 * It loads and edits maps, manages continents, countries, and neighbors, and validates and saves maps.
 */
public class GameEngine {

    /**
     * The current game map being used.
     */
    private static Map d_gameMap;

    /**
     * The map loader responsible for loading and validating maps.
     */
    private static MapLoader d_mapLoader;

    /**
     * The file path of the current map.
     */
    private static String d_mapFilePath;

    /**
     * Constructs a new {@code GameEngine} instance and initializes the game map and map loader.
     */
    public GameEngine() {
        d_gameMap = new Map();
        d_mapLoader = new MapLoader();
    }

    /**
     * Starts the main game loop, accepting and processing user commands.
     */
    public void startGame() {
        System.out.println("Welcome to Warzone Game!");

        Scanner scanner = new Scanner(System.in);
        boolean isMapLoaded = false;

        while (true) {
            System.out.print("Enter command: ");
            String input = scanner.nextLine().trim();
            String[] commandParts = input.split("\\s+");

            String command = commandParts[0];

            if (command.equals("exit"))
            {
                System.out.println("Exiting the program.");
                System.exit(0); // Exit the program
            }
            else if (!isMapLoaded && !command.equals("editmap"))
            {
                System.out.println("You must load/edit a map first using the 'load' command.");
            }
            else
            {
                switch (command) {
                    // editcontinent -add continentID continentValue
                    // editcontinent -remove continentID
                    case "editcontinent":
                        handleEditContinent(commandParts);
                        break;
                    // editcountry -add countryID continentID
                    // editcountry -remove countryID
                    case "editcountry":
                        handleEditCountry(commandParts);
                        break;
                    // ditneighbor -add countryID neighborCountryID
                    // ditneighbor -remove countryID neighborCountryID
                    case "editneighbor":
                        handleEditNeighbor(commandParts);
                        break;
                    // showmap
                    case "showmap":
                        handleShowMap();
                        break;
                    // savemap filename
                    case "savemap":
                        handleSaveMap(commandParts);
                        break;
                    // editmap filename
                    case "editmap":
                        isMapLoaded = true;
                        handleEditMap(commandParts);
                        break;
                    // validatemap
                    case "validatemap":
                        handleValidateMap();
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                }
            }
        }
    }

    /**
     * Handles the command to edit continents, allowing addition or removal.
     * @param p_commandParts The command split into parts.
     */
    private static void handleEditContinent(String[] p_commandParts) {
        if (p_commandParts.length == 4) {
            String action = p_commandParts[1];
            String continentID = p_commandParts[2];
            int continentValue = Integer.parseInt(p_commandParts[3]);

            if (action.equals("-add")) {
                d_gameMap.addContinent(continentID, continentValue);
                System.out.println("Continent added: " + continentID);
            } else if (action.equals("-remove")) {
                d_gameMap.removeContinent(continentID);
                System.out.println("Continent removed: " + continentID);
            } else {
                System.out.println("Invalid action for editcontinent.");
            }
        } else {
            System.out.println("Usage: editcontinent -add continentID continentValue -remove continentID");
        }
    }

    /**
     * Handles the command to edit countries, allowing addition or removal.
     * @param p_commandParts The command split into parts.
     */
    private static void handleEditCountry(String[] p_commandParts) {
        if (p_commandParts.length == 4) {
            String action = p_commandParts[1];
            String countryID = p_commandParts[2];
            String continentID = p_commandParts[3];

            if (action.equals("-add")) {
                d_gameMap.addCountry(countryID, continentID);
                System.out.println("Country added: " + countryID);
            } else if (action.equals("-remove")) {
                d_gameMap.removeCountry(countryID);
                System.out.println("Country removed: " + countryID);
            } else {
                System.out.println("Invalid action for editcountry.");
            }
        } else {
            System.out.println("Usage: editcountry -add countryID continentID -remove countryID");
        }
    }

    /**
     * Handles the command to edit neighbors, allowing addition or removal.
     * @param p_commandParts The command split into parts.
     */
    private static void handleEditNeighbor(String[] p_commandParts) {
        if (p_commandParts.length == 4) {
            String action = p_commandParts[1];
            String countryID = p_commandParts[2];
            String neighborCountryID = p_commandParts[3];

            if (action.equals("-add")) {
                d_gameMap.addNeighbor(countryID, neighborCountryID);
                System.out.println("Neighbor added between: " + countryID + " and " + neighborCountryID);
            } else if (action.equals("-remove")) {
                d_gameMap.removeNeighbor(countryID, neighborCountryID);
                System.out.println("Neighbor removed between: " + countryID + " and " + neighborCountryID);
            } else {
                System.out.println("Invalid action for editneighbor.");
            }
        } else {
            System.out.println("Usage: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID");
        }
    }

    /**
     * Displays the current map.
     */
    private static void handleShowMap() {
        System.out.println(d_mapLoader.getLoadedMap());
    }

    /**
     * Saves the current map to a file.
     * @param p_commandParts The command split into parts.
     */
    private static void handleSaveMap(String[] p_commandParts) {
        if (p_commandParts.length == 2) {
            String filename = p_commandParts[1];
            d_gameMap.saveToFile(filename);
        } else {
            System.out.println("Usage: savemap filename");
        }
    }

    /**
     * Loads or creates a new map based on the specified file.
     * @param p_commandParts The command split into parts.
     */
    private static void handleEditMap(String[] p_commandParts)
    {
        if (p_commandParts.length == 2) {
            d_mapFilePath = p_commandParts[1];
            d_mapLoader.resetLoadedMap();

            boolean isMapValid = d_mapLoader.isValid(d_mapFilePath);
            if(isMapValid)
            {
                d_mapLoader.read(d_mapFilePath);
                System.out.println(d_mapFilePath + " is loaded successfully.");
            }
            else
            {
                System.out.println("The specified map is not exist, a new map is created.");
            }


            // gameMap = mapLoader.getLoadedMap();
            //gameMap.getLoadedMap(filename);
            //  System.out.println("Map loaded from file: " + filename);
        } else {
            System.out.println("Usage: editmap filename");
        }
    }

    /**
     * Validates the current map to ensure it meets game requirements.
     */
    private static void handleValidateMap() {
        // Call your map validation logic here
        // For example, you could add a method to the `Map` class to validate it
        if (d_mapLoader.isValid(d_mapFilePath)) {
            System.out.println("Map is valid.");
        } else {
            System.out.println("Map is invalid.");
        }

    }
}
