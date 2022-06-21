package com.ja90n.bingo.event.InventoryEvents;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.instance.Game;
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

import java.util.UUID;

public class BingoCardEvent implements Listener {

    private Bingo bingo;


    public BingoCardEvent(Bingo bingo){
        this.bingo = bingo;
         bingo.getGame();
    }

    @EventHandler
    public void onInventoryClick (InventoryClickEvent event){
        if (event.getView().getTitle().equals(ChatColor.LIGHT_PURPLE + "Bingo Card") && event.getCurrentItem() != null){
            event.setCancelled(true);
            ItemStack clickedItem;
            clickedItem = event.getCurrentItem();
            if (clickedItem.getType().equals(Material.PAPER)){
                int number;
                Player player = (Player) event.getWhoClicked();
                number = Integer.parseInt(clickedItem.getItemMeta().getDisplayName());
                if (bingo.getGame().getNumbers().get(number)){
                    if (!bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked().get(bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number))){
                        ItemStack itemStack = new ItemStack(Material.GREEN_CONCRETE);
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + String.valueOf(number));
                        itemStack.setItemMeta(itemMeta);

                        bingo.getGame().getCard(player.getUniqueId()).getInventory().setItem(event.getSlot(),itemStack);

                        bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked().remove( bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number));
                        bingo.getGame().getCard(player.getUniqueId()).getNumbersClicked().put(bingo.getGame().getCard(player.getUniqueId()).numberToIndex(number),true);
                    }
                } else {
                    new WrongClickRunable( bingo.getGame().getCard(player.getUniqueId()).getInventory(),event.getSlot(),String.valueOf(number),bingo);
                }
            } else if (event.getSlot() == 19){
                if (bingo.getGame().getCard(event.getWhoClicked().getUniqueId()).bingoCall()){
                    for (UUID uuid : bingo.getGame().getPlayers().keySet()){
                        Bukkit.getPlayer(uuid).sendMessage(ChatColor.LIGHT_PURPLE + event.getWhoClicked().getName() + " has won the game!");
                    }
                    bingo.getGame().stopGame();
                }
            }
        }
    }
}
