package com.lms.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangKeJie on 2015/8/17.
 */
public class OracleDataBaseMeta {
    public List<Map<String,String>> getDataMeta(Connection conn,String tableName){
        List<Map<String,String>> lists = new ArrayList<Map<String, String>>();
        try {
            Statement stat =conn.createStatement();
            String sql = "select a.constraint_name,b.table_name,b.column_name from user_constraints a left join user_cons_columns b on a.r_constraint_name=b.constraint_name where a.constraint_type=\'R\' and a.table_name=\'" + tableName + "\'";
            ResultSet rs = stat.executeQuery(sql);

            while (rs.next()){
                Map<String,String> map = new HashMap<String, String>();
                String pk_tableName="";
                String pk_columnName="";
                String pk = rs.getString("constraint_name");
                String fk_tableName = rs.getString("table_name");
                String fk_columnName = rs.getString("column_name");
                String sql_1 = "select table_name,column_name from user_cons_columns where constraint_name=" + "\'" + pk + "\'";
                Statement stat1 = conn.createStatement();
                //System.out.println("pk" + pk + "||" + "fk_tableName:" + fk_tableName + "||" + "fk_columnName:" + fk_columnName);
                ResultSet rs1 = stat1.executeQuery(sql_1);
                if(rs1.next()) {
                    pk_tableName = rs1.getString("table_name");
                    pk_columnName = rs1.getString("column_name");
                }
                map.put("PKCOLUMN_NAME",pk_columnName);
                map.put("FKTABLE_NAME",fk_tableName);
                map.put("FKCOLUMN_NAME",fk_columnName);
                System.out.println("pk_tableName:" + pk_tableName + "||" + "pk_columnName:" + pk_columnName + "||" + "fk_tableName:" + fk_tableName + "||" + "fk_columnName:" + fk_columnName);
                lists.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return  null;
        }
        return lists;
    }
}
