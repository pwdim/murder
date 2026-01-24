package com.pwdim.murder.manager.game;

import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.manager.arena.ArenaManager;
import com.pwdim.murder.manager.player.PlayerManager;
import com.pwdim.murder.manager.blocks.BlockManager;
import com.pwdim.murder.tasks.GameStartCountdownTask;
import com.pwdim.murder.utils.ColorUtil;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.UUID;

public class GameManager {

    private final Murder plugin;

    private GameState gameState = GameState.WAITING;

    private final BlockManager blockManager;
    private final PlayerManager playerManager;

    private GameStartCountdownTask gameStartCountdownTask;

    public GameManager(Murder plugin) {
        this.plugin = plugin;

        this.blockManager = new BlockManager(this);
        this.playerManager = new PlayerManager(this, plugin);
    }

    public void setGameState(Arena arena, GameState gameState){
        if (arena.getState() == gameState) return;

        arena.setState(gameState);
        // WAITING > STARTING > PLAYING > ENDING > RESTARTING
        if (this.gameState == GameState.WAITING && gameState == GameState.PLAYING) return;
        if (this.gameState == GameState.WAITING && gameState == GameState.ENDING) return;
        if (this.gameState == GameState.WAITING && gameState == GameState.RESTARTING) return;

        if (this.gameState == GameState.STARTING && gameState == GameState.WAITING) return;
        if (this.gameState == GameState.STARTING && gameState == GameState.ENDING) return;
        if (this.gameState == GameState.STARTING && gameState == GameState.RESTARTING) return;

        if (this.gameState == GameState.PLAYING && gameState == GameState.WAITING) return;
        if (this.gameState == GameState.PLAYING && gameState == GameState.STARTING) return;
        if (this.gameState == GameState.PLAYING && gameState == GameState.RESTARTING) return;

        if (this.gameState == GameState.ENDING && gameState == GameState.WAITING) return;
        if (this.gameState == GameState.ENDING && gameState == GameState.STARTING) return;
        if (this.gameState == GameState.ENDING && gameState == GameState.PLAYING) return;

        if (this.gameState == GameState.RESTARTING && gameState == GameState.STARTING) return;
        if (this.gameState == GameState.RESTARTING && gameState == GameState.PLAYING) return;
        if (this.gameState == GameState.RESTARTING && gameState == GameState.ENDING) return;

        this.gameState = gameState;

        switch (gameState){
            case WAITING:



                break;
            case STARTING:
                new GameStartCountdownTask(this, arena).runTaskTimer(plugin, 0, 20);
                break;
            case PLAYING:
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    arena.broadcastArena("&aO jogo iniciou!");
                }, 20L);
                break;
            case ENDING:
                Bukkit.broadcastMessage(ColorUtil.color("Game ENDING"));

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    plugin.getGameManager().setGameState(arena, GameState.RESTARTING);
                }, 20L*30);
                break;
            case RESTARTING:
                plugin.getArenaManager().finishArena(arena.getId());
                break;
            default:
                setGameState(arena, GameState.WAITING);
                Bukkit.broadcastMessage(ColorUtil.color("Default"));
                break;


        }
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
