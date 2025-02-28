package com.Game.model.game;

import com.Game.model.map.Map;
import com.Game.model.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages and stores the current state of the game.
 */
public class GameState {
    // Constants for game phases
    public static final int MAP_EDITING_PHASE = 0;
    public static final int STARTUP_PHASE = 1;
    public static final int MAIN_GAME_PHASE = 2;
    
    private Map d_gameMap;
    private List<Player> d_players;
    private Player d_neutralPlayer;
    private int d_currentPhase;
    private boolean d_gameStarted;
    
    /**
     * Default constructor initializing the game state.
     */
    public GameState() {
        this.d_gameMap = new Map();
        this.d_players = new ArrayList<>();
        this.d_neutralPlayer = new Player("Neutral");
        this.d_currentPhase = MAP_EDITING_PHASE;
        this.d_gameStarted = false;
    }

    // Getters and Setters
    
    public Map getGameMap() {
        return d_gameMap;
    }

    public void setGameMap(Map p_gameMap) {
        this.d_gameMap = p_gameMap;
    }

    public List<Player> getPlayers() {
        return d_players;
    }

    public void setPlayers(List<Player> p_players) {
        this.d_players = p_players;
    }

    public Player getNeutralPlayer() {
        return d_neutralPlayer;
    }

    public void setNeutralPlayer(Player p_neutralPlayer) {
        this.d_neutralPlayer = p_neutralPlayer;
    }

    public int getCurrentPhase() {
        return d_currentPhase;
    }

    public void setCurrentPhase(int p_currentPhase) {
        this.d_currentPhase = p_currentPhase;
    }

    public boolean isGameStarted() {
        return d_gameStarted;
    }

    public void setGameStarted(boolean p_gameStarted) {
        this.d_gameStarted = p_gameStarted;
    }
    
}