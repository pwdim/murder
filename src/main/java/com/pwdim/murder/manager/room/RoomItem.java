package com.pwdim.murder.manager.room;

import com.pwdim.murder.Murder;
import com.pwdim.murder.manager.arena.Arena;
import com.pwdim.murder.utils.ColorUtil;
import com.pwdim.murder.utils.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RoomItem implements Listener {

    private static Murder plugin;

    public RoomItem(Murder plugin){
        this.plugin = plugin;
    }


    public static ItemStack roomItem(Arena arena){
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
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
                break;
            case PLAYING:
                state = "&2Jogando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
                break;
            case ENDING:
                state = "&6Finalizando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1);
                break;
            case RESTARTING:
                state = "&cReiniciando";
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                break;
        }
        lore.add(ColorUtil.color("&eMapa: &a" + arena.getMapName()));
        lore.add(ColorUtil.color("&eJogadores: &a" + arena.getPlayers().size() + "&e/&a" + ConfigUtils.getMaxPLayers()));
        lore.add(ColorUtil.color("&eStatus: " + state));

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.color("&b&o" + arena.getId()));
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack nullItem(){
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ColorUtil.color("&cNenhuma sala encontrada!"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack nextPageItem(){
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ColorUtil.color("&aPróxima Página"));
        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack backPageItem(){
        ItemStack item = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ColorUtil.color("&ePágina Anterior"));
        item.setItemMeta(meta);

        return item;

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().getTitle().contains("Salas - Página")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int currentPage = Integer.parseInt(event.getInventory().getTitle().replaceAll("[^0-9]", "")) - 1;
        RoomInventory gui = new RoomInventory(plugin);

        if (event.getSlot() == 53 && event.getCurrentItem().getType() != Material.AIR) {
            player.openInventory(gui.getInventory(currentPage + 1));
        } else if (event.getSlot() == 45 && event.getCurrentItem().getType() != Material.AIR) {
            player.openInventory(gui.getInventory(currentPage - 1));
        }
    }


}
