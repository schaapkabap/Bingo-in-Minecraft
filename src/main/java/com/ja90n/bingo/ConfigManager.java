package com.ja90n.bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

public class ConfigManager {

    private YamlConfiguration yamlConfiguration;
    private ChatColor chatColor;
    private ItemStack itemStack;

    public ConfigManager(Bingo bingo){
        File file = new File(bingo.getDataFolder(), bingo.getConfig().getString("language") + ".yml");
        if (file.exists()){
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Language file does not exist so the Bingo plugin could not start!");
            bingo.getPluginLoader().disablePlugin(bingo);
        }

        switch (bingo.getConfig().getString("colour")){
            case "red":
                chatColor = ChatColor.RED;
                itemStack = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta redmeta = itemStack.getItemMeta();
                redmeta.setDisplayName(" ");
                itemStack.setItemMeta(redmeta);
                break;
            case "orange":
                chatColor = ChatColor.GOLD;
                itemStack = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
                ItemMeta orangemeta = itemStack.getItemMeta();
                orangemeta.setDisplayName(" ");
                itemStack.setItemMeta(orangemeta);
                break;
            case "lime":
                chatColor = ChatColor.GREEN;
                itemStack = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta limemeta = itemStack.getItemMeta();
                limemeta.setDisplayName(" ");
                itemStack.setItemMeta(limemeta);
                break;
            case "green":
                chatColor = ChatColor.DARK_GREEN;
                itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                ItemMeta greenmeta = itemStack.getItemMeta();
                greenmeta.setDisplayName(" ");
                itemStack.setItemMeta(greenmeta);
                break;
            case "light_blue":
                chatColor = ChatColor.BLUE;
                itemStack = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
                ItemMeta light_bluemeta = itemStack.getItemMeta();
                light_bluemeta.setDisplayName(" ");
                itemStack.setItemMeta(light_bluemeta);
                break;
            case "blue":
                chatColor = ChatColor.BLUE;
                itemStack = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
                ItemMeta bluemeta = itemStack.getItemMeta();
                bluemeta.setDisplayName(" ");
                itemStack.setItemMeta(bluemeta);
                break;
            case "pink":
                chatColor = ChatColor.LIGHT_PURPLE;
                itemStack = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
                ItemMeta pinkmeta = itemStack.getItemMeta();
                pinkmeta.setDisplayName(" ");
                itemStack.setItemMeta(pinkmeta);
                break;
            case "purple":
                chatColor = ChatColor.DARK_PURPLE;
                itemStack = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
                ItemMeta purplemeta = itemStack.getItemMeta();
                purplemeta.setDisplayName(" ");
                itemStack.setItemMeta(purplemeta);
                break;
            case "black":
                chatColor = ChatColor.DARK_GRAY;
                itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                ItemMeta blackmeta = itemStack.getItemMeta();
                blackmeta.setDisplayName(" ");
                itemStack.setItemMeta(blackmeta);
                break;
            case "grey":
            case "gray":
                chatColor = ChatColor.GRAY;
                itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta greymeta = itemStack.getItemMeta();
                greymeta.setDisplayName(" ");
                itemStack.setItemMeta(greymeta);
                break;
            default:
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Color does not exist so the Bingo plugin could not start!");
                bingo.getPluginLoader().disablePlugin(bingo);
        }
    }

    public String getMessage(String type){
        return yamlConfiguration.getString(type);
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public ItemStack getFrame() {
        return itemStack;
    }
}