package com.ocellus.platform.utils;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bi.jialong on 2016/9/27 0027.
 */
public class CsvUtil {
    /**
     * 读取CSV文件
     */
    public static List<String[]> readeCsv(String csvFilePath){
        ArrayList<String[]> csvList = new ArrayList<String[]>(); //用来保存数据
        try {
            CsvReader reader = new CsvReader(csvFilePath,',',Charset.forName("GBK"));    //一般用这编码读就可以了
//          reader.readHeaders(); // 跳过表头   如果需要表头的话，不要写这句。
            while(reader.readRecord()){ //逐行读入除表头的数据
                csvList.add(reader.getValues());
            }
            reader.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return csvList;
    }

    /**
     * 写入CSV文件
     */
    public static void writeCsv(String csvFilePath,List<String[]> contents){
        try {
            CsvWriter wr =new CsvWriter(csvFilePath,',',Charset.forName("GBK"));
            for (String[] content:contents) {
                wr.writeRecord(content);
            }
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
