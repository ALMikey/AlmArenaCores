package org.almrealm.almarenacores.command;

import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.almrealm.almarenacores.manager.ResourceFileManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class Command implements CommandExecutor, TabExecutor {

    private final AlmArenaCores plugin;
    public Command(AlmArenaCores plugin) { this.plugin = plugin; }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        // 执行命令时调用
        GetConfigManager gmc = new GetConfigManager(plugin);

        if (args.length < 1 ) return false;
        if (args[0].equalsIgnoreCase("version")){
            sender.sendMessage(gmc.getString("Version"));
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")){
            ResourceFileManager.getInstance(plugin).saveFile();
            plugin.reloadConfig();
            sender.sendMessage(gmc.getMsgPrefix("reload"));
            return true;
        }
        if (args[0].equalsIgnoreCase("help")){
            sender.sendMessage(gmc.getMsgPrefix("help"));
            return true;
        }
        if (args[0].equalsIgnoreCase("debug")){
            sender.sendMessage(gmc.getString("Version"));
            sender.sendMessage(gmc.getString("Language"));
            sender.sendMessage(gmc.getString("Arenaworld"));
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
