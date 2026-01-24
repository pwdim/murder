package com.pwdim.murder.manager.blocks;


import com.pwdim.murder.manager.game.GameManager;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

public class BlockManager {

    private GameManager gameManager;

    public BlockManager(GameManager gameManager) {
        this.gameManager =  gameManager;


        allowedToBreak.add(Material.BEDROCK);
        allowedToPlace.add(Material.BEDROCK);
    }

    private Set<Material> allowedToBreak = new HashSet<>();
    private Set<Material> allowedToPlace = new HashSet<>();

    public boolean canBreak(Block block) { return allowedToBreak.contains(block.getType());}

    public boolean canPLace(Block block) { return allowedToBreak.contains(block.getType());}

}
