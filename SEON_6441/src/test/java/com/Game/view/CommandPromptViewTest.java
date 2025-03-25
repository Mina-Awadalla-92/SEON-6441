package com.Game.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Example tests for CommandPromptView using a ByteArrayInputStream
 * to simulate user input. We also capture System.out if we want to
 * check the printed prompts. 
 */
public class CommandPromptViewTest {

    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        // (Optional) Capture output if you want to check prompt messages
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    void testGetCommand() {
        // Simulate user typing "assigncountries\n"
        String simulatedInput = "assigncountries\n";
        // Redirect System.in to our ByteArrayInputStream
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();

        // The method prints "Enter command: " but we only need to check the return 
        String command = view.getCommand();

        assertEquals("assigncountries", command);
        // Also optionally check the prompt if desired:
        String printed = outContent.toString();
        assertTrue(printed.contains("Enter command:"), 
                   "Should prompt 'Enter command:' before reading input.");
        
        // Clean up
        view.closeScanner();
        System.setIn(System.in); // restore original System.in
    }

    @Test
    void testGetPlayerOrder() {
        // Simulate user typing "deploy territoryName 3\n"
        String simulatedInput = "deploy territoryName 3\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        String order = view.getPlayerOrder("Alice", 5);

        // The user typed "deploy territoryName 3"
        assertEquals("deploy territoryName 3", order);

        // Optionally check prompt text
        String printed = outContent.toString();
        assertTrue(printed.contains("Hi Alice, please enter your next deploy order, or type FINISH:"),
                   "Should prompt user for deploy order.");

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetConfirmationYes() {
        // Simulate typing "y\n"
        String simulatedInput = "y\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        boolean confirmed = view.getConfirmation("Do you confirm?");

        assertTrue(confirmed, "Expected 'y' => yes => true");

        // Check prompt
        String printed = outContent.toString();
        assertTrue(printed.contains("Do you confirm? (y/n):"));

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetConfirmationNo() {
        // Simulate typing "n\n"
        String simulatedInput = "n\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        boolean confirmed = view.getConfirmation("Delete file?");

        assertFalse(confirmed, "Expected 'n' => no => false");

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetFilename() {
        // Simulate "MyMap.map\n"
        String simulatedInput = "MyMap.map\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        String filename = view.getFilename("Enter map name");

        assertEquals("MyMap.map", filename);

        String printed = outContent.toString();
        assertTrue(printed.contains("Enter map name:"), "Should prompt with 'Enter map name:'");

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetInteger_Valid() {
        // Simulate "42\n"
        String simulatedInput = "42\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        int value = view.getInteger("Enter a number");

        assertEquals(42, value, "Expected to parse 42 successfully");

        String printed = outContent.toString();
        assertTrue(printed.contains("Enter a number:"), "Should prompt with 'Enter a number:'");

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetInteger_Invalid() {
        // Simulate "xyz\n"
        String simulatedInput = "xyz\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        int value = view.getInteger("Enter a number");

        assertEquals(-1, value, "Expected -1 for invalid integer input");

        view.closeScanner();
        System.setIn(System.in);
    }

    @Test
    void testGetString() {
        // Simulate "HelloWorld\n"
        String simulatedInput = "HelloWorld\n";
        ByteArrayInputStream inContent = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inContent);

        CommandPromptView view = new CommandPromptView();
        String input = view.getString("Type something");

        assertEquals("HelloWorld", input);

        String printed = outContent.toString();
        assertTrue(printed.contains("Type something:"),
                   "Should prompt 'Type something:' before reading input.");

        view.closeScanner();
        System.setIn(System.in);
    }
}
