package org.example.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.config.ConfigLoader;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final HikariConfig hikariConfig = new HikariConfig();
    private static final HikariDataSource datasource;

    static {
        hikariConfig.setDriverClassName(ConfigLoader.get("db.driver-class-name"));
        hikariConfig.setJdbcUrl(ConfigLoader.get("db.url"));
        hikariConfig.setUsername(ConfigLoader.get("db.username"));
        hikariConfig.setPassword(ConfigLoader.get("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(ConfigLoader.get("db.maximumPoolSize")));
        hikariConfig.setMinimumIdle(Integer.parseInt(ConfigLoader.get("db.minimumIdle")));
        hikariConfig.setIdleTimeout(Integer.parseInt(ConfigLoader.get("db.idleTimeout")));
        hikariConfig.setMaxLifetime(Integer.parseInt(ConfigLoader.get("db.maxLifetime")));

        datasource = new HikariDataSource(hikariConfig);
    }

    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }
}
