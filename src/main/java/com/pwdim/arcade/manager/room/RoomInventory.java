package com.pwdim.arcade.manager.room;

import com.pwdim.arcade.Arcade;
import com.pwdim.arcade.manager.arena.Arena;
import com.pwdim.arcade.utils.ColorUtil;
import com.pwdim.arcade.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RoomInventory {

    private static Arcade plugin;
    private static final int ITEMS_PER_PAGE = 28;

    public RoomInventory(Arcade plugin){
        this.plugin = plugin;
    }

    public static Inventory getInventory(int page){
        Inventory inventory = Bukkit.createInventory(null, 54, ColorUtil.color("Menu de salas  (" + (page + 1) +")"));

        List<Arena> arenaList = new ArrayList<>(plugin.getArenaManager().getActiveArenas().values());

        if (arenaList.isEmpty()){
            inventory.setItem(22, RoomItem.nullItem());
            return inventory;
        }

        int startIndex = page * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, arenaList.size());

        int slot = 10;
        for (int i = startIndex; i < endIndex; i++){
            if ((slot + 1) % 9 == 0) slot += 2;
            Arena checkArena = arenaList.get(i);
            ItemStack item = RoomItem.roomItem(checkArena);
            item = NMSUtils.setCustomNBT(item, "arenaID", checkArena.getId());
            inventory.setItem(slot, item);

            slot++;
        }

        if (page > 0){
            inventory.setItem(45, RoomItem.backPageItem());
        }
        if (endIndex < arenaList.size()){
            inventory.setItem(53, RoomItem.nextPageItem());
        }

        return inventory;
    }

}