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
            // ????????????????????????????????????????????????????????????????????????????????????
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
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");//??????.xlsx
        response.setHeader("Content-disposition", "attachment;filename*=utf-8" + fileName + ".xlsx");
        // ????????????
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // ?????????????????????
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//        WriteFont headWriteFont = new WriteFont();//????????????
//        headWriteFont.setFontHeightInPoints((short)20);
//        headWriteCellStyle.setWriteFont(headWriteFont);
        // ???????????????
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // ?????????????????? FillPatternType ???FillPatternType.SOLID_FOREGROUND ??????????????????????????????.???????????? FillPatternType?????????????????????
       // contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
       // contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());  // ????????????

        WriteFont contentWriteFont = new WriteFont(); // ????????????
        contentWriteFont.setFontHeightInPoints((short)12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//??????????????????
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//????????????
        // ??????????????? ?????????????????? ???????????????????????? ?????????????????????????????????
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

       // ExcelWriter excelWriter = null;
//        ServletOutputStream out = response.getOutputStream();
//        out.flush();
        try {
            EasyExcel.write(response.getOutputStream(), DemoData.class).registerWriteHandler(horizontalCellStyleStrategy).sheet("??????")
                    .doWrite(ser.qurey());
            /*excelWriter = EasyExcel.write(response.getOutputStream(), DemoData.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("??????").build();
            excelWriter.write(ser.qurey(), writeSheet);*/
        } catch (Exception e) {
            // ??????response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<String, String>();
            map.put("status", "failure");
            map.put("message", "??????????????????" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        } /*finally {
            // ???????????????finish ??????????????????
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
            System.out.println("???"+i+"?????????"+demoData);
        }
        return "success";
    }
}
