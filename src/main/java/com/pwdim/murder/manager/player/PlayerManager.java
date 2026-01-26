package com.pwdim.murder.manager.player;

import com.pwdim.murder.Murder;
import com.pwdim.murder.itens.LobbyItem;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.manager.arena.ArenaManager;
import com.pwdim.murder.manager.game.GameManager;
import com.pwdim.murder.manager.game.GameState;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;


public class PlayerManager {

    private GameManager gameManager;
    private PlayerState playerState;

    private final Murder plugin;

    public PlayerManager(GameManager gameManager, Murder plugin){
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    public void sendToArena(Player player) {
        ArenaManager manager = plugin.getArenaManager();

        Arena bestArena = manager.getActiveArenas().values().stream()
                .filter(arena -> arena.getState() == GameState.WAITING)
                .filter(arena -> arena.getPlayers().size() < MessageUtils.getMaxPLayers())
                .max(Comparator.comparingInt(arena -> arena.getPlayers().size()))
                .orElse(null);

        if (bestArena != null) {
            player.sendMessage(ColorUtil.color("&aConectando..."));
            teleportToArena(player, bestArena);
            LobbyItem.giveItem(player);
        } else {
            player.sendMessage(ColorUtil.color("&aConectando..."));


            manager.setupNewArena("builds", (newArena) -> {
                teleportToArena(player, newArena);
                LobbyItem.giveItem(player);
                checkStart(newArena);
            });
        }
    }

    public void sendToLobby(Player player, Arena arena){
        Bukkit.getScheduler().runTask(plugin, () -> {
            arena.getPlayers().remove(player.getUniqueId());
            player.teleport(MessageUtils.getLobby());

            arena.broadcastArena("&b" + player.getName() + " &esaiu da partida &7(&a" + arena.getPlayers().size() + "/"+MessageUtils.getMaxPLayers()+"&7)");
        });
    }

    private void teleportToArena(Player player, Arena arena){
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.teleport(arena.getWorld().getSpawnLocation());
            arena.getPlayers().add(player.getUniqueId());

            arena.broadcastArena("&b" + player.getName() + " &eentrou na partida &7(&a" + arena.getPlayers().size() + "/"+MessageUtils.getMaxPLayers()+"&7)");
        });
    }

    private void checkStart(Arena arena) {
        if (arena.getPlayers().size() >= MessageUtils.getMinPlayers() && arena.getState() == GameState.WAITING) {
            plugin.getGameManager().setGameState(arena, GameState.STARTING);
        }
    }
}
