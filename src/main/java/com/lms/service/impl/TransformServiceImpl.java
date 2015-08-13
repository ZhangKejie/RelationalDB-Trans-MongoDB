package com.lms.service.impl;

import com.lms.dao.TransformDAO;
import com.lms.service.TransformService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * transform的业务逻辑层的实现类。
 * Created by Asus55 on 2015/8/12.
 */
@Service("transformService")
public class TransformServiceImpl implements TransformService{
    private TransformDAO transformDAO;

    /**
     *transform服务通过此方法对数据访问层进行调用，并将结果进行返回。
     * @param map json类型的数据解析成的map对象
     * @return String 返回由数据访问层返回的json数据
     */
    @Override
    public String trans(Map<String, Object> map) {
        return transformDAO.trans(map);
    }

    public TransformDAO getTransformDAO() {
        return transformDAO;
    }

    @Resource
    public void setTransformDAO(TransformDAO transformDAO) {
        this.transformDAO = transformDAO;
    }
}
