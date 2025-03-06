package com.Game;

import java.util.Scanner;

public class GameEngine {

    private static Map gameMap;
    private static MapLoader mapLoader;
    private static String mapFilePath;

    public GameEngine() {
        gameMap = new Map();
        mapLoader = new MapLoader();
    }

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

    private static void handleEditContinent(String[] commandParts) {
        if (commandParts.length == 4) {
            String action = commandParts[1];
            String continentID = commandParts[2];
            int continentValue = Integer.parseInt(commandParts[3]);

            if (action.equals("-add")) {
                gameMap.addContinent(continentID, continentValue);
                System.out.println("Continent added: " + continentID);
            } else if (action.equals("-remove")) {
                gameMap.removeContinent(continentID);
                System.out.println("Continent removed: " + continentID);
            } else {
                System.out.println("Invalid action for editcontinent.");
            }
        } else {
            System.out.println("Usage: editcontinent -add continentID continentValue -remove continentID");
        }
    }

    private static void handleEditCountry(String[] commandParts) {
        if (commandParts.length == 4) {
            String action = commandParts[1];
            String countryID = commandParts[2];
            String continentID = commandParts[3];

            if (action.equals("-add")) {
                gameMap.addCountry(countryID, continentID);
                System.out.println("Country added: " + countryID);
            } else if (action.equals("-remove")) {
                gameMap.removeCountry(countryID);
                System.out.println("Country removed: " + countryID);
            } else {
                System.out.println("Invalid action for editcountry.");
            }
        } else {
            System.out.println("Usage: editcountry -add countryID continentID -remove countryID");
        }
    }

    private static void handleEditNeighbor(String[] commandParts) {
        if (commandParts.length == 4) {
            String action = commandParts[1];
            String countryID = commandParts[2];
            String neighborCountryID = commandParts[3];

            if (action.equals("-add")) {
                gameMap.addNeighbor(countryID, neighborCountryID);
                System.out.println("Neighbor added between: " + countryID + " and " + neighborCountryID);
            } else if (action.equals("-remove")) {
                gameMap.removeNeighbor(countryID, neighborCountryID);
                System.out.println("Neighbor removed between: " + countryID + " and " + neighborCountryID);
            } else {
                System.out.println("Invalid action for editneighbor.");
            }
        } else {
            System.out.println("Usage: editneighbor -add countryID neighborCountryID -remove countryID neighborCountryID");
        }
    }

    private static void handleShowMap() {
        System.out.println(mapLoader.getLoadedMap());
    }

    private static void handleSaveMap(String[] commandParts) {
        if (commandParts.length == 2) {
            String filename = commandParts[1];
            gameMap.saveToFile(filename);
        } else {
            System.out.println("Usage: savemap filename");
        }
    }

    private static void handleEditMap(String[] commandParts) {
        if (commandParts.length == 2) {
            mapFilePath = commandParts[1];
            mapLoader.resetLoadedMap();

            boolean isMapValid = mapLoader.isValid(mapFilePath);
            if(isMapValid)
            {
                mapLoader.read(mapFilePath);
                System.out.println(mapFilePath + " is loaded successfully.");
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

    private static void handleValidateMap() {
        // Call your map validation logic here
        // For example, you could add a method to the `Map` class to validate it
        if (mapLoader.isValid(mapFilePath)) {
            System.out.println("Map is valid.");
        } else {
            System.out.println("Map is invalid.");
        }

    }
}
