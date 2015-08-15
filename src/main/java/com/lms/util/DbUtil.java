package com.lms.util;

import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * 数据库连接工具类。
 * Created by Zhangkejie on 2015/8/12.
 *
 */
@Component("dbUtil")
public class DbUtil {

    public Connection conn = null;
    public Statement stat = null;
    public ResultSet rs = null;
   // public DatabaseMetaData dbm = null;

    /**
     *根据数据库类型来判别属于哪种数据库，并进行连接。
     *
     * @param dbtype 数据库类型
     * @param url 数据库ip地址
     * @param port 数据库端口
     * @param dbname 数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public void SQLConnect(String dbtype,String url,String port,String dbname,String username,String password){
        if (dbtype.equals("mysql")){ //判断数据库类型
            this.MySQLConnect(url,port,dbname,username,password);
        }else if (dbtype.equals("oracle")) {
            this.OracleConnect(url,port,dbname,username,password);
        }else if(dbtype.equals("sqlserver")){
            this.SQLServerConnect(url,port,dbname,username,password);
        }
    }

    /**
     *MySQL数据库的连接方法。
     *
     * @param url 数据库ip地址
     * @param port 数据库端口
     * @param dbname 数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public void MySQLConnect(String url,String port,String dbname,String username,String password){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection
                        ("jdbc:mysql://" + url + ":" + port + "/" + dbname, username, password);
            System.out.println("Connected Mysql!");//测试语言判断数据库连接是否成功
            stat = conn.createStatement();
           // dbm = conn.getMetaData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     *oracle数据库的连接方法。
     *
     * @param url 数据库ip地址
     * @param port 数据库端口
     * @param dbname 数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public void OracleConnect(String url,String port,String dbname,String username,String password){
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String u = "jdbc:oracle:thin:@" + url + ":" + port + ":" + dbname;
            System.out.println(u);
            conn = DriverManager.getConnection(u,username,password);
            stat = conn.createStatement();
           // dbm = conn.getMetaData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
            System.out.println("Connected Oracle!");//测试语言判断数据库连接是否成功；
    }

    /**
     *SQLServer数据的连接方法。
     *
     * @param url 数据库ip地址
     * @param port 数据库端口
     * @param dbname 数据库名
     * @param username 数据库用户名
     * @param password 数据库密码
     */
    public void SQLServerConnect(String url,String port,String dbname,String username,String password){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection("jdbc:sqlserver://" + url + ":" + port + ";databaseName=" + dbname,username,password);
            stat = conn.createStatement();
            //dbm = conn.getMetaData();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("Connected SQLServer!");//测试语言判断数据库连接是否成功；
    }
}
