package com.pwdim.murder.commands;


import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.manager.game.GameManager;
import com.pwdim.murder.manager.game.GameState;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GameCommand implements CommandExecutor {
    private final GameManager gameManager;
    private final Murder plugin;

    public GameCommand(Murder plugin, GameManager gameManager){
        this.plugin = plugin;
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player)){
            sender.sendMessage(MessageUtils.consoleError());
            return true;
        }

        Player p = (Player) sender;
        if (!(p.hasPermission("arcade.manage"))){
            p.sendMessage(MessageUtils.noPermMSG());
            return true;
        }
        Arena arenaAtual = plugin.getArenaManager().getActiveArenas().values().stream()
                .filter(a -> a.getWorld().getName().equals(p.getWorld().getName()))
                .findFirst()
                .orElse(null);
        if (arenaAtual == null){
            p.sendMessage(ColorUtil.color("&eVocẽ precisa estar em uma sala para executar esse comando!"));
            return true;
        }

        if (args.length == 0){
            p.sendMessage(ColorUtil.color("&cUse: /game <start/stop>"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand){
            case "start":
                gameManager.setGameState(arenaAtual, GameState.STARTING);
                p.sendMessage(ColorUtil.color("&aForçando início da arena: " + arenaAtual.getId()));
                break;
            case "stop":
                gameManager.setGameState(arenaAtual, GameState.ENDING);
                p.sendMessage(ColorUtil.color("&cForçando encerramento da arena."));
                break;
            default:
                p.sendMessage(ColorUtil.color("&cSubcomando inválido."));
                break;
        }


        return true;
    }
}
