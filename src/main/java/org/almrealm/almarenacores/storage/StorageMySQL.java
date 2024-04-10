package org.almrealm.almarenacores.storage;

import org.almrealm.almarenacores.AlmArenaCores;
import org.almrealm.almarenacores.manager.GetConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.*;
import java.util.function.Consumer;

public class StorageMySQL {
    private final AlmArenaCores plugin;
    private final String host;
    private final String port;
    private final String user;
    private final String password;
    private final String database;
    private final String table;

    private Connection connection; // 数据库连接对象

    public StorageMySQL(AlmArenaCores plugin) {
        this.plugin = plugin;
        GetConfigManager gcm = new GetConfigManager(plugin);
        this.host = gcm.getDataConfig("MySQL.host");
        this.port = gcm.getDataConfig("MySQL.port");
        this.user = gcm.getDataConfig("MySQL.user");
        this.password = gcm.getDataConfig("MySQL.password");
        this.database = gcm.getDataConfig("MySQL.database");
        this.table = gcm.getDataConfig("MySQL.table");

        connect(); // 数据库连接对象
    }

    // 连接数据库
    private void connect() {
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";
            connection = DriverManager.getConnection(url, user, password);
            plugin.getLogger().info("Connected to the database successfully.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to the database: " + e.getMessage());
        }
    }

    // 检查表格是否存在，如果不存在则创建
    public void ensureTableExists() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + table + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "player_uuid VARCHAR(36) NOT NULL,"
                + "points INT DEFAULT 0,"
                + "UNIQUE (player_uuid)"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
            plugin.getLogger().info("Table " + table + " has been created or already exists.");
        } catch (SQLException e) {
            plugin.getLogger().severe("Error creating table " + table + ": " + e.getMessage());
        }
    }

    // 检查并确保玩家记录存在，如果不存在则创建新记录
    private void ensurePlayerRecordExists(Player player) {
        String queryCheck = "SELECT * FROM " + table + " WHERE player_uuid = ?";
        String queryInsert = "INSERT INTO " + table + " (player_uuid, points) VALUES (?, ?);";

        try (PreparedStatement preparedStatementCheck = connection.prepareStatement(queryCheck);
             PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert)) {
            preparedStatementCheck.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatementCheck.executeQuery();

            // 如果没有找到记录，则插入新记录
            if (!resultSet.next()) {
                preparedStatementInsert.setString(1, player.getUniqueId().toString());
                preparedStatementInsert.setInt(2, 0); // 默认点数设置为0
                preparedStatementInsert.executeUpdate();
                plugin.getLogger().info("Created new record for player " + player.getName() + ".");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error checking or creating record for player " + player.getName() + ": " + e.getMessage());
        }
    }

    // 添加点数
    public void addPoints(Player player, int points) {
        // 确保玩家记录存在
        ensurePlayerRecordExists(player);
        String query = "UPDATE " + table + " SET points = points + ? WHERE player_uuid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, points);
            preparedStatement.setString(2, player.getUniqueId().toString());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                plugin.getLogger().info("Added " + points + " points to player " + player.getName() + ".");
            } else {
                plugin.getLogger().info("No changes made for player " + player.getName() + ".");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error adding points to player " + player.getName() + ": " + e.getMessage());
        }
    }

    // 移除点数
    public void removePoints(Player player, int points) {
        // 确保玩家记录存在
        ensurePlayerRecordExists(player);
        // 这里可以使用addPoints方法，只需传入负数即可
        addPoints(player, -points);
    }

    // 玩家获取点数
    public int getPlayerPoints(Player player) {
        int points = 0;
        String query = "SELECT points FROM " + table + " WHERE player_uuid = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    points = resultSet.getInt("points");
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error retrieving points for player " + player.getName() + ": " + e.getMessage());
        }
        return points;
    }

    // 关闭数据库连接
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database connection closed.");
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error closing database connection: " + e.getMessage());
        }
    }


    public void addPointsAsync(Player player, int points) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {
            addPoints(player, points);
        });
    }

    public void removePointsAsync(Player player, int points) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.runTaskAsynchronously(plugin, () -> {
            removePoints(player, points);
        });
    }
}
