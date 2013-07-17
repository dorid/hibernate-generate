package com.generate.util;

import java.sql.*;

/**
 * JDBC辅助类
 */
public final class JdbcUtils {
    private static String url = "jdbc:mysql://localhost:3306/information_schema"; //连接数据库连接
    private static String use = Config.getInstance().get("user_name");
    private static String password = Config.getInstance().get("password");

    private JdbcUtils() {

    }

    /**
     * 使用静态模块来进行注册驱动 
     */
    static {
        // 1. 注册驱动  
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * 建立数据库连接
     *
     * @return 返回数据库连接
     * @throws java.sql.SQLException
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, use, password);
    }

    /**
     * 释放数据资源
     *
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null)
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

}  