package org.almrealm.almarenacores.manager;

import org.almrealm.almarenacores.AlmArenaCores;

import java.sql.*;
import java.util.logging.Logger;

public class DataStorageManager {

    private final AlmArenaCores plugin;
    public DataStorageManager(AlmArenaCores plugin) {
        this.plugin = plugin;
    }

    public void loadData(){

        GetConfigManager gcm = new GetConfigManager(plugin);

        if (gcm.getDataConfig("data-storage-method").equals("MySQL")){

            plugin.getLogger().info("已启用 MySql 数据模式！");

        }
        else if (gcm.getDataConfig("data-storage-method").equals("YAML")){

            plugin.getLogger().info("已启用 YAML 数据模式！");

        } else {

            plugin.getLogger().info("请填写正确的数据存储模式!");

        }

    }
}
