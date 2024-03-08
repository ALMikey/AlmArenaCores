package org.almrealm.almarenacores.command;

import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.DataStorageManager;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.almrealm.almarenacores.manager.ResourceFileManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class PluginCommand implements CommandExecutor, TabExecutor {

    private final AlmArenaCores plugin;
    public PluginCommand(AlmArenaCores plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // 执行命令时调用
        GetConfigManager gcm = new GetConfigManager(plugin);
        DataStorageManager dsm = new DataStorageManager(plugin);

        if (args.length < 1 ) return false;
        if (args[0].equalsIgnoreCase("version")){
            sender.sendMessage(gcm.getString("Version"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")){
            ResourceFileManager.getInstance(plugin).saveFile();
            plugin.reloadConfig();
            dsm.loadData();
            sender.sendMessage(gcm.getMsgPrefix("reload"));
            return true;
        }
        if (args[0].equalsIgnoreCase("help")){
            sender.sendMessage(gcm.getMsgPrefix("help"));
            return true;
        }
        if (args[0].equalsIgnoreCase("modelengine")){
            sender.sendMessage(gcm.getMsgPrefix("help"));
            return true;
        }
        if (args[0].equalsIgnoreCase("debug")){

            sender.sendMessage("##### 数据存储 #####");
            sender.sendMessage(gcm.getDataConfig("data-storage-method"));
            sender.sendMessage(gcm.getDataConfig("MySQL.host"));
            sender.sendMessage(gcm.getDataConfig("MySQL.port"));
            sender.sendMessage(gcm.getDataConfig("MySQL.user"));
            sender.sendMessage(gcm.getDataConfig("MySQL.password"));
            sender.sendMessage(gcm.getDataConfig("MySQL.database"));
            sender.sendMessage(gcm.getDataConfig("MySQL.table"));
            sender.sendMessage("###############");
            sender.sendMessage(" ");
            sender.sendMessage("##### ModelEngine #####");
            sender.sendMessage(gcm.getString("ModelEngine.model-1"));
            sender.sendMessage("###############");

            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        List<String> tabCompletions = new ArrayList<>();

        if (args.length == 1) {
            tabCompletions.add("version");
            tabCompletions.add("reload");
            tabCompletions.add("help");
            tabCompletions.add("debug");
        }

        return tabCompletions;
    }
}
