package com.pwdim.arcade.manager.room;

import com.pwdim.arcade.Arcade;
import com.pwdim.arcade.manager.arena.Arena;
import com.pwdim.arcade.utils.ColorUtil;
import com.pwdim.arcade.utils.ConfigUtils;
import com.pwdim.arcade.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
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

        meta.setDisplayName(ColorUtil.color("&cNada encontrado!"));
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
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;


        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        String title = e.getInventory().getTitle();

        if (title.contains("Menu de salas")) {
            if (e.getSlot() == 53 && item.getType() == Material.ARROW) {
                int currentPage = extractPage(title);
                player.openInventory(RoomInventory.getInventory(currentPage + 1));
            } else if (e.getSlot() == 45 && item.getType() == Material.ARROW) {
                int currentPage = extractPage(title);
                if (currentPage > 0) {
                    player.openInventory(RoomInventory.getInventory(currentPage - 1));
                }
            }

            String arenaID = NMSUtils.getCustomNBT(item, "arenaID");
            if (arenaID != null) {
                Arena arena = plugin.getArenaManager().getArena(arenaID);
                if (arena != null) {
                    player.openInventory(RoomManageInventory.manageInventory(arena, player));
                }
            }
        }


        String action = NMSUtils.getCustomNBT(item, "action");
        if (action != null) {
            String manageArenaID = NMSUtils.getCustomNBT(item, "manageArenaID");

            switch (action) {
                case "confirm_delete":
                    plugin.getArenaManager().finishArena(manageArenaID);
                    player.sendMessage(ColorUtil.color("&bArena &c" + manageArenaID + " &bfinalizada com sucesso"));
                    plugin.getArenaManager().getArena(manageArenaID).getPlayers().forEach(
                            uuid -> Bukkit.getPlayer(uuid).sendMessage(ColorUtil.color("&cA sala que você estava foi interrompida!")));
                    player.playNote(player.getLocation(), Instrument.PIANO, Note.natural(0, Note.Tone.A));
                    player.closeInventory();
                    break;
                case "cancel_delete":
                    player.closeInventory();
                    player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.A));
                    break;
                case "arena_delete":
                    player.openInventory(RoomManageInventory.deleteRoomInventory(plugin.getArenaManager().getArena(manageArenaID)));
                    break;
                case "arena_players":
                    player.openInventory(RoomManageInventory.playersListInventory(manageArenaID));
                    break;
            }
        }
    }

    private int extractPage(String title) {
        try {
            String parts = title.substring(title.lastIndexOf("(") + 1, title.lastIndexOf(")"));
            return Integer.parseInt(parts) - 1;
        } catch (Exception ex) {
            return 0;
        }
    }


}
