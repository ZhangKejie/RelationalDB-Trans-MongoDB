package com.lms.dao.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lms.dao.TransformDAO;
import com.lms.util.DbUtil;
import com.lms.util.OracleDataBaseMeta;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import sun.nio.cs.CharsetMapping;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;

/**
 * transform数据访问类，通过此类对数据库进行操作。
 * Created by ZhangKejie on 2015/8/12.
 */

@Repository("transformDAO")
public class TransformDAOImpl implements TransformDAO {

    private DbUtil dbUtil;

    /**
     * transform服务通过此方法对数据库进行操作访问，并将结果进行返回。
     *
     * @param map json类型的数据解析成的map对象
     * @return String 返回String类型的jsonArray数据
     */
    @Override
    public String trans(Map<String, Object> map) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String,String>> fks = new ArrayList<Map<String, String>>(); //储存此表所有的外键信息
        // Map<String,Object> cmap = null;
        JSONArray jsonArray = new JSONArray();
        int i;
        String str = "";

        //得到json数据中传递过来的参数
        String dbtype = (String) map.get("dbtype");
        String url = (String) map.get("url");
        String port = (String) map.get("port");
        String dbname = (String) map.get("dbname");
        String username = (String) map.get("username");
        String password = (String) map.get("password");
        //List<Object> tables = (List<Object>) map.get("tables");
        Map<String, Object> tables = (Map<String, Object>) map.get("tables");
        String fk = (String) tables.get("fk");
        //for(int k = 0;k<2;k++) {
        //Map<String,Object> tmap = (Map<String, Object>) tables.get(k);
        //System.out.println(tmap.size());
        String columnName = "";
        List<String> columns = (List<String>) tables.get("columns");//获取columnName
        for (i = 0; i < (columns.size() - 1); i++) {    //循环取出columnName
            columnName = columnName + columns.get(i) + ",";
        }
        columnName = columnName + columns.get(i); //最后一个字段名后不加逗号
        // System.out.println(columnName);
        String tableName = (String) tables.get("name");
        String sql = "select" + " " + columnName + " from " + tableName;
        //System.out.println(sql);
        boolean flag = dbUtil.SQLConnect(dbtype, url, port, dbname, username, password); //调用数据库连接接口对数据库进行连接
        if(flag==false){
            return "{\"Meta\":{\"Code\":203,\"Message\":\"数据库连接失败，请核对数据库地址及账号密码！\"},\"Data\":" + "[]" + "}";
        }
        //判断是否需要取出外键数据。
        if(fk.equals("true")) {
            try {
                if(dbtype.equals("oracle")){
                    OracleDataBaseMeta odbm = new OracleDataBaseMeta();
                    fks = odbm.getDataMeta(dbUtil.conn,tableName.toUpperCase());
                }else {
                    /**
                     * 读取外键数据。
                     */
                    ResultSet rs1 = dbUtil.conn.getMetaData().getExportedKeys(null, null, tableName);
                    //if(rs1.next()==false){System.out.println("NULLNULLNULL!!!");}
                    ResultSetMetaData rsmd1 = rs1.getMetaData();
                    //System.out.println(tableName);
                    while (rs1.next()) {
                        Map<String, String> fkMes = new HashMap<String, String>();
                   /*for(int m = 1;m<=rsmd1.getColumnCount();m++){
                        System.out.println(rsmd1.getColumnName(m) + ":" + rs1.getString(rsmd1.getColumnName(m)));
                    }*/
                        System.out.println("PKTABLE_NAME:" + rs1.getString("PKTABLE_NAME") + "   " + "PKCOLUMN_NAME:" + rs1.getString("PKCOLUMN_NAME") + "||" + "FKTABLE_NAME:" + rs1.getString("FKTABLE_NAME") + "   " + "FKCOLUMN_NAME:" + rs1.getString("FKCOLUMN_NAME"));
                        // System.out.println("FKTABLE_NAME:" + rs1.getString("FKTABLE_NAME") + "||" + "FKCOLUMN_NAME:" + rs1.getString("FKCOLUMN_NAME"));
                        //fkMes.put("PKTABLE_NAME",rs1.getString("PKTABLE_NAME"));
                        fkMes.put("PKCOLUMN_NAME", rs1.getString("PKCOLUMN_NAME"));
                        fkMes.put("FKTABLE_NAME", rs1.getString("FKTABLE_NAME"));
                        fkMes.put("FKCOLUMN_NAME", rs1.getString("FKCOLUMN_NAME"));
                        fks.add(fkMes);
                    }
                }
                /**
                 * 判断需要查询的字段有没有被外键引用。
                 */
                //List<Map<String,List<String>>> fkNum = new ArrayList<Map<String, List<String>>>();
                Map<String, List<String>> colMap = new HashMap<String, List<String>>();
                if(columnName.equals("*")){
                    for (int n = 0; n < columns.size(); n++) {
                        String col = columns.get(n);
                        for (int a = 0; a < fks.size(); a++) {
                           // if (col.equals(fks.get(a).get("PKCOLUMN_NAME"))) {
                                //System.out.println(col);
                                if (colMap.get(col) == null) {
                                    List<String> fkTable1 = new ArrayList<String>();
                                    fkTable1.add(fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    //fkTable1.add(fks.get(a).get("FKCOLUMN_NAME"));
                                    System.out.println(fks.get(a).get("PKCOLUMN_NAME") + ":" + fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    colMap.put(fks.get(a).get("PKCOLUMN_NAME"), fkTable1);
                                } else {
                                    List<String> fkTable1 = colMap.get(col);
                                    System.out.println(fks.get(a).get("PKCOLUMN_NAME") + ":" + fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    fkTable1.add(fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    //fkTable1.add(fks.get(a).get("FKCOLUMN_NAME"));
                                    colMap.put(fks.get(a).get("PKCOLUMN_NAME"), fkTable1);
                                }
                           // }
                        }
                    }
                }else {
                    for (int n = 0; n < columns.size(); n++) {
                        String col = columns.get(n);
                        for (int a = 0; a < fks.size(); a++) {
                            if (col.equals(fks.get(a).get("PKCOLUMN_NAME"))) {
                                //System.out.println(col);
                                if (colMap.get(col) == null) {
                                    List<String> fkTable1 = new ArrayList<String>();
                                    fkTable1.add(fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    //fkTable1.add(fks.get(a).get("FKCOLUMN_NAME"));
                                    System.out.println(col + ":" + fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    colMap.put(col, fkTable1);
                                } else {
                                    List<String> fkTable1 = colMap.get(col);
                                    System.out.println(col + ":" + fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    fkTable1.add(fks.get(a).get("FKTABLE_NAME") + "," + fks.get(a).get("FKCOLUMN_NAME"));
                                    //fkTable1.add(fks.get(a).get("FKCOLUMN_NAME"));
                                    colMap.put(col, fkTable1);
                                }
                            }
                        }
                    }
                }
                dbUtil.rs = dbUtil.stat.executeQuery(sql);//数据库操作后得到的结果
                ResultSetMetaData rsmd = dbUtil.rs.getMetaData();//通过ResultSet得到ResultSetMetaData，通过ResultSetMetaData可以得到数据column的属性


                while (dbUtil.rs.next()) {
                    Map<String, Object> jsonObject = new HashMap<String, Object>();
                    for (int j = 1; j <= rsmd.getColumnCount(); j++) {  //通过ResultSetMetaData得到rs中的数据column的个数
                        String key = tableName + "_" + rsmd.getColumnName(j);//得到ResultSet中每个column的Name
                        String value = dbUtil.rs.getString(j);//得到每个字段的value
                        //System.out.println(key + ":" + value);
                        jsonObject.put(key.toLowerCase(), value);//将得到的key和value注入到map中
                        Set keySet = colMap.keySet();
                        Iterator<String> it = keySet.iterator();
                        while (it.hasNext()) {
                            String tcol = it.next();
                            //System.out.println(tcol);
                            if (rsmd.getColumnName(j).equals(tcol)) {
                                List<String> fTables = colMap.get(tcol);
                                for (int n = 0; n < fTables.size(); n++) {
                                    String[] Meg = fTables.get(n).split(",");
                                    String sql1 = "select * from " + Meg[0] + " where " + Meg[1] + "=" + "\'" + value + "\'";
                                    System.out.println(sql1);
                                    Statement stat2 = dbUtil.conn.createStatement();
                                    ResultSet rs2 = stat2.executeQuery(sql1);
                                    ResultSetMetaData rsdm1 = rs2.getMetaData();
                                    List<Map<String, String>> fkList = new ArrayList<Map<String, String>>();
                                    while (rs2.next()) {
                                        Map<String, String> fkMap = new HashMap<String, String>();
                                        for (int k = 1; k <= rsdm1.getColumnCount(); k++) {
                                            String fkey = rsdm1.getColumnName(k);
                                            String fValue = rs2.getString(k);
                                            fkMap.put(fkey.toLowerCase(), fValue);
                                        }
                                        fkList.add(fkMap);
                                    }
                                    jsonObject.put(Meg[0].toLowerCase(), fkList);
                                }
                            }
                        }

                    }
                    list.add(jsonObject);//将每个map注入list中
                    //JSONObject json = JSONObject.fromObject(jsonObject);
                    //jsonArray.add(json);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // }
            jsonArray = JSONArray.fromObject(list);//将list对象转化为jsonArray的类型
            str = jsonArray.toString();
            // System.out.println(str);
            return "{\"Meta\":{\"Code\":200,\"Message\":\"转化成功！\"},\"Data\":" + str + "}";
        }else{//不需要取出外键数据
            try {
                dbUtil.rs = dbUtil.stat.executeQuery(sql);//数据库操作后得到的结果
                ResultSetMetaData rsmd = dbUtil.rs.getMetaData();//通过ResultSet得到ResultSetMetaData，通过ResultSetMetaData可以得到数据column的属性
                while (dbUtil.rs.next()){
                    Map<String,Object> jsonObject = new HashMap<String, Object>();
                    for (int j=1;j<=rsmd.getColumnCount();j++){  //通过ResultSetMetaData得到rs中的数据column的个数
                        String key = tableName + "_" + rsmd.getColumnName(j);//得到ResultSet中每个column的Name
                        String value = dbUtil.rs.getString(j);//得到每个字段的value
                        //System.out.println(key + ":" + value);
                        jsonObject.put(key.toLowerCase(),value);//将得到的key和value注入到map中
                    }
                    list.add(jsonObject);//将每个map注入list中
                    //JSONObject json = JSONObject.fromObject(jsonObject);
                    //jsonArray.add(json);
                }
                jsonArray = JSONArray.fromObject(list);//将list对象转化为jsonArray的类型
                str = jsonArray.toString();
                // System.out.println(str);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return "{\"Meta\":{\"Code\":200,\"Message\":\"转化成功！\"},\"Data\":" + str + "}";
        }
    }

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    @Resource
    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }
}
