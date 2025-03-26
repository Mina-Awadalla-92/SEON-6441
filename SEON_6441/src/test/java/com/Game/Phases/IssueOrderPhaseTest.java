package com.Game.Phases; 
@Test
void testStartPhase_PlayerIssuesValidOrders() {
    // Mock dependencies
    GameController mockGameController = Mockito.mock(GameController.class);
    CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
    Map mockGameMap = Mockito.mock(Map.class);
    Player mockPlayer = Mockito.mock(Player.class);

    // Mock player behavior
    Mockito.when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(5, 0); // Start with 5 armies, then 0
    Mockito.when(mockPlayer.getName()).thenReturn("Player1");
    Mockito.when(mockCommandPromptView.getPlayerOrder(Mockito.anyString(), Mockito.anyInt()))
           .thenReturn("deploy Territory1 5", "FINISH"); // Valid order, then finish
    Mockito.when(mockPlayer.issueOrder(Mockito.anyString(), Mockito.eq(mockGameMap), Mockito.anyList()))
           .thenReturn(true); // Order succeeds

    // Mock GameController view behavior
    CommandPromptView mockView = Mockito.mock(CommandPromptView.class);
    Mockito.when(mockGameController.getView()).thenReturn(mockView);

    // Prepare players list
    List<Player> players = List.of(mockPlayer);

    // Execute phase
    IssueOrderPhase issueOrderPhase = new IssueOrderPhase();
    issueOrderPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

    // Verify interactions
    Mockito.verify(mockGameController.getView()).displayPlayerTurn("Player1", 5);
    Mockito.verify(mockGameController.getView()).displayPlayerTerritories(Mockito.anyList(), Mockito.eq(mockPlayer), Mockito.eq(mockGameMap));
    Mockito.verify(mockCommandPromptView).getPlayerOrder("Player1", 5);
    Mockito.verify(mockPlayer).issueOrder("deploy Territory1 5", mockGameMap, players);
}

@Test
void testStartPhase_PlayerIssuesInvalidOrder() {
    // Mock dependencies
    GameController mockGameController = Mockito.mock(GameController.class);
    CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
    Map mockGameMap = Mockito.mock(Map.class);
    Player mockPlayer = Mockito.mock(Player.class);

    // Mock player behavior
    Mockito.when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(5, 5, 0); // Start with 5 armies, stay at 5, then 0
    Mockito.when(mockPlayer.getName()).thenReturn("Player1");
    Mockito.when(mockCommandPromptView.getPlayerOrder(Mockito.anyString(), Mockito.anyInt()))
           .thenReturn("invalid order", "FINISH"); // Invalid order, then finish
    Mockito.when(mockPlayer.issueOrder(Mockito.anyString(), Mockito.eq(mockGameMap), Mockito.anyList()))
           .thenReturn(false); // Order fails

    // Mock GameController view behavior
    CommandPromptView mockView = Mockito.mock(CommandPromptView.class);
    Mockito.when(mockGameController.getView()).thenReturn(mockView);

    // Prepare players list
    List<Player> players = List.of(mockPlayer);

    // Execute phase
    IssueOrderPhase issueOrderPhase = new IssueOrderPhase();
    issueOrderPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

    // Verify interactions
    Mockito.verify(mockGameController.getView()).displayPlayerTurn("Player1", 5);
    Mockito.verify(mockGameController.getView()).displayPlayerTerritories(Mockito.anyList(), Mockito.eq(mockPlayer), Mockito.eq(mockGameMap));
    Mockito.verify(mockCommandPromptView).getPlayerOrder("Player1", 5);
    Mockito.verify(mockPlayer).issueOrder("invalid order", mockGameMap, players);
    Mockito.verify(mockGameController.getView()).displayError("Failed to create order. Please check your command format.");
}

@Test
void testStartPhase_PlayerHasNoReinforcementArmies() {
    // Mock dependencies
    GameController mockGameController = Mockito.mock(GameController.class);
    CommandPromptView mockCommandPromptView = Mockito.mock(CommandPromptView.class);
    Map mockGameMap = Mockito.mock(Map.class);
    Player mockPlayer = Mockito.mock(Player.class);

    // Mock player behavior
    Mockito.when(mockPlayer.getNbrOfReinforcementArmies()).thenReturn(0); // No armies
    Mockito.when(mockPlayer.getName()).thenReturn("Player1");

    // Prepare players list
    List<Player> players = List.of(mockPlayer);

    // Execute phase
    IssueOrderPhase issueOrderPhase = new IssueOrderPhase();
    issueOrderPhase.StartPhase(mockGameController, players, mockCommandPromptView, new String[]{}, mockGameMap);

    // Verify no interactions for issuing orders
    Mockito.verify(mockGameController.getView(), Mockito.never()).displayPlayerTurn(Mockito.anyString(), Mockito.anyInt());
    Mockito.verify(mockCommandPromptView, Mockito.never()).getPlayerOrder(Mockito.anyString(), Mockito.anyInt());
    Mockito.verify(mockPlayer, Mockito.never()).issueOrder(Mockito.anyString(), Mockito.eq(mockGameMap), Mockito.anyList());
}