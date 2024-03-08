package org.almrealm.almarenacores;

import org.almrealm.almarenacores.command.PluginCommand;
import org.almrealm.almarenacores.listener.PlayerArenaListener;
import org.almrealm.almarenacores.manager.DataStorageManager;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.almrealm.almarenacores.manager.RankPointsManager;
import org.almrealm.almarenacores.manager.ResourceFileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class AlmArenaCores extends JavaPlugin {

    public void onLoad() {

        getLogger().info("Loading...");

    }
    @Override
    public void onEnable() {

        getLogger().info("一一一一一一一一一一一一一一一");
        getLogger().info("Welcome to use！！");
        getLogger().info("一一一一一一一一一一一一一一一");

        GetConfigManager getConfigManager = new GetConfigManager(this);
        ResourceFileManager resourceFileManager = new ResourceFileManager(this);
        RankPointsManager rankPointsManager = new RankPointsManager(this);
        DataStorageManager dataStorageManager = new DataStorageManager(this);

        // Resource File Manager
        resourceFileManager.saveFile();

        // Listener
        getServer().getPluginManager().registerEvents(new PlayerArenaListener(this),this);

        // Command
        getServer().getPluginCommand("almarenacore").setExecutor(new PluginCommand(this));


    }

    @Override
    public void onDisable() {

        getLogger().info("Hey Goodbye/~");

    }

    public static AlmArenaCores getInstance(){
        return getPlugin(AlmArenaCores.class);
    }
}
