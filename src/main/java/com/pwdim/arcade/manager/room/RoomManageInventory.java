package com.pwdim.arcade.manager.room;

import com.pwdim.arcade.Arcade;
import com.pwdim.arcade.manager.arena.Arena;
import com.pwdim.arcade.utils.ColorUtil;
import com.pwdim.arcade.utils.GeralUtils;
import com.pwdim.arcade.utils.NMSUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RoomManageInventory implements Listener {

    private static Arcade plugin;

    public RoomManageInventory(Arcade plugin){
        RoomManageInventory.plugin = plugin;
    }

    public static ItemStack deleteRoomItem(){
        ItemStack item = new ItemStack(Material.BARRIER, 1);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.color("&cCancelar partida e excluir mundo"));
        meta.setLore(lore);
        meta.setDisplayName(ColorUtil.color("&4&lREMOVER SALA"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack playersRoomItem(Player p){
        ItemStack item = GeralUtils.getHead(p);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.color("&eVerifique os jogadores da partida"));
        meta.setLore(lore);
        meta.setDisplayName(ColorUtil.color("&a&lLISTA DE JOGADORES"));
        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack confirmRemoveRoomItem(){
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.color("&aRemover sala"));
        meta.setLore(lore);
        meta.setDisplayName(ColorUtil.color("&a&lCONFIRMAR"));
        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack cancelRemoveRoomItem(){
        ItemStack item = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ColorUtil.color("&cNão remover sala"));
        meta.setLore(lore);
        meta.setDisplayName(ColorUtil.color("&c&lCANCELAR"));
        item.setItemMeta(meta);

        return item;
    }

    private static ItemStack fillItem(){
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ColorUtil.color("&r"));
        item.setItemMeta(meta);

        return item;
    }


    public static Inventory deleteRoomInventory(Arena arena) {
        Inventory inv = Bukkit.createInventory(null, 45, ColorUtil.color("&eRemover Sala? "));

        for (int i = 9; i < 18; i++) {
            inv.setItem(i, fillItem());
        }

        inv.setItem(4, RoomItem.roomItem(arena));

        ItemStack confirm = confirmRemoveRoomItem();
        confirm = NMSUtils.setCustomNBT(confirm, "manageArenaID", arena.getId());
        confirm = NMSUtils.setCustomNBT(confirm, "action", "confirm_delete");

        ItemStack cancel = cancelRemoveRoomItem();
        cancel = NMSUtils.setCustomNBT(cancel, "manageArenaID", arena.getId());
        cancel = NMSUtils.setCustomNBT(cancel, "action", "cancel_delete");

        inv.setItem(21, cancel);
        inv.setItem(23, confirm);

        return inv;
    }

    public static Inventory playersListInventory(Arena arena) {
        Inventory inv = Bukkit.createInventory(null, 45, ColorUtil.color("&eLista de Jogadores "));

        for (int i = 9; i < 18; i++) {
            inv.setItem(i, fillItem());
        }
        if (arena == null){
            inv.setItem(21, RoomItem.nullItem());
            return inv;
        }
        inv.setItem(4, RoomItem.roomItem(arena));
        int i = 20;

        if (!arena.getPlayers().isEmpty()){
            for (UUID uuid : arena.getPlayers()){
                Player p = Bukkit.getPlayer(uuid);
                ItemStack head = GeralUtils.getHead(p);
                head = NMSUtils.setCustomNBT(head, "manageArenaID", arena.getId());
                head = NMSUtils.setCustomNBT(head, "managePlayer", p.getUniqueId().toString());
                head = NMSUtils.setCustomNBT(head,"action", "arena_players_manage");

                inv.setItem(i, head);
                i++;
            }
        } else {
            inv.setItem(21, RoomItem.nullItem());
        }


        return inv;
    }

    public static Inventory manageInventory(Arena arena, @Nullable Player p){
        Inventory inv = Bukkit.createInventory(null, 45, ColorUtil.color("&eGerenciar Sala"));

        for (int i = 9; i < 18; i++) {
            inv.setItem(i, fillItem());
        }

        inv.setItem(4, RoomItem.roomItem(arena));

        ItemStack delete = deleteRoomItem();
        delete = NMSUtils.setCustomNBT(delete, "manageArenaID", arena.getId());
        delete = NMSUtils.setCustomNBT(delete,"action", "arena_delete");

        ItemStack list = playersRoomItem(p);
        list = NMSUtils.setCustomNBT(list, "manageArenaID", arena.getId());
        list = NMSUtils.setCustomNBT(list, "action", "arena_players");

        inv.setItem(20, delete);
        inv.setItem(21, list);

        return inv;
    }


}
