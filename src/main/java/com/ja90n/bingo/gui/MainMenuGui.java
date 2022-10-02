package com.ja90n.bingo.gui;

import com.ja90n.bingo.Bingo;
import com.ja90n.bingo.ConfigManager;
import com.ja90n.bingo.Factories.ConcreteItemStackFactory;
import com.ja90n.bingo.Factories.ItemStackFactory;
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

public class MainMenuGui {

    private static int NUMBER = 24;
    private static int SIZE = 45;
    private static int ITEMSIZE = 21;
    private Player player;
    private Game game;
    private ConfigManager configManager;

    private Inventory menu;

    private ItemStackFactory factory;


    public MainMenuGui(UUID uuid, Bingo bingo) {
        factory = new ConcreteItemStackFactory();
        configManager = bingo.getConfigManager();
        player = Bukkit.getPlayer(uuid);

        game = bingo.getGame();

        menu = Bukkit.createInventory(player, SIZE, configManager.getChatColor() + configManager.getMessage("main-menu"));

        GuiBuilder();
        CreateButtons();
    }

    private void GuiBuilder() {
        // Frame

        ItemStack frame = factory.CreateEmptyItemStack(configManager.getFrame(), " ");
        setItemsInStack(new int[]{1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44}, frame);
    }

    private void setItemsInStack(int[] ints, ItemStack frame) {
        for (int i : ints) {
            setTheMenuItemSize(i,frame);
        }
    }

    private void CreateButtons() {

        // Buttons
        ItemStack ButtonStatusItemStack;
        //Status
        if (game.getGameState().equals(GameState.OFF)) {
            ButtonStatusItemStack = factory.CreateItem(Material.RED_CONCRETE, ChatColor.RED, configManager.getMessage("inactive-status"));

        } else {
            Material materialColor = Material.GREEN_CONCRETE;
            if (game.getGameState().equals(GameState.RECRUITING)) {
                ButtonStatusItemStack = factory.CreateItem(materialColor, ChatColor.GREEN, configManager.getMessage("recruiting-status"));
            } else if (game.getGameState().equals(GameState.LINE)) {
                ButtonStatusItemStack = factory.CreateItem(materialColor, ChatColor.RED, configManager.getMessage("line-status"));
            } else {
                ButtonStatusItemStack = factory.CreateItem(materialColor, ChatColor.RED, configManager.getMessage("full-status"));
            }

        }
        setTheMenuItemSize(20, ButtonStatusItemStack);

        PortalButton();
        CreateHostButton();
    }

    private void PortalButton() {
        // Join / leave button
        if (game.getPlayers().containsKey(player.getUniqueId())) {
            if (game.getGameState().equals(GameState.OFF)) {
                ItemStack broken = factory.CreateItem(Material.CHAIN, ChatColor.RED, "Send a picture of this for 1 euro to Ja90n (one time use)");
                setTheMenuItemSize(22, broken);
            } else if (game.getGameState().equals(GameState.RECRUITING)) {
                ItemStack leave = factory.CreateItem(Material.BARRIER, ChatColor.RED, configManager.getMessage("leave-button"));
                ItemMeta leaveMeta = leave.getItemMeta();
                // Setting lore

                if (!game.getPlayers().isEmpty()) {

                    leaveMeta.setLore(getLores());
                }
                leave.setItemMeta(leaveMeta);
                menu.setItem(22, leave);
            } else {
                ItemStack card = factory.CreateItem(Material.PAPER, configManager.getChatColor(), configManager.getMessage("bingo-card"));
                setTheMenuItemSize(ITEMSIZE,card);

            }
        } else {
            if (game.getGameState().equals(GameState.OFF)) {
                ItemStack join = factory.CreateItem(Material.BARRIER, ChatColor.WHITE, configManager.getMessage("status") + ChatColor.RED + configManager.getMessage("inactive-status"));
                setTheMenuItemSize(22,join);
            } else if (game.getGameState().equals(GameState.RECRUITING)) {
                ItemStack join = factory.CreateItem(Material.PAPER, ChatColor.GREEN , configManager.getMessage("join-button"));

                // Setting lore
                if (!game.getPlayers().isEmpty()) {
                    join.getItemMeta().setLore(getLores());
                }

                setTheMenuItemSize(22, join);
            } else {
                ItemStack join = factory.CreateItem(Material.MAP, ChatColor.GREEN, configManager.getMessage("game-active-button"));
                // Setting lore
                join.getItemMeta().setLore(getLores());

                setTheMenuItemSize(22, join);

            }

        }
    }

    private void CreateHostButton() {

        ItemStack host;
        ItemMeta hostMeta;
        // Host button
        if (player.hasPermission("bingo.host")) {
            host = factory.CreateItem(Material.PLAYER_HEAD, configManager.getChatColor(), configManager.getMessage("host-menu"));
        } else {
            host = factory.CreateItem(Material.BARRIER, ChatColor.RED, configManager.getMessage("host-menu"));
        }
        menu.setItem(NUMBER, host);
        player.openInventory(menu);
    }

    private void setTheMenuItemSize(int size, ItemStack itemStack) {
        menu.setItem(size, itemStack);
    }

    private List<String> getLores(){
        List<String> lores = new ArrayList<>();
        lores.add(configManager.getChatColor() + configManager.getMessage("current-players"));
        for (UUID target : game.getPlayers().keySet()) {
            lores.add(ChatColor.WHITE + Bukkit.getPlayer(target).getDisplayName());
        }
        return lores;
    }

}