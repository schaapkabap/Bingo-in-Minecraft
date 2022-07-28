package com.ja90n.bingo.gui;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.ConfigManager;
import com.ja90n.bingo.GameState;
import com.ja90n.bingo.instance.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainMenuGui {

    private Player player;
    private Game game;
    private ConfigManager configManager;

    public MainMenuGui (UUID uuid, Bingo bingo){
        configManager = bingo.getConfigManager();
        player = Bukkit.getPlayer(uuid);
        game = bingo.getGame();

        Inventory menu = Bukkit.createInventory(player,45, configManager.getChatColor() + configManager.getMessage("main-menu"));

        // Frame
        ItemStack frame = configManager.getFrame();
        ItemMeta framemeta = frame.getItemMeta();
        framemeta.setDisplayName(" ");
        frame.setItemMeta(framemeta);
        for (int i : new int[]{0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44}){
            menu.setItem(i, frame);
        }

        // Buttons

        //Status
        if (game.getGameState().equals(GameState.OFF)){
            ItemStack statusOff = new ItemStack(Material.RED_CONCRETE);
            ItemMeta statusOffMeta = statusOff.getItemMeta();
            statusOffMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
            statusOff.setItemMeta(statusOffMeta);
            menu.setItem(20,statusOff);
        } else {
            ItemStack statusOn = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta statusOnMeta = statusOn.getItemMeta();
            if (game.getGameState().equals(GameState.REQRUITING)){
                statusOnMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("recruiting-status"));
            } else if (game.getGameState().equals(GameState.LINE)){
                statusOnMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("line-status"));
            } else {
                statusOnMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("full-status"));
            }
            statusOn.setItemMeta(statusOnMeta);
            menu.setItem(20,statusOn);
        }

        // Join / leave button
        if (game.getPlayers().containsKey(uuid)){
            if (game.getGameState().equals(GameState.OFF)){
                ItemStack broken = new ItemStack(Material.CHAIN);
                ItemMeta brokenMeta = broken.getItemMeta();
                brokenMeta.setDisplayName(ChatColor.RED + "Send a picture of this for 1 euro to Ja90n (one time use)");
                broken.setItemMeta(brokenMeta);

                menu.setItem(22,broken);
            } else if (game.getGameState().equals(GameState.REQRUITING)){
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

                menu.setItem(22,leave);
            } else {
                ItemStack card = new ItemStack(Material.PAPER);
                ItemMeta cardMeta = card.getItemMeta();
                cardMeta.setDisplayName(configManager.getChatColor() + configManager.getMessage("bingo-card"));
                card.setItemMeta(cardMeta);

                menu.setItem(22,card);
            }
        } else {
            if (game.getGameState().equals(GameState.OFF)){
                ItemStack join = new ItemStack(Material.BARRIER);
                ItemMeta joinMeta = join.getItemMeta();
                joinMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
                join.setItemMeta(joinMeta);

                menu.setItem(22,join);
            } else if (game.getGameState().equals(GameState.REQRUITING)){
                ItemStack join = new ItemStack(Material.PAPER);
                ItemMeta joinMeta = join.getItemMeta();
                joinMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("join-button"));

                // Setting lore
                if (!game.getPlayers().isEmpty()){
                    List<String> lore = new ArrayList<>();
                    lore.add(configManager.getChatColor() + configManager.getMessage("current-player"));
                    for (UUID target : game.getPlayers().keySet()){
                        lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                    }
                    joinMeta.setLore(lore);
                }
                join.setItemMeta(joinMeta);

                menu.setItem(22,join);
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

                menu.setItem(22,join);
            }

        }

        // Host button
        if (player.hasPermission("bingo.host")){
            ItemStack host = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta hostMeta = (SkullMeta) host.getItemMeta();
            hostMeta.setDisplayName(configManager.getChatColor() + configManager.getMessage("host-menu"));
            host.setItemMeta(hostMeta);

            menu.setItem(24,host);
        } else {
            ItemStack host = new ItemStack(Material.BARRIER);
            ItemMeta hostMeta = host.getItemMeta();
            hostMeta.setDisplayName(ChatColor.RED + configManager.getMessage("host-menu"));
            host.setItemMeta(hostMeta);

            menu.setItem(24,host);
        }

        player.openInventory(menu);
    }
}