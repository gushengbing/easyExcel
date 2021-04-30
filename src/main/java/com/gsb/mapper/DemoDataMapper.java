package com.gsb.mapper;

import com.gsb.pojo.DemoData;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface DemoDataMapper {//extends Mapper<DemoData>

    //@Insert("INSERT INTO excel (string,date,doubleData) VALUES(#{str},#{da},#{db})")
    int insertDemo(DemoData demoData);

    List<DemoData> qurey();
}
