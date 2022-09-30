package com.ja90n.bingo;

import com.ja90n.bingo.command.BingoCommand;
import com.ja90n.bingo.event.InventoryEvents.BingoCardEvent;
import com.ja90n.bingo.event.InventoryEvents.HostMenuEvent;
import com.ja90n.bingo.event.InventoryEvents.MainMenuEvent;
import com.ja90n.bingo.instance.Game;
import com.ja90n.bingo.util.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bingo extends JavaPlugin {

    private Game game;
    private ConfigManager configManager;

    public static String LanguageEnglishYAMl= "lang_en.yml";
    public static String LanguageDutchYAMl= "ang_nl.yml";
    public static String LanguageSpanishYAMl= "lang_es.yml";
    public static int BSTATS_PLUGIN_ID = 15698;


    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource(LanguageEnglishYAMl, false);
        saveResource(LanguageDutchYAMl, false);
        saveResource(LanguageSpanishYAMl, false);

        configManager = new ConfigManager(this);

        getCommand("bingo").setExecutor(new BingoCommand(this));


       new Metrics(this, BSTATS_PLUGIN_ID);

        RegisterPluginEvents(new MainMenuEvent(this));
        RegisterPluginEvents(new HostMenuEvent(this));
        RegisterPluginEvents(new BingoCardEvent(this));


        game = new Game(this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "The bingo plugin has been enabled!");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Thank you so much for playing with my plugin - Ja90n");
    }

    public Game getGame() {
        return game;
    }
    public ConfigManager getConfigManager() { return configManager; }


    private void RegisterPluginEvents(Listener listner){

        getServer().getPluginManager().registerEvents(listner, this);

    }
}
