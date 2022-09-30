package com.ja90n.bingo.util;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.ConfigManager;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.instance.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UpdateJoinLeaveButton {

    public UpdateJoinLeaveButton(InventoryView inventory, UUID uuid, Game game, Bingo bingo){
        ConfigManager configManager = bingo.getConfigManager();

        // Join / leave button
        if (game.getPlayers().containsKey(uuid)){
            if (game.getGameState().equals(GameState.OFF)){
                ItemStack broken = new ItemStack(Material.CHAIN);
                ItemMeta brokenMeta = broken.getItemMeta();
                brokenMeta.setDisplayName(ChatColor.RED + "Send a picture of this for 1 euro to Ja90n (one time use)");
                broken.setItemMeta(brokenMeta);

                inventory.setItem(22,broken);
            } else if (game.getGameState().equals(GameState.RECRUITING)){
                ItemStack leave = new ItemStack(Material.BARRIER);
                ItemMeta leaveMeta = leave.getItemMeta();
                leaveMeta.setDisplayName(ChatColor.RED + configManager.getMessage("leave-button"));

                // Setting lore
                if (!game.getPlayers().isEmpty()) {
                    List<String> lore = new ArrayList<>();
                    lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                    for (UUID target : game.getPlayers().keySet()) {
                        lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                    }
                    leaveMeta.setLore(lore);
                }

                leave.setItemMeta(leaveMeta);

                inventory.setItem(22,leave);
            } else {
                ItemStack card = new ItemStack(Material.PAPER);
                ItemMeta cardMeta = card.getItemMeta();
                cardMeta.setDisplayName(configManager.getChatColor() + configManager.getMessage("bingo-card"));
                card.setItemMeta(cardMeta);

                inventory.setItem(22,card);
            }
        } else {
            if (game.getGameState().equals(GameState.OFF)){
                ItemStack join = new ItemStack(Material.BARRIER);
                ItemMeta joinMeta = join.getItemMeta();
                joinMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
                join.setItemMeta(joinMeta);

                inventory.setItem(22,join);
            } else if (game.getGameState().equals(GameState.RECRUITING)){
                ItemStack join = new ItemStack(Material.PAPER);
                ItemMeta joinMeta = join.getItemMeta();
                joinMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("join-button"));

                // Setting lore
                if (!game.getPlayers().isEmpty()){
                    List<String> lore = new ArrayList<>();
                    lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                    for (UUID target : game.getPlayers().keySet()){
                        lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                    }
                    joinMeta.setLore(lore);
                }
                join.setItemMeta(joinMeta);

                inventory.setItem(22,join);
            } else {
                ItemStack join = new ItemStack(Material.MAP);
                ItemMeta joinMeta = join.getItemMeta();
                joinMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("game-active-button"));

                // Setting lore
                List<String> lore = new ArrayList<>();
                lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                for (UUID target : game.getPlayers().keySet()){
                    lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                }
                joinMeta.setLore(lore);
                join.setItemMeta(joinMeta);

                inventory.setItem(22,join);
            }

        }
    }
}
