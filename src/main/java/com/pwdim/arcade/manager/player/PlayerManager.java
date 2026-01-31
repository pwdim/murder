package com.pwdim.arcade.manager.player;

import com.pwdim.arcade.Arcade;
import com.pwdim.arcade.itens.LobbyItem;
import com.pwdim.arcade.manager.arena.Arena;
import com.pwdim.arcade.manager.arena.ArenaManager;
import com.pwdim.arcade.manager.game.GameManager;
import com.pwdim.arcade.manager.game.GameState;
import com.pwdim.arcade.utils.ColorUtil;
import com.pwdim.arcade.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.Comparator;


public class PlayerManager {

    private GameManager gameManager;
    private PlayerState playerState;

    private final Arcade plugin;

    public PlayerManager(GameManager gameManager, Arcade plugin){
        this.gameManager = gameManager;
        this.plugin = plugin;
    }

    public void sendToArena(Player player) {
        ArenaManager manager = plugin.getArenaManager();

        Arena bestArena = manager.getActiveArenas().values().stream()
                .filter(arena -> arena.getState() == GameState.WAITING)
                .filter(arena -> arena.getPlayers().size() < ConfigUtils.getMaxPLayers())
                .max(Comparator.comparingInt(arena -> arena.getPlayers().size()))
                .orElse(null);

        if (bestArena != null) {
            player.sendMessage(ColorUtil.color("&aConectando..."));
            teleportToArena(player, bestArena);
            LobbyItem.giveItem(player);
            checkStart(bestArena);
        } else {
            player.sendMessage(ColorUtil.color("&aConectando..."));


            manager.setupNewArena(ConfigUtils.getGameMap(), (newArena) -> {
                teleportToArena(player, newArena);
                LobbyItem.giveItem(player);
                checkStart(newArena);
            });
        }
    }

    public void sendToLobby(Player player, Arena arena){
        Bukkit.getScheduler().runTask(plugin, () -> {
            arena.getPlayers().remove(player.getUniqueId());
            player.teleport(ConfigUtils.getLobby());
            LobbyItem.removeItem(player);


            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (arena.getState() == GameState.WAITING || arena.getState() == GameState.STARTING){
                    arena.broadcastArena("&b" + player.getName() + " &esaiu da partida &7(&a" + arena.getPlayers().size() + "/"+ConfigUtils.getMaxPLayers()+"&7)");
                }
                }, 23L);
            checkStart(arena);
        });
    }

    public void sendToLobby(Player player){
        Arena checkArena = plugin.getArenaManager().getPlayerArena(player);
        Bukkit.getScheduler().runTask(plugin, () -> {
            checkArena.getPlayers().remove(player.getUniqueId());
            player.teleport(ConfigUtils.getLobby());
            LobbyItem.removeItem(player);


            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (checkArena.getState() == GameState.WAITING || checkArena.getState() == GameState.STARTING){
                    checkArena.broadcastArena("&b" + player.getName() + " &esaiu da partida &7(&a" + checkArena.getPlayers().size() + "/"+ConfigUtils.getMaxPLayers()+"&7)");
                }
            }, 23L);
            checkStart(checkArena);
        });
    }

    private void teleportToArena(Player player, Arena arena){
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.teleport(arena.getWorld().getSpawnLocation());
            arena.getPlayers().add(player.getUniqueId());

            arena.broadcastArena("&b" + player.getName() + " &eentrou na partida &7(&a" + arena.getPlayers().size() + "/"+ConfigUtils.getMaxPLayers()+"&7)");
            checkStart(arena);
        });
    }

    private void checkStart(Arena arena) {
        if ((arena.getPlayers().size() >= ConfigUtils.getMinPlayers()) && (arena.getState() == GameState.WAITING)) {
            plugin.getGameManager().setGameState(arena, GameState.STARTING);
        }
        if ((arena.getPlayers().size() < ConfigUtils.getMinPlayers()) && (arena.getState() == GameState.STARTING)){
            plugin.getGameManager().setGameState(arena, GameState.WAITING);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                arena.broadcastArena(ColorUtil.color("&cJogadores insuficientes, contador cancelado!"));
            }, 25L);
        }
    }
}
