package jp.ac.aoyama.it.it_lab_3.business_trip_support_system;

//15818015_InoueYoshiaki

import jp.ac.aoyama.it.it_lab_3.business_trip2.BusinessTripModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;

//excelに出力する溜めの関数
public class Output {

    //regionに対してのデザイン設計
    private static void setRegionBorder(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.BLACK.getIndex(), region, sheet);
    }

    private static void setStyle(CellStyle style, Font font) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // 垂直方向の位置を中央揃えに設定
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // フォントの種類をメイリオに設定
        font.setFontName("メイリオ");
        // フォントのサイズを12ポイントに設定
        font.setFontHeightInPoints((short) 12);
        // セルのスタイルオブジェクトのフォントを設定
        style.setFont(font);
    }

    private static void setTitleStyle(CellStyle style, Font font) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // 水平方向の位置を中央揃えに設定
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontName("メイリオ");
        font.setFontHeightInPoints((short) 24);
        style.setFont(font);
    }

    private static void setSubTitleStyle(CellStyle style, Font font) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontName("メイリオ");
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
    }

    private static void setSubTitleStyle2(CellStyle style, Font font) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());

        // 水平方向の位置を中央揃えに設定
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontName("メイリオ");
        font.setFontHeightInPoints((short) 12);
        // フォントを太字に設定
        font.setBold(true);
        style.setFont(font);
    }

    public static void createExcelFile(BusinessTripModel model) {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("出張旅費申請書");
        Sheet sheet2 = wb.createSheet("出張計画書");

        // セルのスタイルオブジェクトの作成
        CellStyle style = wb.createCellStyle();
        // フォントオブジェクトを作成
        Font font = wb.createFont();
        setStyle(style, font);

        //タイトルスタイル_24メイリオ
        CellStyle title_style = wb.createCellStyle();
        Font title_font = wb.createFont();
        setTitleStyle(title_style, title_font);

        //サブタイトルスタイル_16メイリオ
        CellStyle sub_title_style = wb.createCellStyle();
        Font sub_title_font = wb.createFont();
        setSubTitleStyle(sub_title_style, sub_title_font);

        //sheet2サブタイトルスタイル_12メイリオ太字
        CellStyle sub_title_style2 = wb.createCellStyle();
        Font sub_title_font2 = wb.createFont();
        setSubTitleStyle2(sub_title_style2, sub_title_font2);

        //sheet1_1行目
        Row row0 = sheet1.createRow(0);
        row0.createCell(0).setCellValue("出張依頼申請書");
        sheet1.addMergedRegion(CellRangeAddress.valueOf("A1:C1"));
        row0.getCell(0).setCellStyle(title_style);

        //2行目内容
        String[] itemLabels = new String[]{
                "所属", "学部", "学科", "職名", "氏名", "所属機関名・部局"
                , "職名", "氏名", "出張目的", "用務地", "用務先", "日程"
                , "出張時間(日帰りの場合)", "日当", "宿泊費", "運賃"
        };
        for (int i = 0; i < itemLabels.length; i++) {
            sheet1.createRow(i + 1).createCell(1).setCellValue(itemLabels[i]);
            sheet1.getRow(i + 1).createCell(2);
            sheet1.getRow(i + 1).getCell(1).setCellStyle(style);
            sheet1.getRow(i + 1).getCell(2).setCellStyle(style);
        }

        //2行目タイトル
        sheet1.getRow(1).createCell(0).setCellValue("申請者");
        CellRangeAddress region = CellRangeAddress.valueOf("A2:A6");
        sheet1.addMergedRegion(region);
        setRegionBorder(region, sheet1);
        //文字スタイルの設定
        sheet1.getRow(1).getCell(0).setCellStyle(sub_title_style);

        sheet1.getRow(6).createCell(0).setCellValue("出張者");
        CellRangeAddress region1 = CellRangeAddress.valueOf("A7:A14");
        sheet1.addMergedRegion(region1);
        setRegionBorder(region1, sheet1);
        sheet1.getRow(6).getCell(0).setCellStyle(sub_title_style);

        sheet1.getRow(14).createCell(0).setCellValue("費用");
        CellRangeAddress region2 = CellRangeAddress.valueOf("A15:A17");
        sheet1.addMergedRegion(region2);
        setRegionBorder(region2, sheet1);
        sheet1.getRow(14).getCell(0).setCellStyle(sub_title_style);

        //3列目
        sheet1.getRow(1).getCell(2).setCellValue(model.getBelonging());
        sheet1.getRow(2).getCell(2).setCellValue(model.getDepartment());
        sheet1.getRow(3).getCell(2).setCellValue(model.getDepartment2());
        sheet1.getRow(4).getCell(2).setCellValue(model.getJobTitle());
        sheet1.getRow(5).getCell(2).setCellValue(model.getName());
        sheet1.getRow(6).getCell(2).setCellValue(model.getBelonging());
        sheet1.getRow(7).getCell(2).setCellValue(model.getJobTitle());
        sheet1.getRow(8).getCell(2).setCellValue(model.getName());
        sheet1.getRow(9).getCell(2).setCellValue(model.getPurpose());
        sheet1.getRow(10).getCell(2).setCellValue(model.getPlace());
        sheet1.getRow(11).getCell(2).setCellValue(model.getPlace2());
        sheet1.getRow(12).getCell(2).setCellValue(model.getAgenda());
        sheet1.getRow(13).getCell(2).setCellValue(model.getNumberOfNights());
        sheet1.getRow(14).getCell(2).setCellValue(model.getDailyAllowance());
        sheet1.getRow(15).getCell(2).setCellValue(model.getAccommodationFee());
        sheet1.getRow(16).getCell(2).setCellValue(model.getFare());

        //sheet2
        //1行目
        Row sheet2_row1 = sheet2.createRow(0);
        sheet2_row1.createCell(0).setCellValue("出張計画");
        sheet2.addMergedRegion(CellRangeAddress.valueOf("A1:J1"));
        sheet2_row1.getCell(0).setCellStyle(title_style);

        //2行目
        sheet2.createRow(1).createCell(0).setCellValue("年月日");
        CellRangeAddress sheet2_region_row2 = CellRangeAddress.valueOf("A2:F2");
        sheet2.addMergedRegion(sheet2_region_row2);
        setRegionBorder(sheet2_region_row2, sheet2);
        sheet2.getRow(1).getCell(0).setCellStyle(sub_title_style2);

        sheet2.getRow(1).createCell(6).setCellValue("移動または用務");
        CellRangeAddress sheet2_region_row2_2 = CellRangeAddress.valueOf("G2:J2");
        sheet2.addMergedRegion(sheet2_region_row2_2);
        setRegionBorder(sheet2_region_row2_2, sheet2);
        sheet2.getRow(1).getCell(6).setCellStyle(sub_title_style2);

        //3行目以降
        int NumOfItems = 3; //行数 ここ変える

        //日付の2次元リスト
        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();
        ArrayList<Integer> date = new ArrayList<Integer>();
        date.add(2021);
        date.add(1);
        date.add(25);
        rows.add(date);

        //内容のリスト
        ArrayList<String> contents = new ArrayList<String>();
        contents.add(model.getChanging());
        contents.add("内容2");
        contents.add("内容3");

        for (int i = 0; i < NumOfItems; i++) {
            sheet2.createRow(i + 2).createCell(1).setCellValue("年");
            sheet2.getRow(i + 2).createCell(3).setCellValue("月");
            sheet2.getRow(i + 2).createCell(5).setCellValue("日");

            sheet2.getRow(i + 2).createCell(0).setCellValue(rows.get(0).get(0));
            sheet2.getRow(i + 2).createCell(2).setCellValue(rows.get(0).get(1));
            sheet2.getRow(i + 2).createCell(4).setCellValue(rows.get(0).get(2));
            sheet2.getRow(i + 2).createCell(6).setCellValue(contents.get(i));

            //スタイル設定
            for (int j = 0; j < 6; j++)
                sheet2.getRow(i + 2).getCell(j).setCellStyle(style);

            //indexからエクセルの行数を作成
            String str_i = Integer.toString(i + 3);
            //rangeアドレスとして送る
            CellRangeAddress sheet2_region_row3_7 = CellRangeAddress.valueOf("G" + str_i + ":J" + str_i);
            sheet2.addMergedRegion(sheet2_region_row3_7);
            setRegionBorder(sheet2_region_row3_7, sheet2);
        }

        //列幅の設定
        sheet1.autoSizeColumn(0);
        sheet1.autoSizeColumn(1);
        sheet1.autoSizeColumn(2);
        sheet2.setColumnWidth(1, 256 * 3);
        sheet2.setColumnWidth(3, 256 * 3);
        sheet2.setColumnWidth(5, 256 * 3);

        try {
            // main/resources/static/output.xlsx にエクセルファイルを出力
            String outputFilePath = Output.class.getClassLoader().getResource("static").getPath()
                    + File.separator + "output.xlsx";
            System.out.println(outputFilePath);
            OutputStream fileOut = new FileOutputStream(outputFilePath);
            wb.write(fileOut);
            wb.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
