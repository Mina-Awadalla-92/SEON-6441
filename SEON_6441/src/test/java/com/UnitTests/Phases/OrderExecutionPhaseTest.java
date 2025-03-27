package com.UnitTests.Phases;

import com.Game.Phases.OrderExecutionPhase;
import com.Game.model.Player;
import com.Game.controller.GameController;
import com.Game.model.order.Order;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class OrderExecutionPhaseTest {

    private GameController gameController;
    private CommandPromptView commandPromptView;
    private Map gameMap;
    private List<Player> players;
    private OrderExecutionPhase orderExecutionPhase;
    private GameView gameView;

    @BeforeEach
    public void setUp() {
        gameController = mock(GameController.class);
        commandPromptView = mock(CommandPromptView.class);
        gameMap = mock(Map.class);
        players = new ArrayList<>();
        orderExecutionPhase = new OrderExecutionPhase();
        gameView = mock(GameView.class);

        when(gameController.getView()).thenReturn(gameView);
    }

    @Test
    public void testStartPhase_whenOrdersRemain() {
        // Create mock players with orders
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);

        // Mock player names
        when(player1.getName()).thenReturn("Player1");
        when(player2.getName()).thenReturn("Player2");

        // Mock nextOrder() to return the orders for each player
        when(player1.nextOrder()).thenReturn(order1).thenReturn(null); // Return null after executing one order
        when(player2.nextOrder()).thenReturn(order2).thenReturn(null); // Return null after executing one order

        // Use doNothing() to simulate the void execute() method
        doNothing().when(order1).execute();
        doNothing().when(order2).execute();

        players.add(player1);
        players.add(player2);

        // Call StartPhase
        String[] commandParts = {};
        orderExecutionPhase.StartPhase(gameController, players, commandPromptView, commandParts, gameMap);

        // Verify that the order execution happens and the correct player name is passed to displayExecutingOrder
        verify(gameController.getView(), times(2)).displayExecutingOrder(anyString());
        verify(gameView, times(1)).displayExecutingOrder("Player1");
        verify(gameView, times(1)).displayExecutingOrder("Player2");
        verify(order1, times(1)).execute();
        verify(order2, times(1)).execute();
    }

    @Test
    public void testStartPhase_whenNoOrdersRemain() {
        // Create mock players with no orders
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);

        // Mock nextOrder to return null, indicating no orders for each player
        when(player1.nextOrder()).thenReturn(null);
        when(player2.nextOrder()).thenReturn(null);

        players.add(player1);
        players.add(player2);

        // Call StartPhase
        String[] commandParts = {};
        orderExecutionPhase.StartPhase(gameController, players, commandPromptView, commandParts, gameMap);

        // Verify that displayExecutingOrder is never called (since no orders remain)
        verify(gameController.getView(), never()).displayExecutingOrder(anyString());

        // Verify nextOrder is not called after it returns null (this should happen once)
        verify(player1, times(1)).nextOrder(); // nextOrder should only be called once and return null
        verify(player2, times(1)).nextOrder(); // nextOrder should only be called once and return null
    }

    @Test
    public void testStartPhase_whenSomeOrdersRemain() {
        // Create mock players with some orders
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Order order1 = mock(Order.class);

        // Player 1 has an order; Player 2 has no orders
        when(player1.nextOrder()).thenReturn(order1).thenReturn(null);  // Player 1 will return order1 first, then null
        when(player2.nextOrder()).thenReturn(null);  // Player 2 has no orders

        // Use doNothing() to simulate the void execute() method
        doNothing().when(order1).execute();

        players.add(player1);
        players.add(player2);

        // Call StartPhase
        String[] commandParts = {};
        orderExecutionPhase.StartPhase(gameController, players, commandPromptView, commandParts, gameMap);

        // Verify that player1's order is executed
        verify(gameController.getView(), times(1)).displayExecutingOrder(player1.getName());
        verify(order1, times(1)).execute();

        // Verify that player2's order was not executed (since it returned null)
        verify(player2, times(2)).nextOrder();

        // Verify that player1's nextOrder was called twice (first returning the order, then returning null)
        verify(player1, times(2)).nextOrder();
    }

}
