package com.lms.dao;

import java.util.Map;
import java.util.Objects;

/**
 * transform数据访问层的接口。
 * Created by Asus55 on 2015/8/12.
 */
public interface TransformDAO {
    public String trans(Map<String,Object> map);
}
