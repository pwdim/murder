package com.pwdim.murder.manager.arena;

import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.game.GameManager;
import com.pwdim.murder.manager.game.GameState;
import com.pwdim.murder.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class ArenaManager {

    private final Murder plugin;
    private final ArenaEgine arenaEgine;
    private final Map<String, Arena> activeArenas = new HashMap<>();

    public ArenaManager(Murder plugin){
        this.plugin = plugin;

        this.arenaEgine = new ArenaEgine(plugin);
    }

    public void setupNewArena(String mapName, Consumer<Arena> callback){
        String arenaID = "arena_" + System.currentTimeMillis();

        arenaEgine.createWorldInstace(mapName, arenaID, world -> {
            Arena arena = new Arena(arenaID, mapName, world);
            activeArenas.put(arenaID, arena);
            plugin.logger("&aNova sala criada para o &eMapa "+ mapName+" &b(" + arenaID + ") ");

            if (callback != null) {
                callback.accept(arena);
            }
        });
    }

    public void finishArena(String arenaID){
        Arena arena = activeArenas.get(arenaID);
        if (arena == null) return;

        String template = arena.getMapName();

        arena.getPlayers().forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) {
                p.teleport(MessageUtils.getLobby());
            }
        });

        arenaEgine.deleteWorldInstace(arenaID);
        activeArenas.remove(arenaID);


        setupNewArena(template, null);
    }

    public Map<String, Arena> getActiveArenas() {
        return activeArenas;
    }
}
