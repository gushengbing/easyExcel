package com.gsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import tk.mybatis.spring.annotation.MapperScan;


//@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.gsb.mapper")
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class EasyExcelApplication  {
    public static void main(String[] args) {
        SpringApplication.run(EasyExcelApplication.class,args);
    }
}
