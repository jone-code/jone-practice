package com.thunisoft.zipper.util;

import com.thunisoft.zipper.controller.AjInfoController;
import com.thunisoft.zipper.pojo.TSjy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtil {

    /**
     * GP_DRIVER
     */
    private static final String GPDB_DRIVER = "com.pivotal.jdbc.GreenplumDriver";
    /**
     * 日志对象
     */
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUtil.class);

    /**
     * 查询sql
     * @param sjy
     * @param sql
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Map<String, String>> query(TSjy sjy, String sql) throws SQLException, ClassNotFoundException {
        List<Map<String, String>> list = new ArrayList<>();
        Connection con = getConnection(sjy);
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int fieldCount = rsmd.getColumnCount();
        while (rs.next()) {
            Map<String, String> valueMap = new HashMap<String, String>();
            for (int i = 0; i < fieldCount; i++) {
                String columnName = rsmd.getColumnName(i+1);
                String value = rs.getString(i+1);
                valueMap.put(columnName, value);
            }
            list.add(valueMap);
        }
        close(con, ps, rs);
        return list;
    }

    /**
     * 获取连接
     * @param sjy
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private static Connection getConnection(TSjy sjy) throws ClassNotFoundException, SQLException {
        Class.forName(GPDB_DRIVER);
        Connection conn = DriverManager.getConnection(sjy.getcUrl(), sjy.getcUserName(), sjy.getcPassword());
        return conn;
    }

    /**
     * 关闭连接
     * @param conn
     * @param statement
     * @param rs
     */
    private static void close(Connection conn, Statement statement, ResultSet rs) {
        JdbcUtils.closeConnection(conn);
        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeResultSet(rs);
    }

    /**
     * 执行一个sql
     * @param sjy
     * @param sql
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void execute(TSjy sjy, String sql) throws SQLException, ClassNotFoundException {
        Connection con = getConnection(sjy);
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        close(con, ps, null);
    }

    /**
     * 批量去执行
     * @param sjy
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static void executeBatch(TSjy sjy, List<String> sqls) throws SQLException, ClassNotFoundException {
        Connection con = getConnection(sjy);
        Statement statement = con.createStatement();
        for (int i = 0; i < sqls.size(); i++) {
            LOG.info("准备执行--->"+sqls.get(i));
            statement.addBatch(sqls.get(i));
        }
//        statement.executeBatch();
        close(con, statement, null);
    }
}
