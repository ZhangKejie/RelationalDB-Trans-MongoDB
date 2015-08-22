package com.lms.controller;

import com.lms.service.TransformService;
import com.lms.util.HttpRequestUtils;
import com.lms.util.JsonUtil;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TransForm服务的Controller类。
 * Created by ZhangKejie on 2015/8/12.
 */
@Controller
public class TransformController {


    private TransformService transformService;
    private JsonUtil jsonUtil;

    /**
     *读取关系型数据库内容并将其转化为json对象。
     * @param json
     * @return json
     */
    //@ResponseBody
    @RequestMapping(value="/lms/trans",method= RequestMethod.POST)
    public String trans(@RequestBody String json,HttpServletResponse response){
        //System.out.println(json);

         //转化json对象，除去其中的空格换行等字符，为jsonUtil中的jsonParse做准备。
        JSONObject json1 = JSONObject.fromObject(json);
        String json2 = json1.toString();
        System.out.println(json2);
        //调用jsoUtil一次性解析所有的json数据
        Map<String,Object> map = (Map<String, Object>) jsonUtil.jsonParse(json2);
        String jsonObject = transformService.trans(map);
        System.out.println(jsonObject);
        try {
            if(response!=null) {
                response.getWriter().write(jsonObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     *将关系型数据库的数据插入MongoDB中。
     * @param json
     * @param response
     */
    @RequestMapping(value = "/lms/insert",method = RequestMethod.POST)
    public void insert(@RequestBody String json,HttpServletResponse response) {
        // System.out.println(json);
        JSONObject jsonObject = HttpRequestUtils.httpPost("http://218.108.45.7:6002/sqlTransform", JSONObject.fromObject(json), false);
        String res = jsonObject==null?"":jsonObject.toString();
        try {
            response.getWriter().write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取之前已经转化过的数据库连接信息。
     * @param json
     * @param response
     */
    @RequestMapping(value = "/lms/update",method = RequestMethod.POST)
    public void update(@RequestBody String json,HttpServletResponse response){
        String result = "";
        JSONObject jsonObject = HttpRequestUtils.httpPost("http://218.108.45.7:6002/getTransformInfo", JSONObject.fromObject(json), false);
        String res = jsonObject==null?"":jsonObject.toString();
        Map<String,Object> map = (Map<String, Object>) JsonUtil.jsonParse(res);
        Map<String,Object> meta = (Map<String, Object>) map.get("Meta");
        int code = Integer.parseInt((String) meta.get("Code"));
        if(code==200) {
            Map<String,Object> data = (Map<String, Object>) map.get("Data");
            Map<String,Object> database = (Map<String, Object>) data.get("database");
            String sql_id = (String) data.get("sql_id");
            List<String> columns = (List<String>) data.get("columns");
            String operator_id = (String) data.get("operator_id");
            String company_code = (String) data.get("company_code");
            String fk = (String) data.get("get_foreign_key");
            String tableName = (String) data.get("table");
            String collection_name = (String) data.get("collection_name");

            Map<String,Object> trans = new HashMap<String, Object>();//发送给trans的data
            trans.put("company_id",company_code);
            Map<String,Object> datatree = new HashMap<String, Object>();
            if(database.equals(new HashMap<String, Object>())!=true){
                datatree.put("dbtype",database.get("db_type"));
                datatree.put("dbname",database.get("name"));
                datatree.put("username",database.get("username"));
                datatree.put("password",database.get("password"));
                datatree.put("port",database.get("port"));
                datatree.put("host",database.get("host"));
            }else {
                datatree = null;
                try {
                    response.getWriter().write("{\"Meta:\"{\"Message\":\"数据库连接信息不存在！\",\"Code\":\"203\"}}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            trans.put("datatree",datatree);
            Map<String,Object> tables = new HashMap<String, Object>();
            tables.put("name",tableName);
            tables.put("fk",fk);
            List<String> newCol = new ArrayList<String>();
            System.out.println(columns.size());
            for (int i = 0;i<columns.size();i++){
                String column = columns.get(i);
                if (column.contains(tableName)){
                    column = column.replace(tableName+"_","");
                    newCol.add(column);
                }
            }
            tables.put("columns",newCol);
            trans.put("tables",tables);
            String jsonParam = JSONObject.fromObject(trans).toString();
            //JSONObject jsonObject1 = HttpRequestUtils.httpPost("http://127.0.0.1:6010/lms/trans", JSONObject.fromObject(jsonParam), false);
            String jsonObject1 = this.trans(jsonParam,null);
            //String res1 = jsonObject1==null?"":jsonObject1;
            Map<String,Object> transRes = (Map<String, Object>) JsonUtil.jsonParse(jsonObject1);
            Map<String,Object> transMeta = (Map<String, Object>) transRes.get("Meta");
            Map<String,Object> transData = (Map<String, Object>) transRes.get("Data");
            int transCode = Integer.parseInt((String)transMeta.get("Code"));
            Map<String,Object> insertMes = new HashMap<String, Object>();
            //System.out.println(jsonObject1);
           if(transCode==200&&transData.equals(new HashMap())!=true){
               //System.out.println(transData);
               List<Map<String,Object>> transCol = (List<Map<String, Object>>) transData.get("columns");
               insertMes.put("company_code",company_code);
               insertMes.put("database",database);
               insertMes.put("table",tableName);
               insertMes.put("sql_id",sql_id);
               insertMes.put("get_foreign_key",fk);
               insertMes.put("columns",transCol);
               insertMes.put("collection_name",collection_name);
               insertMes.put("operator_id",operator_id);
               JSONObject insertRes = HttpRequestUtils.httpPost("http://218.108.45.7:6002/sqlTransform", JSONObject.fromObject(insertMes), false);
               result = insertRes==null?"":insertRes.toString();
            }else{
                transData = null;
               try {
                   response.getWriter().write("{\"Meta:\"{\"Message\":\"获取数据字段信息为空，无内容可更新！\",\"Code\":\"203\"}}");
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
            //System.out.println(res1);
        }else{
            result = "{\"Meta:\"{\"Message\":\"获取数据库连接信息失败！\",\"Code\":\"203\"}}";
        }
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TransformService getTransformService() {
        return transformService;
    }

    @Resource
    public void setTransformService(TransformService transformService) {
        this.transformService = transformService;
    }


    public JsonUtil getJsonUtil() {
        return jsonUtil;
    }

    @Resource
    public void setJsonUtil(JsonUtil jsonUtil) {
        this.jsonUtil = jsonUtil;
    }
}
