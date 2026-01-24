package com.pwdim.murder.manager.arena;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.pwdim.murder.manager.game.GameState;
import com.pwdim.murder.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Arena {

    private String id;
    private String mapName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    private World world;
    private List<UUID> players = new ArrayList<>();
    private GameState state;

    public Arena(String id, String mapName, World world){
        this.id = id;
        this.mapName = mapName;
        this.world = world;
        this.state = GameState.WAITING;
    }

    public void broadcastArena(String msg){
        players.forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(ColorUtil.color(msg)));
    }

    public void titleArena(String msg){
        players.forEach(uuid -> Bukkit.getPlayer(uuid).sendTitle(ColorUtil.color(msg), null));
    }

}
