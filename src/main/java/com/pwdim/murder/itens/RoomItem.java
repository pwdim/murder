package com.pwdim.murder.itens;

import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RoomItem implements Listener {

    private static Murder plugin;

    public RoomItem(Murder plugin){
        this.plugin = plugin;
    }


    private static ItemStack roomItem(Arena arena){
        ItemStack item = null;
        List<String> lore = new ArrayList<>();
        String state = "";
        switch (arena.getState()){
            case WAITING:
                state = "&eEsperando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                break;
            case STARTING:
                state = "&aIniciando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                break;
            case PLAYING:
                state = "&2Jogando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                break;
            case ENDING:
                state = "&6Finalizando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                break;
            case RESTARTING:
                state = "&cReiniciando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
                break;
        }
        lore.add(ColorUtil.color("&bMapa: &a" + arena.getMapName()));
        lore.add(ColorUtil.color("&bJogadores: &a" + arena.getPlayers().size() + "&b/" + ConfigUtils.getMaxPLayers()));
        lore.add(ColorUtil.color("&bStatus: " + state));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.color("&b&o" + arena.getId()));
        meta.setLore(lore);
        item.setItemMeta(meta);

        if (item != null){
            return item;
        } else {
            return new ItemStack(Material.BEDROCK, 1, (short) 4);
        }
    }

    public static Inventory roomMenuInventory(){
        Inventory inventory = Bukkit.createInventory(null, 54, ColorUtil.color("&eMenu de Salas"));
        int arenaItemCount = 9;

        plugin.getArenaManager().getActiveArenas()
                .forEach((s, arena) -> {
                    inventory.setItem(arenaItemCount+1, roomItem(arena));
                });


        return inventory;
    }


}
