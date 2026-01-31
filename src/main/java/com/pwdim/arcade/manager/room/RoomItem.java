package com.pwdim.arcade.manager.room;

import com.pwdim.arcade.Arcade;
import com.pwdim.arcade.manager.arena.Arena;
import com.pwdim.arcade.utils.ColorUtil;
import com.pwdim.arcade.utils.ConfigUtils;
import com.pwdim.arcade.utils.NMSUtils;
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

    private final Arcade plugin;

    public RoomItem(Arcade plugin){
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
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        int currentPage = Integer.parseInt(e.getInventory().getTitle().replaceAll("[^0-9]", "")) - 1;
        RoomInventory gui = new RoomInventory(plugin);

        if (e.getSlot() == 53 && e.getCurrentItem().equals(nextPageItem())) {
            player.openInventory(gui.getInventory(currentPage + 1));
        } else if (e.getSlot() == 45 && e.getCurrentItem().equals(backPageItem())) {
            player.openInventory(gui.getInventory(currentPage - 1));
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        String action = NMSUtils.getCustomNBT(item, "action");
        String arenaID = NMSUtils.getCustomNBT(item, "manageArenaID");

        if ("confirm_delete".equals(action)) {
            event.getWhoClicked().sendMessage("§cArena " + arenaID + " removida com sucesso!");
            event.getWhoClicked().closeInventory();
        }
    }


}
