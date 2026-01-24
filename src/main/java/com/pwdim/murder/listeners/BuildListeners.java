package com.pwdim.murder.listeners;

import com.pwdim.murder.manager.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListeners implements Listener {

    private GameManager gameManager;

    public BuildListeners(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent  e){
        if (!gameManager.getBlockManager().canBreak(e.getBlock())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if (!gameManager.getBlockManager().canPLace(e.getBlock())){
            e.setCancelled(true);
        }
    }



}
