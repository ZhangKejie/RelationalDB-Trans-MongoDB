package com.lms.controller;

import com.lms.service.TransformService;
import com.lms.util.JsonUtil;
import com.sun.deploy.net.HttpResponse;
import net.sf.json.JSONObject;
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
    public void trans(@RequestBody String json,HttpServletResponse response){
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
            response.getWriter().write(jsonObject);
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
