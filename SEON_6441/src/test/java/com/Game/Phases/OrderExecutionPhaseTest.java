package com.Game.Phases;

import static org.junit.Assert.*;

import com.Game.controller.GameController;
import com.Game.model.order.Order;
import com.Game.observer.GameLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.view.GameView;
import com.Game.view.CommandPromptView;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OrderExecutionPhaseTest {
    private OrderExecutionPhase orderExecutionPhase;
    private GameController gameControllerMock;
    private CommandPromptView commandPromptViewMock;
    private Map gameMapMock;
    private Player playerMock1;
    private Player playerMock2;
    private Order orderMock1;
    private Order orderMock2;
    private GameLogger gameLoggerMock;

    @Before
    public void setUp() {
        orderExecutionPhase = new OrderExecutionPhase();
        gameControllerMock = mock(GameController.class);
        commandPromptViewMock = mock(CommandPromptView.class);
        gameMapMock = mock(Map.class);
        playerMock1 = mock(Player.class);
        playerMock2 = mock(Player.class);
        orderMock1 = mock(Order.class);
        orderMock2 = mock(Order.class);
        gameLoggerMock = mock(GameLogger.class);

        // Mock the behavior of nextOrder
        when(playerMock1.nextOrder()).thenReturn(orderMock1, null);
        when(playerMock2.nextOrder()).thenReturn(orderMock2, null);

        GameView gameViewMock = mock(GameView.class);
        when(gameControllerMock.getView()).thenReturn(gameViewMock);
    }

    @Test
    public void testStartPhase_ExecutesOrdersInRoundRobin() {
        List<Player> players = Arrays.asList(playerMock1, playerMock2);
        orderExecutionPhase.StartPhase(gameControllerMock, players, commandPromptViewMock, new String[]{}, gameMapMock);

        // Verify that the orders were executed
        verify(orderMock1, times(1)).execute();
        verify(orderMock2, times(1)).execute();
    }

    @Test
    public void testStartPhase_NoOrders_DoNothing() {
        when(playerMock1.nextOrder()).thenReturn(null);
        when(playerMock2.nextOrder()).thenReturn(null);

        List<Player> players = Arrays.asList(playerMock1, playerMock2);
        orderExecutionPhase.StartPhase(gameControllerMock, players, commandPromptViewMock, new String[]{}, gameMapMock);

        verify(orderMock1, never()).execute();
        verify(orderMock2, never()).execute();
    }

    @Test
    public void testGetNextPhase_ReturnsIssueOrderPhase() {
        assertEquals(PhaseType.ISSUE_ORDER, orderExecutionPhase.getNextPhase());
    }

    @Test
    public void testValidateCommand_ValidCommands() {
        assertTrue(orderExecutionPhase.validateCommand("showmap"));
        assertTrue(orderExecutionPhase.validateCommand("endturn"));
    }

    @Test
    public void testValidateCommand_InvalidCommand() {
        assertFalse(orderExecutionPhase.validateCommand("deploy"));
        assertFalse(orderExecutionPhase.validateCommand("attack"));
    }
}

