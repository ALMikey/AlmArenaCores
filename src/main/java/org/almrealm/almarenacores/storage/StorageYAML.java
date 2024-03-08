package org.almrealm.almarenacores.storage;

import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.RankPointsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class StorageYAML {
    private final AlmArenaCores plugin;

    public StorageYAML(AlmArenaCores plugin) {
        this.plugin = plugin;
        loadData();
    }
    private static RankPointsManager instance;
    private File playerdataFile;
    private FileConfiguration playerdataConfig;

    private void loadData() {
        playerdataFile = new File(plugin.getDataFolder(), "playerdata.yml");
        playerdataConfig = YamlConfiguration.loadConfiguration(playerdataFile);
    }

    public void addPoints(Player player, int points) {
        int currentPoints = playerdataConfig.getInt(player.getName());
        playerdataConfig.set(player.getName(), currentPoints + points);
        save();
    }

    public void removePoints(Player player, int points) {
        int currentPoints = playerdataConfig.getInt(player.getName());
        playerdataConfig.set(player.getName(), Math.max(currentPoints - points, 0)); // 防止最低点
        save();
    }
    private void save() {
        try {
            playerdataConfig.save(playerdataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 单例实例
    public static RankPointsManager getInstance(AlmArenaCores plugin) {
        if (instance == null) {
            instance = new RankPointsManager(plugin);
        }
        return instance;
    }
}
