package com.ocellus.platform.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportExcelFile {
	
	//判断excel版本
	public static Workbook openWorkbook(String fileName)throws IOException{
		InputStream in = new FileInputStream(fileName);
		Workbook wb = null;
		if(fileName.endsWith(".xlsx")){
			wb = new XSSFWorkbook(in);//Excel 2007
		} else {
			wb = new HSSFWorkbook(in);//Excel 2003
		}
		return wb;
	}
	
	/***
	 * 得到文件各列标题集合
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getTitle(Workbook wb){
		Map titleMap = new HashMap();
		Sheet sheet = (Sheet)wb.getSheetAt(0);
		Iterator rows = sheet.rowIterator();
		Row row = null;
		if(rows.hasNext()){
			row = sheet.getRow(0);
		}
		Iterator cells1 = row.cellIterator();
		while(cells1.hasNext()){
			Cell cellContent = (Cell) cells1.next();
			if (cellContent != null && !cellContent.equals(null)) {
				Integer num = cellContent.getColumnIndex();
				titleMap.put(num, cellContent);
			}
		}
		return titleMap;
	}
	
	/**
	 * 得到Excel数据集合
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getExcelList(String fileName) throws Exception{
		List list = new ArrayList();
		Workbook wb = openWorkbook(fileName);
		Map title = getTitle(wb);
		Sheet sheet = (Sheet)wb.getSheetAt(0);
		Row row = null;
		Cell cell = null;
		int totalRows = sheet.getPhysicalNumberOfRows();
		int totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		boolean isAllNull = true;//针对都为空值的行数据做判断
		HashMap titleMap;
		for(int r=1; r<totalRows; r++){ //行的控制
			row = sheet.getRow(r);
			if(row != null){
				isAllNull = true;
				titleMap = new HashMap();
				for(int c = 0; c < totalCells; c++){//列的控制
					String cellName = title.get(c).toString();
					cell = row.getCell(c);
					String cellValue = getExcelData(cell);//列数据
					if(isAllNull && !StringUtil.isEmpty(cellValue)){
						isAllNull = false;
					}
					titleMap.put(cellName, cellValue);
				}
				if(!isAllNull){
					list.add(titleMap);
				}
			}
		}
		return list;
	}
	
	/**
	 * 返回excel表内容类型
	 * @param cell
	 * @throws Exception
	 */
	public static String getExcelData(Cell cell){
		String cellValue = "";
		if(null != cell){
			// 以下是判断数据的类型
			switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC: // 数字
			cellValue = cell.getNumericCellValue() + "";
			cellValue = new BigDecimal(cellValue).toString();

			//时间格式
			if(HSSFDateUtil.isCellDateFormatted(cell)){
				Date dd = cell.getDateCellValue();
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cellValue = df.format(dd);
			}
			break;
			case HSSFCell.CELL_TYPE_STRING: // 字符串
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				cellValue = cell.getBooleanCellValue() + "";
				break;
			case HSSFCell.CELL_TYPE_FORMULA: // 公式
				cellValue = cell.getCellFormula() + "";
				break;
			case HSSFCell.CELL_TYPE_BLANK: // 空值
				cellValue = "";
				break;
			case HSSFCell.CELL_TYPE_ERROR: // 故障
				cellValue = "非法字符";
				break;
			default:
				cellValue = "未知类型";
				break;
			}
		}
		return cellValue.trim();
	}
	
	/**
	 * 在导入的数据中会把数字转下double.这时就需要以下两个方法对其复原
	 * @param cellValue
	 * @param point
	 * @return
	 */
	private String doubleToString(String cellValue, int point) {
		double dbl = 0.0D;
		String format = new String();
		for (int i = 0; i < point; i++)
			format = format + "0";
		if (format.length() > 0)
			format = "0." + format;
		else
			format = "0";
		try {
			dbl = Double.parseDouble(cellValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return double2String(dbl, format);
	}

	private String double2String(double dnum, String format) {
		DecimalFormat nf = new DecimalFormat(format);
		return nf.format(dnum);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws Exception{
		String fileName = "D:/Book1.xlsx";
		ImportExcelFile importExcelFile = new ImportExcelFile();
		List<Map> userList = importExcelFile.getExcelList(fileName);
		//User user = new User();
		for (int i = 0; i < userList.size(); i++) {
			Map hashMap = userList.get(i);
			/*user.setName(String.valueOf(hashMap.get("姓名")));
			user.setSex(String.valueOf(hashMap.get("性别")));*/
			System.out.println(String.valueOf(hashMap.get("密码")));
		}
	}


}
