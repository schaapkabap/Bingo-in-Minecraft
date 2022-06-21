package com.ja90n.bingo.gui;

import com.ja90n.bingo.Bingo;
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

    public HostMenuGui(UUID uuid, Bingo bingo) {
        player = Bukkit.getPlayer(uuid);
        game = bingo.getGame();

        Inventory menu = Bukkit.createInventory(player,45, ChatColor.LIGHT_PURPLE + "Host menu");

        // Frame
        ItemStack frame = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta framemeta = frame.getItemMeta();
        framemeta.setDisplayName(" ");
        frame.setItemMeta(framemeta);
        for (int i : new int[]{0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44}){
            menu.setItem(i, frame);
        }

        // Buttons

        // Start / stop button
        if (game.getGameState().equals(GameState.OFF)){
            ItemStack status = new ItemStack(Material.RED_CONCRETE);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.GREEN + "Activate game");

            // Setting lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "Status: " + ChatColor.RED + "Inactive");
            statusMeta.setLore(lore);

            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        } else if (game.getGameState().equals(GameState.REQRUITING)){
            ItemStack status = new ItemStack(Material.GREEN_CONCRETE);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.GREEN + "Start game");

            // Setting lore
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Recruiting");
            lore.add(ChatColor.LIGHT_PURPLE + "Current players:");
            for (UUID target : game.getPlayers().keySet()){
                lore.add(ChatColor.BLUE + Bukkit.getPlayer(target).getDisplayName());
            }

            statusMeta.setLore(lore);

            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        } else {
            ItemStack status = new ItemStack(Material.BARRIER);
            ItemMeta statusMeta = status.getItemMeta();
            statusMeta.setDisplayName(ChatColor.RED + "Force stop game");

            // Setting lore
            List<String> lore = new ArrayList<>();
            if (game.getGameState().equals(GameState.LINE)){
                lore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Line");
            } else {
                lore.add(ChatColor.WHITE + "Status: " + ChatColor.GREEN + "Full");
            }
            if (!game.getPlayers().isEmpty()){
                lore.add(ChatColor.LIGHT_PURPLE + "Current players:");
                for (UUID target : game.getPlayers().keySet()){
                    lore.add(ChatColor.BLUE + Bukkit.getPlayer(target).getDisplayName());
                }
            }

            statusMeta.setLore(lore);

            status.setItemMeta(statusMeta);
            menu.setItem(20,status);
        }

        // Next number
        ItemStack number = new ItemStack(Material.PAPER);
        ItemMeta numberMeta = number.getItemMeta();
        numberMeta.setDisplayName(ChatColor.WHITE + "Next number");
        number.setItemMeta(numberMeta);
        menu.setItem(24,number);

        player.openInventory(menu);
    }
}