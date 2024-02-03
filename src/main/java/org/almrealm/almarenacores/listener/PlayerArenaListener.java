package org.almrealm.almarenacores.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.almrealm.almarenacores.manager.RankPointsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerArenaListener implements Listener {
    private final AlmArenaCores plugin;
    public PlayerArenaListener(AlmArenaCores plugin) { this.plugin = plugin; }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player killer = event.getEntity().getKiller();

        if(killer != null){
            GetConfigManager gcm = new GetConfigManager(plugin);
            String ArenaWorld = gcm.getString("Arenaworld");

            Player victim = event.getEntity();

            if (killer.getWorld().getName().equalsIgnoreCase(ArenaWorld)){
                sendKillMessage(killer, victim);
                RankPointsManager.getInstance(plugin).
                        addPoints(killer, gcm.getInt("ArenaPoints.win"));
                RankPointsManager.getInstance(plugin).
                        removePoints(victim, gcm.getInt("ArenaPoints.fial"));
            }

        }
    }

    private void sendKillMessage(Player killer, Player victim) {
        GetConfigManager gcm = new GetConfigManager(plugin);
        // 在这里编写发送提示信息的逻辑
        // Actionbar messages
        if (gcm.getBoolean("PVP-message.actionbar")){
            String killerMsg = gcm.getMsgPlayer(victim, "actionbar.kill-player");
            String victimMsg= gcm.getMsgPlayer(killer, "actionbar.killed-by-player");
            killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(killerMsg));
            victim.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(victimMsg));
        }

        // Player messages
        if (gcm.getBoolean("PVP-message.message")){
            killer.sendMessage(gcm.getMsgPlayer(victim, "message.kill-player"));
            victim.sendMessage(gcm.getMsgPlayer(killer, "message.killed-by-player"));
        }

        // Title messages
        if(gcm.getBoolean("PVP-message.title.enable")){
            killer.sendTitle(gcm.getMsgPlayer(victim, "title.killer-title"),gcm.getMsg("title.killer-sub"),
                    gcm.getInt("PVP-message.title.fadeIn"),
                    gcm.getInt("PVP-message.title.stay"),
                    gcm.getInt("PVP-message.title.fadeOut"));
            victim.sendTitle(gcm.getMsgPlayer(killer, "title.victim-title"),gcm.getMsg("title.victim-sub"),
                    gcm.getInt("PVP-message.title.fadeIn"),
                    gcm.getInt("PVP-message.title.stay"),
                    gcm.getInt("PVP-message.title.fadeOut"));
        }
    }
}
