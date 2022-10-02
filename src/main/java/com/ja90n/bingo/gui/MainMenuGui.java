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

    private static int NUMBER = 24;
    private static int SIZE = 45;
    private static int ITEMSIZE = 21;
    private Player player;
    private Game game;
    private ConfigManager configManager;

    private Inventory menu;


    public MainMenuGui(UUID uuid, Bingo bingo) {
        configManager = bingo.getConfigManager();
        player = Bukkit.getPlayer(uuid);

        game = bingo.getGame();

        menu = Bukkit.createInventory(player, SIZE, configManager.getChatColor() + configManager.getMessage("main-menu"));

        GuiBuilder();
        CreateButtons();
    }

    private void GuiBuilder() {
        // Frame
        ItemStack frame = configManager.getFrame();
        ItemMeta frameMeta = frame.getItemMeta();
        frameMeta.setDisplayName(" ");
        frame.setItemMeta(frameMeta);
        for (int i : new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44}) {
            menu.setItem(i, frame);
        }
    }

    private void CreateButtons() {

        // Buttons
        ItemStack status;
        ItemMeta statusMeta;
        //Status
        if (game.getGameState().equals(GameState.OFF)) {
            status = new ItemStack(Material.RED_CONCRETE);
            statusMeta = setDisplayNameWithStatus(status.getItemMeta(), ChatColor.RED, configManager.getMessage("inactive-status"));

        } else {
            status = new ItemStack(Material.GREEN_CONCRETE);
            if (game.getGameState().equals(GameState.RECRUITING)) {
                statusMeta = setDisplayNameWithStatus(status.getItemMeta(), ChatColor.GREEN, configManager.getMessage("recruiting-status"));
            } else if (game.getGameState().equals(GameState.LINE)) {
                statusMeta = setDisplayNameWithStatus(status.getItemMeta(), ChatColor.RED, configManager.getMessage("line-status"));
            } else {
                statusMeta = setDisplayNameWithStatus(status.getItemMeta(), ChatColor.RED, configManager.getMessage("full-status"));
            }

        }
        status.setItemMeta(statusMeta);
        setTheMenuItemSize(20, status);

        PortalButton();
        CreateHostButton();
    }

    private void PortalButton() {
        // Join / leave button
        if (game.getPlayers().containsKey(player.getUniqueId())) {
            if (game.getGameState().equals(GameState.OFF)) {
                ItemStack broken = new ItemStack(Material.CHAIN);
                ItemMeta brokenMeta = setDisplayName(broken.getItemMeta(), ChatColor.RED, "Send a picture of this for 1 euro to Ja90n (one time use)");
                broken.setItemMeta(brokenMeta);

                menu.setItem(22, broken);
            } else if (game.getGameState().equals(GameState.RECRUITING)) {
                ItemStack leave = new ItemStack(Material.BARRIER);
                ItemMeta leaveMeta = setDisplayName(leave.getItemMeta(), ChatColor.RED, configManager.getMessage("leave-button"));

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

                menu.setItem(22, leave);
            } else {
                ItemStack card = new ItemStack(Material.PAPER);
                ItemMeta cardMeta = setDisplayName(card.getItemMeta(), configManager.getChatColor() + configManager.getMessage("bingo-card"));
                card.setItemMeta(cardMeta);

                menu.setItem(ITEMSIZE, card);
            }
        } else {
            if (game.getGameState().equals(GameState.OFF)) {
                ItemStack join = new ItemStack(Material.BARRIER);
                ItemMeta joinMeta = setDisplayName(join.getItemMeta(),ChatColor.WHITE, configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
                join.setItemMeta(joinMeta);

                menu.setItem(22, join);
            } else if (game.getGameState().equals(GameState.RECRUITING)) {
                ItemStack join = new ItemStack(Material.PAPER);
                ItemMeta joinMeta = setDisplayName(join.getItemMeta(),ChatColor.GREEN + configManager.getMessage("join-button"));

                // Setting lore
                if (!game.getPlayers().isEmpty()) {
                    List<String> lore = new ArrayList<>();
                    lore.add(configManager.getChatColor() + configManager.getMessage("current-player"));
                    for (UUID target : game.getPlayers().keySet()) {
                        lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                    }
                    joinMeta.setLore(lore);
                }
                join.setItemMeta(joinMeta);

                setTheMenuItemSize(22, join);
            } else {
                ItemStack join = new ItemStack(Material.MAP);
                ItemMeta joinMeta = setDisplayName(join.getItemMeta(), ChatColor.GREEN, configManager.getMessage("game-active-button"));

                // Setting lore
                List<String> lore = new ArrayList<>();
                lore.add(configManager.getChatColor() + configManager.getMessage("current-players"));
                for (UUID target : game.getPlayers().keySet()) {
                    lore.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
                }
                joinMeta.setLore(lore);
                join.setItemMeta(joinMeta);

                setTheMenuItemSize(22, join);

            }

        }
    }

    private void CreateHostButton() {

        ItemStack host;
        ItemMeta hostMeta;
        // Host button
        if (player.hasPermission("bingo.host")) {
            host = new ItemStack(Material.PLAYER_HEAD);
            hostMeta = (SkullMeta) setDisplayName(host.getItemMeta(), configManager.getChatColor(), configManager.getMessage("host-menu"));
            menu.setItem(NUMBER, host);

        } else {
            host = new ItemStack(Material.BARRIER);
            hostMeta = setDisplayName(host.getItemMeta(), ChatColor.RED + configManager.getMessage("host-menu"));
            host.setItemMeta(hostMeta);
        }
        host.setItemMeta(hostMeta);
        menu.setItem(NUMBER, host);
        player.openInventory(menu);
    }

    private ItemMeta setDisplayNameWithStatus(ItemMeta meta, ChatColor color, String text) {
        setDisplayName(meta, color, configManager.getMessage("status") + text);
        return meta;
    }

    private ItemMeta setDisplayName(ItemMeta meta, ChatColor color, String text) {
        setDisplayName(meta, color + text);
        return meta;
    }

    private ItemMeta setDisplayName(ItemMeta meta, String text) {
        meta.setDisplayName(text);
        return meta;
    }
    private void setTheMenuItemSize(int size, ItemStack itemStack) {
        menu.setItem(size, itemStack);
    }


}