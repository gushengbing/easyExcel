package com.gsb;
//我改了文件
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.gsb.Listener.DemoDataListener;
import com.gsb.pojo.DemoData;
import com.gsb.util.TestFileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = EasyExcelApplication.class)
public class TestDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDemo.class);
    /**
     * EasyExcel读操作
     */
    @Test
    public  void tesrRead(){
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = TestFileUtil.getPath()+"easyExcel071616376158638.xlsx";
        System.out.println(fileName);
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    /**
     * EasyExcel写操作
     */
    @Test
    public  void testWrite() {

        String fileName = TestFileUtil.getPath() + "easyExcel07"+System.currentTimeMillis()+".xlsx";
        /*// 写法1
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());*/

        // 写法2
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(data(), writeSheet);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private List<DemoData> data() {
        List<DemoData> list = new ArrayList<DemoData>();
       /* for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }*/
        return list;
    }

    @Test
    public  void test1() throws UnsupportedEncodingException {
       String name= URLEncoder.encode("缺陷管理_", "UTF-8").replaceAll("\\+", "%20");
//        String name="缺陷管理_";
//        name=new String(name.getBytes("GBK"),"iso-8859-1");
        System.out.println(name);//%E7%BC%BA%E9%99%B7%E7%AE%A1%E7%90%86_

    }
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    @Test
    public void  aVoid(){
//        getname("测试_复制(10)");
//        List<DemoData> list= data();
//        System.out.println(list.isEmpty());
        List<String> list=new ArrayList<String>();
        for (int i=0;i<20;i++) {
            list.add(""+i);
        }
        int pageSize=10;
        int pageNum=0;
        List<String> collect = list.stream().skip(pageSize * (pageNum - 1)).limit(pageSize).collect(Collectors.toList());


    }

    private    String getname(String max){
        String result ="";
        String[] name =max.split("_");
        for (int i=0;i<name.length;i++){
            if (i<name.length-1){
                result =result+name[i]+"_";
            }else{
                String tool=name[i];
                String num=tool.split("\\(")[1].split("\\)")[0];
                result=result+tool.split("\\(")[0]+"("+(Integer.parseInt(num)+1)+")";
            }
        }
        System.out.println(result);
        return result;
    }
}
