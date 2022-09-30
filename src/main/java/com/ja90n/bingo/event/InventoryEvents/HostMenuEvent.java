package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.ConfigManager;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.gui.MainMenuGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class HostMenuEvent implements Listener {

    private Bingo bingo;
    private ConfigManager configManager;

    public HostMenuEvent (Bingo bingo){
        this.bingo = bingo;
        configManager = bingo.getConfigManager();
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(configManager.getChatColor() + configManager.getMessage("host-menu")) && event.getCurrentItem() != null){
            Player player = (Player) event.getWhoClicked();
            event.setCancelled(true);
            if (event.getSlot() == 20){
                if (bingo.getGame().getGameState().equals(GameState.OFF)){
                    bingo.getGame().setGameState(GameState.RECRUITING);
                    ItemStack status = new ItemStack(Material.GREEN_CONCRETE);
                    ItemMeta statusMeta = status.getItemMeta();
                    statusMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("start-game-button"));

                    // Setting lore
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("recruiting-status"));
                    if (!bingo.getGame().getPlayers().isEmpty()){
                        lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                        for (UUID target : bingo.getGame().getPlayers().keySet()){
                            lore.add(ChatColor.BLUE + Bukkit.getPlayer(target).getDisplayName());
                        }
                        statusMeta.setLore(lore);
                    }
                    status.setItemMeta(statusMeta);
                    event.getClickedInventory().setItem(20,status);

                } else if (bingo.getGame().getGameState().equals(GameState.RECRUITING)){
                    if (bingo.getGame().getPlayers().size() >= 2){
                        bingo.getGame().startGame();
                        for (Player target : Bukkit.getOnlinePlayers()){
                            if (target.getOpenInventory().getTitle().equals(configManager.getChatColor() + configManager.getMessage("main-menu"))){
                                target.closeInventory();
                                new MainMenuGui(target.getUniqueId(),bingo);
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + configManager.getMessage("not-enough-players-to-start-the-game-message"));
                    }
                } else {
                    player.sendMessage(ChatColor.RED + configManager.getMessage("you-have-stopped-the-game-message"));
                    for (UUID uuid : bingo.getGame().getPlayers().keySet()){
                        if (!uuid.equals(player.getUniqueId())){
                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.RED + configManager.getMessage("game-has-been-stopped-message"));
                        }
                    }
                    bingo.getGame().stopGame();
                }
            } else if (event.getSlot() == 24) {
                List<Integer> list = new ArrayList<>();
                for (int number : bingo.getGame().getNumbers().keySet()){
                    if (!bingo.getGame().getNumbers().get(number)){
                        list.add(number);
                    }
                }
                if (list.isEmpty()){
                    if (bingo.getGame().getGameState().equals(GameState.OFF) ||
                            bingo.getGame().getGameState().equals(GameState.RECRUITING)){
                        player.sendMessage(ChatColor.RED + configManager.getMessage("game-is-not-active-message"));
                    } else {
                        player.sendMessage(ChatColor.GREEN + configManager.getMessage("all-numbers-are-called-messsge"));
                    }
                } else {
                    int random = list.get(randomNumber(0,list.size()));

                    ItemStack status = new ItemStack(Material.BARRIER);
                    ItemMeta statusMeta = status.getItemMeta();
                    statusMeta.setDisplayName(ChatColor.RED + configManager.getMessage("force-stop-game-button"));

                    // Setting lore
                    List<String> lore = new ArrayList<>();
                    if (bingo.getGame().getGameState().equals(GameState.LINE)){
                        lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("line-status"));
                    } else {
                        lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("full-status"));
                    }
                    if (!bingo.getGame().getPlayers().isEmpty()){
                        lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                        for (UUID target : bingo.getGame().getPlayers().keySet()){
                            lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                        }
                    }
                    statusMeta.setLore(lore);
                    status.setItemMeta(statusMeta);
                    event.getClickedInventory().setItem(20,status);

                    bingo.getGame().callNumber(random);
                }
            } else if (event.getSlot() == 0){
                player.closeInventory();
                new MainMenuGui(player.getUniqueId(),bingo);
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
