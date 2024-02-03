package org.almrealm.almarenacores.manager;

import org.almrealm.almarenacores.AlmArenaCores;

import java.io.File;

public class ResourceFileManager {
    private final AlmArenaCores plugin;
    private static ResourceFileManager instance;
    private File chineseFile;
    private File englishFile;
    private File playerData;
    private File databaseFile;

    public ResourceFileManager(AlmArenaCores plugin) {
        this.plugin = plugin;
    }

    public void saveFile() {
        // 获取插件数据文件夹
        // File dataFolder = plugin.getDataFolder();
        chineseFile = new File(plugin.getDataFolder(),"messages/chinese.yml");
        englishFile = new File(plugin.getDataFolder(),"messages/english.yml");
        playerData = new File(plugin.getDataFolder(),"playerdata.yml");
        databaseFile = new File(plugin.getDataFolder(),"playerdata.yml");

        // 如果文件不存在...
        if (!chineseFile.exists()){
            plugin.saveResource("messages/chinese.yml", false);
        }
        if (!englishFile.exists()){
            plugin.saveResource("messages/english.yml", false);
        }
        if (!playerData.exists()){
            plugin.saveResource("playerdata.yml", false);
        }
        if (!databaseFile.exists()){
            plugin.saveResource("database.yml", false);
        }

        plugin.saveDefaultConfig();
    }

    // 单例实例
    public static ResourceFileManager getInstance(AlmArenaCores plugin) {
        if (instance == null) {
            instance = new ResourceFileManager(plugin);
        }
        return instance;
    }
}
