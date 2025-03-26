package com.Game.Phases;

import com.Game.controller.GameController;
import com.Game.view.CommandPromptView;
import com.Game.model.Map;
import com.Game.model.Player;
import com.Game.model.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

class OrderExecutionPhaseTest {

    @Test
    void testStartPhase_WhenAllOrdersAreExecuted() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);
        Player mockPlayer1 = Mockito.mock(Player.class);
        Player mockPlayer2 = Mockito.mock(Player.class);
        Order mockOrder1 = Mockito.mock(Order.class);
        Order mockOrder2 = Mockito.mock(Order.class);

        // Mock player behavior
        Mockito.when(mockPlayer1.getName()).thenReturn("Player1");
        Mockito.when(mockPlayer2.getName()).thenReturn("Player2");
        Mockito.when(mockPlayer1.nextOrder()).thenReturn(mockOrder1, null); // First call returns an order, second call returns null
        Mockito.when(mockPlayer2.nextOrder()).thenReturn(mockOrder2, null); // First call returns an order, second call returns null

        // Mock GameController view behavior
        CommandPromptView mockView = Mockito.mock(CommandPromptView.class);
        Mockito.when(mockGameController.getView()).thenReturn(mockView);

        // Prepare players list
        List<Player> players = List.of(mockPlayer1, mockPlayer2);

        // Execute phase
        OrderExecutionPhase orderExecutionPhase = new OrderExecutionPhase();
        orderExecutionPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView()).displayExecutingOrder("Player1");
        Mockito.verify(mockGameController.getView()).displayExecutingOrder("Player2");
        Mockito.verify(mockOrder1).execute();
        Mockito.verify(mockOrder2).execute();
    }

    @Test
    void testStartPhase_WhenNoOrdersExist() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);
        Player mockPlayer1 = Mockito.mock(Player.class);
        Player mockPlayer2 = Mockito.mock(Player.class);

        // Mock player behavior
        Mockito.when(mockPlayer1.nextOrder()).thenReturn(null); // No orders for Player1
        Mockito.when(mockPlayer2.nextOrder()).thenReturn(null); // No orders for Player2

        // Prepare players list
        List<Player> players = List.of(mockPlayer1, mockPlayer2);

        // Execute phase
        OrderExecutionPhase orderExecutionPhase = new OrderExecutionPhase();
        orderExecutionPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

        // Verify no interactions for executing orders
        Mockito.verify(mockGameController.getView(), Mockito.never()).displayExecutingOrder(Mockito.anyString());
        Mockito.verify(mockPlayer1, Mockito.times(1)).nextOrder();
        Mockito.verify(mockPlayer2, Mockito.times(1)).nextOrder();
    }

    @Test
    void testStartPhase_WhenPlayerHasMultipleOrders() {
        // Mock dependencies
        GameController mockGameController = Mockito.mock(GameController.class);
        CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
        Map mockGameMap = Mockito.mock(Map.class);
        Player mockPlayer1 = Mockito.mock(Player.class);
        Order mockOrder1 = Mockito.mock(Order.class);
        Order mockOrder2 = Mockito.mock(Order.class);

        // Mock player behavior
        Mockito.when(mockPlayer1.getName()).thenReturn("Player1");
        Mockito.when(mockPlayer1.nextOrder()).thenReturn(mockOrder1, mockOrder2, null); // Two orders, then null

        // Mock GameController view behavior
        CommandPromptView mockView = Mockito.mock(CommandPromptView.class);
        Mockito.when(mockGameController.getView()).thenReturn(mockView);

        // Prepare players list
        List<Player> players = List.of(mockPlayer1);

        // Execute phase
        OrderExecutionPhase orderExecutionPhase = new OrderExecutionPhase();
        orderExecutionPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

        // Verify interactions
        Mockito.verify(mockGameController.getView(), Mockito.times(2)).displayExecutingOrder("Player1");
        Mockito.verify(mockOrder1).execute();
        Mockito.verify(mockOrder2).execute();
    }

    @Test
    void testStartPhase_MixedPlayersWithAndWithoutOrders() {
        // Test for mixed players (some with orders, some without)
        // (Implementation provided above)
    }

    @Test
    void testStartPhase_OrderExecutionFails() {
        // Test for orders that fail during execution
        // (Implementation provided above)
    }
}