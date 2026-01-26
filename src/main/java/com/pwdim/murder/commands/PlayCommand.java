package com.pwdim.murder.commands;

import com.pwdim.murder.Murder;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayCommand implements CommandExecutor {
    private final Murder plugin;

    public PlayCommand(Murder plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage(ConfigUtils.consoleError());
            return true;
        }

        Player p = (Player) sender;
        boolean inGame = plugin.getArenaManager().getActiveArenas().values().stream()
                .anyMatch(arena -> arena.getPlayers().contains(p.getUniqueId()));

        if (!inGame) {
            plugin.getPlayerManager().sendToArena(p);
            return true;
        }
        p.sendMessage(ColorUtil.color("&cVocê já está em partida"));



        return true;
    }
}
