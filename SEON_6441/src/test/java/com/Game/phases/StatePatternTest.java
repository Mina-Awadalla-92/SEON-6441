package com.Game.phases;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.Phases.IssueOrderPhase;
import com.Game.Phases.MapEditorPhase;
import com.Game.Phases.OrderExecutionPhase;
import com.Game.Phases.Phase;
import com.Game.Phases.PhaseType;
import com.Game.Phases.StartupPhase;
import com.Game.controller.GameController;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.CommandPromptView;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for the State pattern implementation.
 * Tests the phase transitions and phase-specific behaviors.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatePatternTest {
    
    @Mock private GameController mockGameController;
    @Mock private Map mockMap;
    @Mock private CommandPromptView mockCommandPromptView;
    
    private List<Player> players;
    private Phase mapEditorPhase;
    private Phase startupPhase;
    private Phase issueOrderPhase;
    private Phase orderExecutionPhase;
    
    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Create the phases
        mapEditorPhase = new MapEditorPhase();
        startupPhase = new StartupPhase();
        issueOrderPhase = new IssueOrderPhase();
        orderExecutionPhase = new OrderExecutionPhase();
        
        // Create a test list of players
        players = new ArrayList<>();
        players.add(new Player("TestPlayer1"));
        players.add(new Player("TestPlayer2"));
    }
    
    /**
     * Tests the phase transition mechanism of the State pattern.
     */
    @Test
    public void testPhaseTransitions() {
        // Test transition from map editor to startup
        Phase newPhase = mapEditorPhase.setPhase(PhaseType.STARTUP);
        assertTrue("Transition should create a StartupPhase", newPhase instanceof StartupPhase);
        
        // Test transition from startup to issue order
        newPhase = startupPhase.setPhase(PhaseType.ISSUE_ORDER);
        assertTrue("Transition should create an IssueOrderPhase", newPhase instanceof IssueOrderPhase);
        
        // Test transition from issue order to order execution
        newPhase = issueOrderPhase.setPhase(PhaseType.ORDER_EXECUTION);
        assertTrue("Transition should create an OrderExecutionPhase", newPhase instanceof OrderExecutionPhase);
        
        // Test transition from order execution back to issue order
        newPhase = orderExecutionPhase.setPhase(PhaseType.ISSUE_ORDER);
        assertTrue("Transition should create an IssueOrderPhase", newPhase instanceof IssueOrderPhase);
    }
    
    /**
     * Tests that each phase validates commands correctly.
     */
    @Test
    public void testCommandValidation() {
        // Map Editor Phase
        assertTrue("'editmap' should be valid in MapEditorPhase", mapEditorPhase.validateCommand("editmap"));
        assertTrue("'showmap' should be valid in MapEditorPhase", mapEditorPhase.validateCommand("showmap"));
        assertFalse("'issueorder' should not be valid in MapEditorPhase", mapEditorPhase.validateCommand("issueorder"));
        
        // Startup Phase
        assertTrue("'gameplayer' should be valid in StartupPhase", startupPhase.validateCommand("gameplayer"));
        assertTrue("'assigncountries' should be valid in StartupPhase", startupPhase.validateCommand("assigncountries"));
        assertFalse("'executeorders' should not be valid in StartupPhase", startupPhase.validateCommand("executeorders"));
        
        // Issue Order Phase
        assertTrue("'deploy' should be valid in IssueOrderPhase", issueOrderPhase.validateCommand("deploy"));
        assertTrue("'advance' should be valid in IssueOrderPhase", issueOrderPhase.validateCommand("advance"));
        assertFalse("'executeorders' should not be valid in IssueOrderPhase", issueOrderPhase.validateCommand("executeorders"));
        
        // Order Execution Phase
        assertTrue("'showmap' should be valid in OrderExecutionPhase", orderExecutionPhase.validateCommand("showmap"));
        assertTrue("'endturn' should be valid in OrderExecutionPhase", orderExecutionPhase.validateCommand("endturn"));
        assertFalse("'deploy' should not be valid in OrderExecutionPhase", orderExecutionPhase.validateCommand("deploy"));
    }
    
    /**
     * Tests the getNextPhase method for each phase.
     */
    @Test
    public void testGetNextPhase() {
        assertEquals("MapEditorPhase should transition to STARTUP", PhaseType.STARTUP, mapEditorPhase.getNextPhase());
        assertEquals("StartupPhase should transition to ISSUE_ORDER", PhaseType.ISSUE_ORDER, startupPhase.getNextPhase());
        assertEquals("IssueOrderPhase should transition to ORDER_EXECUTION", PhaseType.ORDER_EXECUTION, issueOrderPhase.getNextPhase());
        assertEquals("OrderExecutionPhase should transition to ISSUE_ORDER", PhaseType.ISSUE_ORDER, orderExecutionPhase.getNextPhase());
    }
    
    /**
     * Tests that the StartPhase method is implemented by each phase.
     * This test only checks that the method doesn't throw exceptions, as the actual implementation
     * would require more extensive integration testing with real components.
     */
    @Test
    public void testStartPhaseImplementation() {
        try {
            // These tests are minimal since a full test would require complex integration testing
            
            // We're just ensuring the method is implemented and doesn't throw exceptions with mocks
            String[] commandParts = new String[]{"testCommand"};
            
            mapEditorPhase.StartPhase(mockGameController, players, mockCommandPromptView, commandParts, mockMap);
            startupPhase.StartPhase(mockGameController, players, mockCommandPromptView, commandParts, mockMap);
            issueOrderPhase.StartPhase(mockGameController, players, mockCommandPromptView, commandParts, mockMap);
            orderExecutionPhase.StartPhase(mockGameController, players, mockCommandPromptView, commandParts, mockMap);
            
            // No assertions - this test passes if no exceptions are thrown
        } catch (Exception e) {
            fail("StartPhase implementation threw an exception: " + e.getMessage());
        }
    }
}