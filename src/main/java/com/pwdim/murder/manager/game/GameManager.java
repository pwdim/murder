package com.pwdim.murder.manager.game;


import com.pwdim.murder.Murder;
import com.pwdim.murder.itens.LobbyItem;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.manager.blocks.BlockManager;
import com.pwdim.murder.manager.player.PlayerManager;
import com.pwdim.murder.tasks.GameStartCountdownTask;
import org.bukkit.Bukkit;

public class GameManager {

    private final Murder plugin;
    private final BlockManager blockManager;
    private final PlayerManager playerManager;
    private GameStartCountdownTask gameStartCountdownTask;

    public GameManager(Murder plugin) {
        this.plugin = plugin;

        this.blockManager = new BlockManager(this);
        this.playerManager = new PlayerManager(this, plugin);
    }

    public void setGameState(Arena arena, GameState arenaState){
        this.gameStartCountdownTask = new GameStartCountdownTask(this, arena);
        if (!arena.getState().canTransitionTo(arenaState)){
            return;
        }
        arena.setState(arenaState);

        switch (arenaState){
            case WAITING:
                arena.getPlayers().forEach(uuid -> LobbyItem.giveItem(Bukkit.getPlayer(uuid)));
                break;
            case STARTING:
                new GameStartCountdownTask(this, arena).runTaskTimer(plugin, 0, 20);
                break;
            case PLAYING:
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    arena.getPlayers().forEach(uuid -> LobbyItem.removeItem(Bukkit.getPlayer(uuid)));
                    arena.broadcastArena("&aO jogo iniciou!");
                }, 20L);
                break;
            case ENDING:
                arena.titleArena("&c&lFIM DE JOGO!", "&eObrigado por jogar!", 1, 1000, 300);
                arena.getPlayers().forEach(uuid -> LobbyItem.giveItem(Bukkit.getPlayer(uuid)));

                Bukkit.getScheduler().runTaskLater(plugin, () -> plugin.getGameManager().setGameState(arena, GameState.RESTARTING), 20L*15);
                break;
            case RESTARTING:
                arena.getPlayers().forEach(uuid -> LobbyItem.removeItem(Bukkit.getPlayer(uuid)));
                plugin.getArenaManager().finishArena(arena.getId());
                break;
            default:
                setGameState(arena, GameState.WAITING);
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
