package com.ja90n.bingo;

import com.ja90n.bingo.command.BingoCommand;
import com.ja90n.bingo.event.InventoryEvents.BingoCardEvent;
import com.ja90n.bingo.event.InventoryEvents.HostMenuEvent;
import com.ja90n.bingo.event.InventoryEvents.MainMenuEvent;
import com.ja90n.bingo.instance.Game;
import com.ja90n.bingo.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;

public final class Bingo extends JavaPlugin {

    private Game game;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource("lang_en.yml", false);
        saveResource("lang_es.yml", false);
        saveResource("lang_nl.yml", false);

        configManager = new ConfigManager(this);

        getCommand("bingo").setExecutor(new BingoCommand(this));

        int pluginId = 15698;
        Metrics metrics = new Metrics(this, pluginId);

        getServer().getPluginManager().registerEvents(new MainMenuEvent(this),this);
        getServer().getPluginManager().registerEvents(new HostMenuEvent(this),this);
        getServer().getPluginManager().registerEvents(new BingoCardEvent(this),this);

        game = new Game(this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "The bingo plugin has been enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Thank you so much for playing with my plugin - Ja90n");
    }

    public Game getGame() {
        return game;
    }
    public ConfigManager getConfigManager() { return configManager; }
}
