package org.almrealm.almarenacores.listener;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.almrealm.almarenacores.storage.StorageMySQL;
import org.almrealm.almarenacores.storage.StorageYAML;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
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

            Player victim = event.getEntity();

            GetConfigManager gcm = new GetConfigManager(plugin);
            String ArenaWorld = gcm.getString("Arenaworld");

            World world = Bukkit.getWorld(ArenaWorld);

            StorageYAML yamlSave = new StorageYAML(plugin);
            StorageMySQL mysqlSave = new StorageMySQL(plugin);

            // 获取玩家当前位置的坐标
            double x = victim.getLocation().getX();
            double y = victim.getLocation().getY();
            double z = victim.getLocation().getZ();

            Location location = new Location(world,x,y,z);

            if (killer.getWorld().getName().equalsIgnoreCase(ArenaWorld)){

                sendKillMessage(killer, victim);

                //RankPointsManager.getInstance(plugin).
                //        addPoints(killer, gcm.getInt("ArenaPoints.win"));
                //RankPointsManager.getInstance(plugin).
                //        removePoints(victim, gcm.getInt("ArenaPoints.fial"));

                if (gcm.getDataConfig("data-storage-method").equalsIgnoreCase("MySQL")){
                    // Mysql
                    mysqlSave.addPointsAsync(killer, gcm.getInt("ArenaPoints.win"));
                    mysqlSave.removePointsAsync(victim, gcm.getInt("ArenaPoints.fial"));
                }else if (gcm.getDataConfig("data-storage-method").equalsIgnoreCase("YAML")) {
                    // YAML
                    yamlSave.addPoints(killer, gcm.getInt("ArenaPoints.win"));
                    yamlSave.removePoints(victim, gcm.getInt("ArenaPoints.fial"));
                }

                // Spawn a new entity
                ArmorStand entity = location.getWorld().spawn(location, ArmorStand.class, (armorStand) -> {
                    armorStand.setCustomName("a");
                    armorStand.setCustomNameVisible(true);
                    armorStand.setInvisible(true);
                });
                // Create a new ModeledEntity from the spawned entity
                ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
                // Create a new ActiveModel using the ID of a model
                // Will throw an error if the model does not exist
                ActiveModel activeModel = ModelEngineAPI.createActiveModel(gcm.getString("ModelEngine.model-1"));
                // Add the model to the entity
                modeledEntity.addModel(activeModel, true);

                Bukkit.getScheduler().runTaskLater(plugin, (Runnable) () -> {
                    activeModel.destroy();
                    entity.remove();
                }, (long) (activeModel.getBlueprint().getAnimations().get("spawn").getLength()*20));

                sendKillMessage(killer,victim);
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
