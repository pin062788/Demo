package com.ocellus.platform.utils;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 */
public class ExcelUtils {
    private ExcelUtils() {
    }

    private static Logger logger = Logger.getLogger(ExcelUtils.class);

    /**
     * 标准内容样式
     *
     * @param book
     * @return
     */
    public static CellStyle createBaseStyle(Workbook book) {
        if (book == null) {
            book = new HSSFWorkbook();
        }
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();
        font.setFontName("宋体");
        style.setFont(font);
        style.setAlignment(HSSFCellStyle.ALIGN_LEFT); //左右居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        style.setWrapText(true); // 单元格自动换行
        style.setFont(font);
        return style;
    }

    /**
     * 标题样式
     *
     * @param book
     * @return
     */
    public static CellStyle createTitleStyle(Workbook book) {
        if (book == null) {
            book = new HSSFWorkbook();
        }
        CellStyle titleStyle = book.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //左右居中
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        Font titlefont = book.createFont();
        titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加重
        titlefont.setFontName("宋体");
        titlefont.setFontHeightInPoints((short) 16);
        titleStyle.setFont(titlefont);
        return titleStyle;
    }

    /**
     * 标题样式
     *
     * @param book
     * @return
     */
    public static CellStyle createTableTitleStyle(Workbook book) {
        if (book == null) {
            book = new HSSFWorkbook();
        }
        CellStyle tableTitleStyle = book.createCellStyle();
        tableTitleStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        tableTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        tableTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        tableTitleStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        tableTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //左右居中
        tableTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); //垂直居中
        Font tableTitlefont = book.createFont();
        tableTitlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //字体加重
        tableTitlefont.setFontName("宋体");
        tableTitlefont.setFontHeightInPoints((short) 10);
        tableTitleStyle.setFont(tableTitlefont);
        return tableTitleStyle;
    }
}
