package com.ja90n.bingo.instance;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.util.UpdateJoinLeaveButton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            card.generateCard();
            cards.put(playerUUID,card);
            Bukkit.getPlayer(playerUUID).sendMessage(ChatColor.LIGHT_PURPLE + "The bingo game has been started!");
            card.openCard();
        }
        state = GameState.LINE;
    }

    public void stopGame() {
        for (UUID playerUUID : players.keySet()){
            Bukkit.getPlayer(playerUUID).closeInventory();
            cards.remove(playerUUID);
        }
        cards.clear();
        numbers.clear();
        players.clear();
        state = GameState.OFF;
    }

    public void callNumber (int number){
        numbers.remove(number);
        numbers.put(number,true);
        for (UUID uuid : players.keySet()){
            ItemStack itemStack = new ItemStack(Material.MAP);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Called number: " +ChatColor.WHITE  + number + ChatColor.LIGHT_PURPLE +"!");
            itemStack.setItemMeta(itemMeta);

            getCard(uuid).getInventory().setItem(28,itemStack);
            Bukkit.getPlayer(uuid).sendMessage(ChatColor.LIGHT_PURPLE + "Called number: " +ChatColor.WHITE  + number + ChatColor.LIGHT_PURPLE +"!");
        }
    }

    public Map<Integer, Boolean> getNumbers(){
        return numbers;
    }

    public Map<UUID, Card> getCards(){
        return cards;
    }

    public Card getCard (UUID uuid){
        return cards.getOrDefault(uuid, null);
    }

    public void addPlayer(UUID uuid){
        players.put(uuid,false);
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE + "Main menu")){
                new  UpdateJoinLeaveButton(player.getOpenInventory(),player.getUniqueId(),this);
            }
        }
        Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "You have joined the bingo!");
    }

    public void removePlayer(UUID uuid){
        players.remove(uuid,false);
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE + "Main menu")){
                new  UpdateJoinLeaveButton(player.getOpenInventory(),player.getUniqueId(),this);
            }
        }
        Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "You have leaved the bingo!");
    }

    public GameState getGameState () { return state; }
    public void setGameState (GameState gameState){
        state = gameState;
    }
    public HashMap<UUID,Boolean> getPlayers () { return players;}
}
