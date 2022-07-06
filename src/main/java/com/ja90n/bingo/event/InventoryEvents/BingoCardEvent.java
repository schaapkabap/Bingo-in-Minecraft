package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.gui.MainMenuGui;
import com.ja90n.bingo.runable.WrongClickRunable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class BingoCardEvent implements Listener {

    private Bingo bingo;
    private ItemStack clickedItem;

    public BingoCardEvent(Bingo bingo){
        this.bingo = bingo;
         bingo.getGame();
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Bingo Card") && event.getCurrentItem() != null){
            event.setCancelled(true);
            clickedItem = event.getCurrentItem();
            if (clickedItem.getType().equals(Material.PAPER) && event.getSlot() <= 54){
                int number;
                Player player = (Player) event.getWhoClicked();
                number = Integer.parseInt(clickedItem.getItemMeta().getDisplayName());
                if (bingo.getGame().getNumbers().get(number)){
                    if (!bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked()
                            .get(bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number))){
                        ItemStack itemStack = new ItemStack(Material.GREEN_CONCRETE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + String.valueOf(number));
                        itemStack.setItemMeta(itemMeta);

                        bingo.getGame().getCard(player.getUniqueId()).getInventory().setItem(event.getSlot(),itemStack);

                        bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked()
                                .remove(bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number));
                        bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked()
                                .put(bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number),true);
                    }
                } else {
                    new WrongClickRunable(
                            bingo.getGame().getCard(player.getUniqueId()).getInventory(),
                            event.getSlot(),String.valueOf(number),bingo);
                }
            } else if (event.getSlot() == 19){
                for (Player player : Bukkit.getOnlinePlayers()){
                    if (player.getOpenInventory().getTitle().equals(ChatColor.LIGHT_PURPLE + "Host menu")){
                        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
                        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(event.getWhoClicked().getUniqueId()));
                        skullMeta.setDisplayName(ChatColor.WHITE + event.getWhoClicked().getName() +
                                ChatColor.LIGHT_PURPLE + " has called a bingo!");
                        itemStack.setItemMeta(skullMeta);

                        player.getOpenInventory().setItem(22,itemStack);
                    }
                }
                if (bingo.getGame().getCard(event.getWhoClicked().getUniqueId()).bingoCall()){
                    if (bingo.getGame().getGameState().equals(GameState.LINE)){
                        for (UUID uuid : bingo.getGame().getPlayers().keySet()){
                            Bukkit.getPlayer(uuid).sendMessage(
                                    event.getWhoClicked().getName() + ChatColor.LIGHT_PURPLE + " has completed a row!");
                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.LIGHT_PURPLE + "Moving on to full card!");
                        }
                        bingo.getGame().setGameState(GameState.FULL);
                    } else {
                        for (UUID uuid : bingo.getGame().getPlayers().keySet()){
                            Bukkit.getPlayer(uuid).sendMessage(event.getWhoClicked().getName()
                                    + ChatColor.LIGHT_PURPLE + " has completed the full card!");
                            Bukkit.getPlayer(uuid).sendMessage(ChatColor.LIGHT_PURPLE + "Stopping the game!");
                            Bukkit.getPlayer(uuid).sendTitle(event.getWhoClicked().getName() +
                                    ChatColor.LIGHT_PURPLE + " has completed the full card!",
                                    ChatColor.LIGHT_PURPLE + "Stopping the game!");
                        }
                        bingo.getGame().stopGame();
                    }
                }
            } else if (event.getSlot() == 0){
                Player player = (Player) event.getWhoClicked();
                player.closeInventory();
                new MainMenuGui(player.getUniqueId(),bingo);
            }
        }
    }
}
