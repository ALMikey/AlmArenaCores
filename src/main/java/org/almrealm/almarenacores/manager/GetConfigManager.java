package org.almrealm.almarenacores.manager;

import me.clip.placeholderapi.PlaceholderAPI;
import org.almrealm.almarenacores.AlmArenaCores;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class GetConfigManager {
    // 单例实例
    // 私有构造函数，确保只能通过getInstance方法获取实例
    private static GetConfigManager instance;
    // 插件实例
    private final AlmArenaCores plugin;
    // 配置文件
    private FileConfiguration config;
    private YamlConfiguration messagesConfig;
    public GetConfigManager(AlmArenaCores plugin) {
        this.plugin = plugin;
        // 初始化配置文件
        plugin.reloadConfig();
        // 获取插件config.yml
        this.config = plugin.getConfig();
        // 获取语言文件
        getMessage();
    }
    public String getString(String key) {
        return config.getString(key);
    }
    public int getInt(String key) {
        return config.getInt(key);
    }
    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public void getMessage(){
        String Language = getString("Language");
        String DefaultLanguage = "chinese";
        // 语言切换模块
        File messageFile = new File(plugin.getDataFolder(), "messages/"+Language+".yml");
        messagesConfig = YamlConfiguration.loadConfiguration(messageFile);
    }
    // 默认使用获取消息
    public String getMsg(String key) {
        String msg = messagesConfig.getString("messages." + key);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    // 替换玩家占位符方法
    public String getMsgPlayer(Player player, String key) {
        String msg = messagesConfig.getString("messages." + key);
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        return PlaceholderAPI.setPlaceholders(player, msg);
    }
    public String getMsgPrefix(String key) {
        String msg = messagesConfig.getString("messages." + key);
        String prefix = messagesConfig.getString("messages.prefix");
        String msglist = prefix+msg;
        return ChatColor.translateAlternateColorCodes('&', msglist);
    }
}
