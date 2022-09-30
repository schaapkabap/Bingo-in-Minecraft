package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.ConfigManager;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.gui.HostMenuGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenuEvent implements Listener {

    private Bingo bingo;
    private ConfigManager configManager;

    public MainMenuEvent(Bingo bingo){
        this.bingo = bingo;
        configManager = bingo.getConfigManager();
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(configManager.getChatColor() + configManager.getMessage("main-menu")) && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getSlot() == 24){
                if (event.getWhoClicked().hasPermission("bingo.host")){
                    new HostMenuGui(event.getWhoClicked().getUniqueId(),bingo);
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + configManager.getMessage("you-dont-have-permission"));
                }
            } else if (event.getSlot() == 22) {
                if ( bingo.getGame().getGameState().equals(GameState.OFF)){
                    player.sendMessage(ChatColor.RED + configManager.getMessage("game-is-inactive"));
                } else if ( bingo.getGame().getGameState().equals(GameState.RECRUITING)){
                    if ( bingo.getGame().getPlayers().containsKey(player.getUniqueId())){
                        bingo.getGame().removePlayer(player.getUniqueId());
                    } else {
                        bingo.getGame().addPlayer(player.getUniqueId());
                    }
                } else {
                    if ( bingo.getGame().getPlayers().containsKey(player.getUniqueId())){
                        bingo.getGame().getCard(player.getUniqueId()).openCard();
                    } else {
                        player.sendMessage(ChatColor.RED + configManager.getMessage("game-is-already-active"));
                    }
                }
            }
        }
    }
}