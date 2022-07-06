package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.gui.HostMenuGui;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenuEvent implements Listener {

    private Bingo bingo;

    public MainMenuEvent(Bingo bingo){
        this.bingo = bingo;
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Main menu") && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getSlot() == 24){
                if (event.getWhoClicked().hasPermission("bingo.host")){
                    new HostMenuGui(event.getWhoClicked().getUniqueId(),bingo);
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "You don't have permission");
                }
            } else if (event.getSlot() == 22) {
                if ( bingo.getGame().getGameState().equals(GameState.OFF)){
                    player.sendMessage(ChatColor.RED + "You can not join right now, game is inactive");
                } else if ( bingo.getGame().getGameState().equals(GameState.REQRUITING)){
                    if ( bingo.getGame().getPlayers().containsKey(player.getUniqueId())){
                        bingo.getGame().removePlayer(player.getUniqueId());
                    } else {
                        bingo.getGame().addPlayer(player.getUniqueId());
                    }
                } else {
                    if ( bingo.getGame().getPlayers().containsKey(player.getUniqueId())){
                        bingo.getGame().getCard(player.getUniqueId()).openCard();
                    } else {
                        player.sendMessage(ChatColor.RED + "You can't join now, game is already active.");
                    }
                }
            }
        }
    }
}