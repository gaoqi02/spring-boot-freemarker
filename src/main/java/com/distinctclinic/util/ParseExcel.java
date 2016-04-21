package com.distinctclinic.util;


import java.io.File;
import java.io.IOException;
import java.util.List;

import com.distinctclinic.domain.CrawlRequestVo;
import com.distinctclinic.domain.Item;
import com.distinctclinic.domain.Request;
import com.distinctclinic.model.Article;
import com.distinctclinic.service.ISearchService;
import com.weejinfu.common.exception.BaseException;
import com.weejinfu.common.util.JsonParser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by gaoqi on 2015/12/16.
 */
public class ParseExcel {

    @Autowired
    private ISearchService searchService;

    public static void main(String[] args) {
        readExcelToObj("G://PI整理3.xlsx");
    }

    private static void readExcelToObj(String path) {

        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(new File(path));
            readExcel(wb, 0, 1, 0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取excel文件
     * @param  wb
     * @param sheetIndex sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine 去除最后读取的行
     */
    private static void readExcel(Workbook wb,int sheetIndex, int startReadLine, int tailLine) {

        Sheet sheet = wb.getSheetAt(sheetIndex);
        Row row = null;

        for(int i=startReadLine; i<sheet.getLastRowNum()-tailLine+1; i++) {

            row = sheet.getRow(i);
            int q = 0;
            Article article = new Article();
            for(Cell c : row) {
                c.setCellType(Cell.CELL_TYPE_STRING);

                q++;
                if (q == 1) continue;
//                Cell cell = row.getCell(q);
                if (q == 4 && c.getHyperlink() != null) {
                    String url = c.getHyperlink().getAddress();
                    System.out.print(url);
                }
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                //判断是否具有合并单元格
                if(isMerge) {
                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    System.out.print(rs + "  ");
                }else {
                    System.out.print(c.getRichStringCellValue()+"  ");
                }

                if (q == 3) {
                    article.setTitle(c.getRichStringCellValue().getString());
                } else if (q == 4) {
//                    Request request = new Request();
//                    request.setUrl(c.getHyperlink().getAddress());
//                    CrawlRequestVo requestVo = searchService.build(request, "weixin_article_spider");
//                    String checkResult = scrawlService.post("/crawl.json", requestVo);
//
//                    if (checkResult == null) {
//                        throw new BaseException(500, "查找不到任何信息");
//                    }
//                    if (!JsonParser.read(checkResult, String.class, "status").equals("ok")) {
//                        throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
//                    }
//                    List<Item> item = JsonParser.read(checkResult, List.class, "items");
//
//                    if (item != null && item.size() > 0) {
//                        jsonData.put("item", item.get(0));
//
//                    } else {
//                        throw new BaseException(500, "未搜索到您输入的文章,请检查输入的连接是否有效");
//                    }
                }
            }
            System.out.println();

        }

    }

    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    public static String getMergedRegionValue(Sheet sheet ,int row , int column){

        int sheetMergeCount = sheet.getNumMergedRegions();

        for(int i = 0 ; i < sheetMergeCount ; i++){
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if(row >= firstRow && row <= lastRow){

                if(column >= firstColumn && column <= lastColumn){
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell) ;
                }
            }
        }

        return null ;
    }

    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private boolean isMergedRow(Sheet sheet,int row ,int column) {

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row == firstRow && row == lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     * @return
     */
    private static boolean isMergedRegion(Sheet sheet,int row ,int column) {

        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {

            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if(row >= firstRow && row <= lastRow){
                if(column >= firstColumn && column <= lastColumn){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     * @return
     */
    private boolean hasMerged(Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }

    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private void mergeRegion(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell){

        if(cell == null) return "";

        if(cell.getCellType() == Cell.CELL_TYPE_STRING){

            return cell.getStringCellValue();

        }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){

            return String.valueOf(cell.getBooleanCellValue());

        }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){

            return cell.getCellFormula() ;

        }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){

            return String.valueOf(cell.getNumericCellValue());

        }
        return "";
    }
}
