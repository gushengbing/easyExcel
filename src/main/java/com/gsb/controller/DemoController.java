package com.gsb.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.gsb.Listener.DemoDataListener;
import com.gsb.dao.DemoDAO;
import com.gsb.pojo.DemoData;
import com.gsb.service.service;
import com.gsb.util.TestFileUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
public class DemoController {
    private final static Logger logger = LoggerFactory.getLogger(DemoController.class);
    @Autowired
    private DemoDAO demoDAO;
    @Autowired
    private  service ser;
    @PostMapping("upload")
    public String upload( @RequestParam(value = "file")MultipartFile file) throws IOException {
       // EasyExcel.read(file.getInputStream(), DemoData.class, new DemoDataListener(demoDAO)).sheet().doRead();
        ExcelReader excelReader = null;
        InputStream in = null;
        try {
            in = file.getInputStream();
            excelReader = EasyExcel.read(in, DemoData.class,
                    new DemoDataListener(demoDAO)).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } catch (IOException ex) {
            logger.error("import excel to db fail", ex);
        } finally {
            // 这里一定别忘记关闭，读的时候会创建临时文件，到时磁盘会崩
            if (excelReader != null) {
                excelReader.finish();
            }
            in.close();
        }

        return "success";

    }


    @PostMapping("download")
    public  String download(HttpServletResponse response) throws IOException {
        String fileName = TestFileUtil.getPath()+System.currentTimeMillis()+".xlsx";
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");//导出.xlsx
        response.setHeader("Content-disposition", "attachment;filename*=utf-8" + fileName + ".xlsx");
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//        WriteFont headWriteFont = new WriteFont();//字体大小
//        headWriteFont.setFontHeightInPoints((short)20);
//        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
       // contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
       // contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());  // 背景白色

        WriteFont contentWriteFont = new WriteFont(); // 字体大小
        contentWriteFont.setFontHeightInPoints((short)12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//内容水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

       // ExcelWriter excelWriter = null;
//        ServletOutputStream out = response.getOutputStream();
//        out.flush();
        try {
            EasyExcel.write(response.getOutputStream(), DemoData.class).registerWriteHandler(horizontalCellStyleStrategy).sheet("模板")
                    .doWrite(ser.qurey());
            /*excelWriter = EasyExcel.write(response.getOutputStream(), DemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            excelWriter.write(ser.qurey(), writeSheet);*/
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        } /*finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }*/
        return  "success";
    }

    @GetMapping("qurey")
    public  String query(){
        List <DemoData> list=ser.qurey();
        int i=0;
        for (DemoData demoData:list) {
            i++;
            System.out.println("第"+i+"个对象"+demoData);
        }
        return "success";
    }
}
