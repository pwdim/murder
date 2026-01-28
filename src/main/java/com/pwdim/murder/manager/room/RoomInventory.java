package com.pwdim.murder.manager.room;

import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class RoomInventory {

    private final Murder plugin;
    private static final int ITEMS_PER_PAGE = 28;

    public RoomInventory(Murder plugin){
        this.plugin = plugin;
    }

    public Inventory getInventory(int page){
        Inventory inventory = Bukkit.createInventory(null, 54, ColorUtil.color("&bMenu de salas &7- Pág: &e" + (page + 1)));

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

            inventory.setItem(slot, RoomItem.roomItem(arenaList.get(i)));
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
    //    public static Inventory roomMenuInventory(){
//        Inventory inventory = Bukkit.createInventory(null, 54, ColorUtil.color("&eMenu de Salas"));
//        AtomicInteger arenaItemCount = new AtomicInteger(9);
//
//        if (plugin.getArenaManager().getActiveArenas().isEmpty()) {
//            inventory.setItem(21, RoomItem.nullItem());
//        } else {
//            plugin.getArenaManager().getActiveArenas()
//                    .forEach((s, arena) -> {
//                        arenaItemCount.getAndIncrement();
//                        inventory.setItem(arenaItemCount.get(), RoomItem.roomItem(arena));
//                    });
//        }
//
//        return inventory;
//    }
}