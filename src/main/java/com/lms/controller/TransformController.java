package com.lms.controller;

import com.lms.service.TransformService;
import com.lms.util.JsonUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
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
     */
    @RequestMapping(value="/lms/trans",method= RequestMethod.POST)
    public void trans(@RequestBody String json){
        System.out.println(json);
        Map<String,Object> map = (Map<String, Object>) jsonUtil.jsonParse(json);
        String jsonArray = transformService.trans(map);
        String trans = "{\"Meta\":{\"Code\":200,\"Message\":\"转化成功！\"},\"Data\":" + jsonArray + "}";
        System.out.println(trans);
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
