package com.gsb.service;

import com.gsb.pojo.DemoData;
import com.gsb.mapper.DemoDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class service   {
    @Autowired
    private DemoDataMapper demoDataMapper;


    public int inster(DemoData demoData){
       return demoDataMapper.insertDemo(demoData);
    }
    public List<DemoData> qurey(){
      return  demoDataMapper.qurey();
    }

}
