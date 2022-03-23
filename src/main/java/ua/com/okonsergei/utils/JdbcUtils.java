package ua.com.okonsergei.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {

    private static final String url;
    private static final String username;
    private static final String password;

    static {
        Properties props = new Properties();
        try (InputStream in = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("database.properties")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        url = props.getProperty("url");
        username = props.getProperty("username");
        password = props.getProperty("password");
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public PreparedStatement getPreparedStatementReturnId(String sql) throws SQLException {
        getConnection();
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        getConnection();
        return getConnection().prepareStatement(sql);
    }
}
