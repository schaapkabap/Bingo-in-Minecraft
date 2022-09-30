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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HostMenuGui {

    private Player player;
    private Game game;
    private ConfigManager configManager;

    public HostMenuGui(UUID uuid, Bingo bingo) {
        configManager = bingo.getConfigManager();
        player = Bukkit.getPlayer(uuid);
        game = bingo.getGame();

        Inventory menu = Bukkit.createInventory(player,45, configManager.getChatColor() + configManager.getMessage("host-menu"));

        // Frame
        ItemStack frame = configManager.getFrame();
        ItemMeta framemeta = frame.getItemMeta();
        framemeta.setDisplayName(" ");
        frame.setItemMeta(framemeta);
        for (int i : new int[]{1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44}){
            menu.setItem(i, frame);
        }

        // Buttons

        // Start / stop button
        if (game.getGameState().equals(GameState.OFF)){
            ItemStack status = new ItemStack(Material.RED_CONCRETE);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("activate-game-button"));

            // Setting lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
            statusMeta.setLore(lore);

            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        } else if (game.getGameState().equals(GameState.RECRUITING)){
            ItemStack status = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.GREEN + configManager.getMessage("start-game-button"));

            // Setting lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("recruiting-status"));
            if (!game.getPlayers().isEmpty()){
                lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                for (UUID target : game.getPlayers().keySet()){
                    lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                }
            }

            statusMeta.setLore(lore);

            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        } else {
            ItemStack status = new ItemStack(Material.BARRIER);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.RED + configManager.getMessage("force-stop-game-button"));

            // Setting lore
            List<String> lore = new ArrayList<>();
            if (game.getGameState().equals(GameState.LINE)){
                lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("line-status"));
            } else {
                lore.add(ChatColor.WHITE + configManager.getMessage("status") + ChatColor.GREEN + configManager.getMessage("full-status"));
            }
            if (!game.getPlayers().isEmpty()){
                lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                for (UUID target : game.getPlayers().keySet()){
                    lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                }
            }

            statusMeta.setLore(lore);
            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        }

        // Back button
        ItemStack backButton = new ItemStack(Material.ARROW);
        ItemMeta backButtonMeta = backButton.getItemMeta();
        backButtonMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("back-to-main-menu-button"));
        backButton.setItemMeta(backButtonMeta);
        menu.setItem(0,backButton);

        // Next number
        ItemStack number = new ItemStack(Material.PAPER);
        ItemMeta numberMeta = number.getItemMeta();
        numberMeta.setDisplayName(ChatColor.WHITE + configManager.getMessage("next-number-button"));
        number.setItemMeta(numberMeta);
        menu.setItem(24,number);

        player.openInventory(menu);
    }
}