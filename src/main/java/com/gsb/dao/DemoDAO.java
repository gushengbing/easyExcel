package com.gsb.dao;

import com.alibaba.fastjson.JSON;
import com.gsb.pojo.DemoData;
import com.gsb.service.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 假设这个是你的DAO存储。当然还要这个类让spring管理，当然你不用需要存储，也不需要这个类。
 **/
@Repository
public class DemoDAO {
    private final static Logger logger = LoggerFactory.getLogger(DemoDAO.class);

    @Autowired
    private service se;

    /*@Autowired
    private DemoDataMapper demoDataMapper;*/

    public void save(List<DemoData> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入D
        for (DemoData data:list) {
            logger.info("对象：{}",JSON.toJSONString(data));
            se.inster(data);
//            demoDataMapper.insertDemo(data.getString(),data.getDate(),data.getDoubleData());
        }

    }
}