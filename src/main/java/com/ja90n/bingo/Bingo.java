package com.ja90n.bingo;

import com.ja90n.bingo.command.BingoCommand;
import com.ja90n.bingo.event.InventoryEvents.BingoCardEvent;
import com.ja90n.bingo.event.InventoryEvents.HostMenuEvent;
import com.ja90n.bingo.event.InventoryEvents.MainMenuEvent;
import com.ja90n.bingo.instance.Game;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bingo extends JavaPlugin {

    private Game game;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("bingo").setExecutor(new BingoCommand(this));


        getServer().getPluginManager().registerEvents(new MainMenuEvent(this),this);
        getServer().getPluginManager().registerEvents(new HostMenuEvent(this),this);
        getServer().getPluginManager().registerEvents(new BingoCardEvent(this),this);

        game = new Game(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Game getGame() {
        return game;
    }
}
