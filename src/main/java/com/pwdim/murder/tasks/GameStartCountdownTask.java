package com.pwdim.murder.tasks;

import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.manager.game.GameManager;
import com.pwdim.murder.manager.game.GameState;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCountdownTask extends BukkitRunnable {

    private GameManager gameManager;
    private final Arena arena;

    public GameStartCountdownTask(GameManager gameManager, Arena arena){
        this.gameManager = gameManager;
        this.arena = arena;
    }


    private int timeLeft = MessageUtils.timeLeft() + 1;
    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 3 && timeLeft >= 1){
            arena.titleArena("&c" + timeLeft, null, 1, 20, 20);
        }

        if (timeLeft <= 1){
            cancel();
            gameManager.setGameState(arena, GameState.PLAYING);
        }
        arena.broadcastArena("&b" + timeLeft + " &esegundos para o jogo iniciar!");

    }
}
