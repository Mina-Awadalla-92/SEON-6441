package com.UnitTests;
import com.Game.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Unit tests for the {@link GameEngine} class.
 * This class tests various commands and phases of the game using reflection to access private methods.
 */
public class GameEngineTest {

//    /**
//     * Captures console output for verification in tests.
//     * <p>
//     * This stream is used to store {@code System.out} output during tests, allowing assertions
//     * on printed messages.
//     * </p>
//     */
//    private final ByteArrayOutputStream OUTCONTENT = new ByteArrayOutputStream();
//
//    /**
//     * Stores the original {@code System.out} stream.
//     */
//    private final PrintStream ORIGINALOUT = System.out;
//
//    /**
//     * Redirects System.out to capture console output for verification.
//     */
//    @BeforeEach
//    public void setUpStreams() {
//        System.setOut(new PrintStream(OUTCONTENT));
//    }
//
//    /**
//     * Restores System.out and clears captured output after each test.
//     */
//    @AfterEach
//    public void restoreStreams() {
//        System.setOut(ORIGINALOUT);
//        OUTCONTENT.reset();
//    }
//
//    /**
//     * Tests the {@code handleEditContinent} method.
//     * It prints a confirmation message when a continent is added.
//     *
//     * @throws Exception if reflection access fails.
//     */
//    @Test
//    public void testHandleEditContinentOutput() throws Exception {
//        GameEngine engine = new GameEngine();
//        // Invoke: editcontinent -add Asia 7
//        Method handleEditContinent = GameEngine.class.getDeclaredMethod("handleEditContinent", String[].class);
//        handleEditContinent.setAccessible(true);
//        String[] cmd = {"editcontinent", "-add", "Asia", "7"};
//        handleEditContinent.invoke(engine, (Object) cmd);
//        
//        String output = OUTCONTENT.toString();
//        assertTrue(output.contains("Continent added: Asia"), "Output should confirm that 'Asia' was added.");
//    }
//
//    /**
//     * Tests the {@code handleEditCountry} method for invalid usage.
//     * It prints a usage message when given insufficient arguments.
//     *
//     * @throws Exception if reflection access fails.
//     */
//    @Test
//    public void testHandleEditCountryInvalidUsage() throws Exception {
//        GameEngine engine = new GameEngine();
//        Method handleEditCountry = GameEngine.class.getDeclaredMethod("handleEditCountry", String[].class);
//        handleEditCountry.setAccessible(true);
//        // Provide insufficient arguments for the -add action.
//        String[] cmd = {"editcountry", "-add", "India"};
//        handleEditCountry.invoke(engine, (Object) cmd);
//        
//        String output = OUTCONTENT.toString();
//        assertTrue(output.contains("Usage: editcountry -add countryID continentID"),
//            "Output should display usage instructions for 'editcountry -add'.");
//    }
//
//    /**
//     * Tests the {@code handleAssignCountries} method that distributes all territories among players.
//     * It verifies that after assignment, every territory has an owner and starts with 1 army.
//     *
//     * @throws Exception if reflection access fails.
//     */
//    @Test
//    public void testAssignCountries() throws Exception {
//        GameEngine engine = new GameEngine();
//        
//        // Set up: add a continent and two countries.
//        Method handleEditContinent = GameEngine.class.getDeclaredMethod("handleEditContinent", String[].class);
//        handleEditContinent.setAccessible(true);
//        String[] continentCmd = {"editcontinent", "-add", "Europe", "5"};
//        handleEditContinent.invoke(engine, (Object) continentCmd);
//        
//        Method handleEditCountry = GameEngine.class.getDeclaredMethod("handleEditCountry", String[].class);
//        handleEditCountry.setAccessible(true);
//        String[] countryCmd1 = {"editcountry", "-add", "France", "Europe"};
//        String[] countryCmd2 = {"editcountry", "-add", "Germany", "Europe"};
//        handleEditCountry.invoke(engine, (Object) countryCmd1);
//        handleEditCountry.invoke(engine, (Object) countryCmd2);
//        
//        // Add two players.
//        Method handleGamePlayer = GameEngine.class.getDeclaredMethod("handleGamePlayer", String[].class);
//        handleGamePlayer.setAccessible(true);
//        String[] playerCmd1 = {"gameplayer", "-add", "Alice"};
//        String[] playerCmd2 = {"gameplayer", "-add", "Bob"};
//        handleGamePlayer.invoke(engine, (Object) playerCmd1);
//        handleGamePlayer.invoke(engine, (Object) playerCmd2);
//        
//        // Invoke assigncountries.
//        Method assignCountries = GameEngine.class.getDeclaredMethod("handleAssignCountries");
//        assignCountries.setAccessible(true);
//        assignCountries.invoke(engine);
//        
//        String output = OUTCONTENT.toString();
//        assertTrue(output.contains("Countries assigned to players:"), 
//            "Output should confirm that countries have been assigned.");
//        
//        // Retrieve players list via reflection and check that each territory has an owner and starts with 1 army.
//        Field playersField = GameEngine.class.getDeclaredField("d_players");
//        playersField.setAccessible(true);
//        @SuppressWarnings("unchecked")
//        List<Player> players = (List<Player>) playersField.get(engine);
//        for (Player p : players) {
//            for (Territory t : p.getOwnedTerritories()) {
//                assertNotNull(t.getOwner(), "Each assigned territory should have an owner.");
//                assertEquals(1, t.getNumOfArmies(), "Each assigned territory should start with 1 army.");
//            }
//        }
//    }
//
//    /**
//     * Test that the reinforcement phase calculates reinforcement armies correctly.
//     * For each player, reinforcement = Math.max(3, (territories owned)/3).
//     *
//     * @throws Exception if reflection access fails.
//     */
//    @Test
//    public void testHandleReinforcementOutput() throws Exception {
//        GameEngine engine = new GameEngine();
//        
//        // Set up: add a continent and three countries.
//        Method handleEditContinent = GameEngine.class.getDeclaredMethod("handleEditContinent", String[].class);
//        handleEditContinent.setAccessible(true);
//        String[] continentCmd = {"editcontinent", "-add", "Africa", "4"};
//        handleEditContinent.invoke(engine, (Object) continentCmd);
//        
//        Method handleEditCountry = GameEngine.class.getDeclaredMethod("handleEditCountry", String[].class);
//        handleEditCountry.setAccessible(true);
//        String[] countryCmd1 = {"editcountry", "-add", "Egypt", "Africa"};
//        String[] countryCmd2 = {"editcountry", "-add", "Kenya", "Africa"};
//        String[] countryCmd3 = {"editcountry", "-add", "Nigeria", "Africa"};
//        handleEditCountry.invoke(engine, (Object) countryCmd1);
//        handleEditCountry.invoke(engine, (Object) countryCmd2);
//        handleEditCountry.invoke(engine, (Object) countryCmd3);
//        
//        // Add two players.
//        Method handleGamePlayer = GameEngine.class.getDeclaredMethod("handleGamePlayer", String[].class);
//        handleGamePlayer.setAccessible(true);
//        String[] playerCmd1 = {"gameplayer", "-add", "Alice"};
//        String[] playerCmd2 = {"gameplayer", "-add", "Bob"};
//        handleGamePlayer.invoke(engine, (Object) playerCmd1);
//        handleGamePlayer.invoke(engine, (Object) playerCmd2);
//        
//        // Assign countries.
//        Method assignCountries = GameEngine.class.getDeclaredMethod("handleAssignCountries");
//        assignCountries.setAccessible(true);
//        assignCountries.invoke(engine);
//        
//        // Mark game as started and set phase to MAIN_GAME_PHASE.
//        Field gameStartedField = GameEngine.class.getDeclaredField("d_gameStarted");
//        gameStartedField.setAccessible(true);
//        gameStartedField.set(engine, true);
//        Field currentPhaseField = GameEngine.class.getDeclaredField("d_currentPhase");
//        currentPhaseField.setAccessible(true);
//        currentPhaseField.set(engine, 2); // MAIN_GAME_PHASE
//        
//        // Clear previous output and call reinforcement phase.
//        OUTCONTENT.reset();
//        Method handleReinforcement = GameEngine.class.getDeclaredMethod("handleReinforcement");
//        handleReinforcement.setAccessible(true);
//        handleReinforcement.invoke(engine);
//        
//        String output = OUTCONTENT.toString();
//        assertTrue(output.contains("receives"), "Output should indicate reinforcement armies have been assigned.");
//        
//        // Retrieve players list and check reinforcement count.
//        Field playersField = GameEngine.class.getDeclaredField("d_players");
//        playersField.setAccessible(true);
//        @SuppressWarnings("unchecked")
//        List<Player> players = (List<Player>) playersField.get(engine);
//        for (Player p : players) {
//            int territoriesOwned = p.getOwnedTerritories().size();
//            int expectedReinforcements = Math.max(3, territoriesOwned / 3);
//            assertEquals(expectedReinforcements, p.getNbrOfReinforcementArmies(), 
//                "Reinforcement for " + p.getName() + " should be " + expectedReinforcements);
//        }
//    }
}
