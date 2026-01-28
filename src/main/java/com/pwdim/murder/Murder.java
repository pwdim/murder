package com.pwdim.murder;

import com.pwdim.murder.commands.GameCommand;
import com.pwdim.murder.commands.LobbyCommand;
import com.pwdim.murder.commands.PlayCommand;
import com.pwdim.murder.commands.RoomManagerCommand;
import com.pwdim.murder.itens.LobbyItem;
import com.pwdim.murder.itens.RoomItem;
import com.pwdim.murder.listeners.BuildListeners;
import com.pwdim.murder.manager.arena.ArenaEgine;
import com.pwdim.murder.manager.arena.ArenaManager;
import com.pwdim.murder.manager.game.GameManager;
import com.pwdim.murder.manager.player.PlayerManager;
import com.pwdim.murder.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Murder extends JavaPlugin {

    private GameManager gameManager;
    private ArenaManager arenaManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.arenaManager = new ArenaManager(this);

        this.gameManager = new GameManager(this);

        this.playerManager = gameManager.getPlayerManager();

        getServer().getPluginManager().registerEvents(new BuildListeners(gameManager), this);
        getServer().getPluginManager().registerEvents(new ArenaEgine(this), this);
        getServer().getPluginManager().registerEvents(new LobbyItem(this), this);
        getServer().getPluginManager().registerEvents(new RoomItem(this), this);

        getCommand("game").setExecutor(new GameCommand(this, gameManager));
        getCommand("murder").setExecutor(new PlayCommand(this));
        getCommand("lobby").setExecutor(new LobbyCommand(this));
        getCommand("rooms").setExecutor(new RoomManagerCommand());

        logger("&bO plugin foi iniciado com sucesso");
    }

    public String getPrefix() {
        String prefixConfig = getConfig().getString("plugin.prefix", "&c[Murder]");
        return ColorUtil.color(prefixConfig + " &r");
    }

    public void logger(String log){

        log = ColorUtil.color(log);

        Bukkit.getConsoleSender().sendMessage(getPrefix() + ColorUtil.color(log));
    }

    @Override
    public void onDisable() {
        if (arenaManager != null) {
            logger("&eDescarregando arenas ativas...");
            arenaManager.getActiveArenas().keySet().forEach(id -> {
                Bukkit.unloadWorld(id, false);
            });
        }
        logger("&cO plugin foi desligado com sucesso");
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public void setArenaManager(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }
}
