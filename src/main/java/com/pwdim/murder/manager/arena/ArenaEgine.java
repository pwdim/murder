package com.pwdim.murder.manager.arena;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import com.pwdim.murder.Murder;
import com.pwdim.murder.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public class ArenaEgine implements Listener {

    private final Murder plugin;
    private final SlimePlugin slimePlugin;
    private final SlimeLoader loader;

    public ArenaEgine(Murder plugin) {
        this.plugin = plugin;
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        this.loader = slimePlugin.getLoader("file");
    }

    public void createWorldInstace(String templateName, String instaceName, Consumer<World> callback) {
        SlimePropertyMap slimePropertyMap = new SlimePropertyMap();
        slimePropertyMap.setInt(SlimeProperties.SPAWN_X, ConfigUtils.getMapSpawn().getBlockX());
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Y, ConfigUtils.getMapSpawn().getBlockY());
        slimePropertyMap.setInt(SlimeProperties.SPAWN_Z, ConfigUtils.getMapSpawn().getBlockZ());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                SlimeWorld template = slimePlugin.loadWorld(loader, templateName, true, slimePropertyMap);

                SlimeWorld instace = template.clone(instaceName);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    slimePlugin.generateWorld(instace);

                    World bukkitWorld = Bukkit.getWorld(instaceName);
                    if (bukkitWorld != null && callback != null) {
                        callback.accept(bukkitWorld);
                    }
                });

            } catch (Exception e) {
                Bukkit.getScheduler().runTask(plugin, () ->
                        plugin.logger("&4&lERROR NO SWM: &c" + e.getMessage())
                );
            }
        });
    }

    public void deleteWorldInstace(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.unloadWorld(world, false));
        }
    }
}
