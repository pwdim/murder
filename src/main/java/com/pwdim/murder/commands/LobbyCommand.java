package com.pwdim.murder.commands;

import com.pwdim.murder.Murder;
import com.pwdim.murder.itens.LobbyItem;
import com.pwdim.murder.utils.ConfigUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    private final Murder plugin;
    public LobbyCommand(Murder plugin){
        this.plugin = plugin;
    }


        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if (!(sender instanceof Player)){
                sender.sendMessage(ConfigUtils.consoleError());
                return true;
            }
            Player p = (Player) sender;

            if (!ConfigUtils.inGame(p)){
                p.sendMessage(ConfigUtils.alreadyLobby());
                return true;
            }
            p.teleport(ConfigUtils.getLobby());
            if (p.getInventory().contains(LobbyItem.lobbyItem())){
                LobbyItem.removeItem(p);
            }



        return true;
    }
}
