package org.almrealm.almarenacores.storage;

import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.*;

public class StorageMySQL {
    private final AlmArenaCores plugin;
    public StorageMySQL(AlmArenaCores plugin) {
        this.plugin = plugin;
    }
    public void loadData(){
        GetConfigManager gcm = new GetConfigManager(plugin);
        // JDBC 信息
        String host = gcm.getDataConfig("MySQL.host");
        String port = gcm.getDataConfig("MySQL.port");
        String user = gcm.getDataConfig("MySQL.user");
        String password = gcm.getDataConfig("MySQL.password");
        String database = gcm.getDataConfig("MySQL.database");
        String table = gcm.getDataConfig("MySQL.table");
        // String url = "jdbc:mysql://localhost:3306/mydatabase";
        String url = "jdbc:mysql://"+host+":"+port+"/"+database;
        plugin.getLogger().info(url);
        // 加载MySQL驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC driver.");
            e.printStackTrace();
            return;
        }
        // 建立数据库连接
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // 创建Statement对象
            Statement statement = connection.createStatement();
            // 执行SQL查询
            String sql = "SELECT * FROM "+table;
            ResultSet resultSet = statement.executeQuery(sql);
            // 处理查询结果
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String ip = resultSet.getString("ip");
                plugin.getLogger().info(id+" "+username+" "+ip);
            }
        } catch (SQLException e) {
            System.err.println("Database access error:");
            e.printStackTrace();
        }
    }

    public void addPoints(Player player, int points) {

    }

    public void removePoints(Player player, int points) {

    }
    private void save() {

    }
}
