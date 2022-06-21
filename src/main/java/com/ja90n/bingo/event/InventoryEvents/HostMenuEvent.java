package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.gui.HostMenuGui;
import com.ja90n.bingo.instance.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HostMenuEvent implements Listener {

    private Bingo bingo;

    public HostMenuEvent (Bingo bingo){
        this.bingo = bingo;
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Host menu") && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getSlot() == 20){
                if (bingo.getGame().getGameState().equals(GameState.OFF)){
                    bingo.getGame().setGameState(GameState.REQRUITING);
                    ItemStack status = new ItemStack(Material.GREEN_CONCRETE);
                    ItemMeta statusMeta = status.getItemMeta();
                    statusMeta.setDisplayName(ChatColor.GREEN + "Start game");

                    // Setting lore
                    List<String> lore = new ArrayList<>();
                    lore.add("Status: " + ChatColor.GREEN + "Recruiting");
                    lore.add(ChatColor.LIGHT_PURPLE + "Current players:");
                    for (UUID target : bingo.getGame().getPlayers().keySet()){
                        lore.add(ChatColor.BLUE + Bukkit.getPlayer(target).getDisplayName());
                    }
                    statusMeta.setLore(lore);
                    status.setItemMeta(statusMeta);

                    event.getClickedInventory().setItem(20,status);

                } else if ( bingo.getGame().getGameState().equals(GameState.REQRUITING)){
                    bingo.getGame().startGame();
                } else {
                    for (UUID uuid : bingo.getGame().getPlayers().keySet()){
                        Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + "Game has been stopped");
                        bingo.getGame().stopGame();
                    }
                }
            } else if (event.getSlot() == 24) {
                List<Integer> list = new ArrayList<>();
                for (int number : bingo.getGame().getNumbers().keySet()){
                    if (!bingo.getGame().getNumbers().get(number)){
                        list.add(number);
                    }
                }
                if (list.isEmpty()){
                    if (bingo.getGame().getGameState().equals(GameState.OFF) || bingo.getGame().getGameState().equals(GameState.REQRUITING)){
                        player.sendMessage(ChatColor.RED + "The game is not active");
                    } else {
                        player.sendMessage( ChatColor.GREEN + "All numbers are called!");
                    }
                } else {
                    int random = list.get(randomNumber(0,list.size()));

                    ItemStack status = new ItemStack(Material.BARRIER);
                    ItemMeta statusMeta = status.getItemMeta();
                    statusMeta.setDisplayName(ChatColor.RED + "Force stop game");

                    // Setting lore
                    List<String> lore = new ArrayList<>();
                    if (bingo.getGame().getGameState().equals(GameState.LINE)){
                        lore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Line");
                    } else {
                        lore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Full");
                    }
                    if (!bingo.getGame().getPlayers().isEmpty()){
                        lore.add(ChatColor.LIGHT_PURPLE + "Current players:");
                        for (UUID target : bingo.getGame().getPlayers().keySet()){
                            lore.add(ChatColor.BLUE + Bukkit.getPlayer(target).getDisplayName());
                        }
                    }
                    statusMeta.setLore(lore);
                    status.setItemMeta(statusMeta);
                    event.getClickedInventory().setItem(20,status);

                    bingo.getGame().callNumber(random);
                }
            }
        }
    }

    public int randomNumber(int up, int down){
        Random r = new Random();
        int low = up;
        int high = down;
        int result = r.nextInt(high-low) + low;
        return result;
    }
}
