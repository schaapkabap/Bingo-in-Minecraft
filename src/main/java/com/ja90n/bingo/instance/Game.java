package com.ja90n.bingo.instance;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.GameState;

import java.util.*;

public class Game {

    private GameState state;
    private Bingo bingo;
    private HashMap <UUID,Boolean> players;
    private HashMap <UUID,Card> cards;
    private HashMap <Integer,Boolean> numbers;

    public Game(Bingo bingo){
        this.bingo = bingo;
        this.state = GameState.OFF;
        players = new HashMap<>();
        cards = new HashMap<>();
        numbers = new HashMap<>();
    }

    public void startGame (){
        for (int i = 75; i >= 1; i--){
            numbers.put(i,false);
        }
        for (UUID playerUUID : players.keySet()){
            Card card = new Card(playerUUID,this,bingo);
            cards.put(playerUUID,card);
        }
        state = GameState.LINE;
    }

    public void stopGame() {
        for (UUID playerUUID : players.keySet()){
            cards.remove(playerUUID);
        }
        state = GameState.OFF;
    }

    public Map<Integer, Boolean> getNumbers(){
        return numbers;
    }

    public Map<UUID, Card> getCards(){
        return cards;
    }

    public Card getCard (UUID uuid){
        if (cards.containsValue(uuid)){
            return cards.get(uuid);
        } else {
            return null;
        }
    }

    public void addPlayer(UUID uuid){
        players.put(uuid,false);
    }

    public GameState getGameState () { return state; }
    public HashMap<UUID,Boolean> getPlayers () { return players;}
}
