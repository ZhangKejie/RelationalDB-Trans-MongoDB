package com.lms.dao.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lms.dao.TransformDAO;
import com.lms.util.DbUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;
import sun.nio.cs.CharsetMapping;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * transform数据访问类，通过此类对数据库进行操作。
 * Created by ZhangKejie on 2015/8/12.
 */

@Repository("transformDAO")
public class TransformDAOImpl implements TransformDAO{

    private DbUtil dbUtil;

    /**
     *transform服务通过此方法对数据库进行操作访问，并将结果进行返回。
     * @param map json类型的数据解析成的map对象
     * @return String 返回String类型的jsonArray数据
     */
    @Override
    public String trans(Map<String, Object> map){
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Map<String,Object> cmap = null;
        JSONArray jsonArray = new JSONArray();
        int i;
        String str = "";

        //得到json数据中传递过来的参数
        String dbtype = (String) map.get("dbtype");
        String url = (String)map.get("url");
        String port = (String)map.get("port");
        String dbname = (String)map.get("dbname");
        String username = (String)map.get("username");
        String password = (String)map.get("password");
        String columnName = "";
        List<Object> column = (List<Object>) map.get("column");//获取columnName
        for(i = 0;i<(column.size()-1);i++){    //循环取出columnName
            cmap = (Map<String, Object>)column.get(i);
            columnName = columnName + cmap.get("name") + ",";
        }
        cmap = (Map<String, Object>)column.get(i);
        columnName = columnName + cmap.get("name"); //最后一个字段名后不加逗号
       // System.out.println(columnName);
        String tableName = (String)map.get("tableName");
        String sql = "select" + " " + columnName + " from " + tableName;
        //System.out.println(sql);
        dbUtil.SQLConnect(dbtype,url,port,dbname,username,password); //调用数据库连接接口对数据库进行连接
        try {
            dbUtil.rs = dbUtil.stat.executeQuery(sql);//数据库操作后得到的结果
            ResultSetMetaData rsmd = dbUtil.rs.getMetaData();//通过ResultSet得到ResultSetMetaData，通过ResultSetMetaData可以得到数据column的属性
            while (dbUtil.rs.next()){
                Map<String,Object> jsonObject = new HashMap<String, Object>();
                for (int j=1;j<=rsmd.getColumnCount();j++){  //通过ResultSetMetaData得到rs中的数据column的个数
                    String key = rsmd.getColumnName(j);//得到ResultSet中每个column的Name
                    String value = dbUtil.rs.getString(j);//得到每个字段的value
                    //System.out.println(key + ":" + value);
                    jsonObject.put(key,value);//将得到的key和value注入到map中
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
        return str;
    }

    public DbUtil getDbUtil() {
        return dbUtil;
    }

    @Resource
    public void setDbUtil(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }
}
