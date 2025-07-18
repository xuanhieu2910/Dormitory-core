package teamit.hust.ktxcdshustbe.utility;

import ch.qos.logback.core.util.FileSize;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.exception.FileExtensionException;
import teamit.hust.ktxcdshustbe.exception.FileIsNullException;
import teamit.hust.ktxcdshustbe.exception.FileSizeException;

import java.util.Arrays;
import java.util.Objects;

public class ExcelUtil {


    /**
     * Constants File Excel
     * */
    public static String[] FILE_EXCEL = {"xls", "xlsx", "xlsm"};

    public static String[] FILE_IMAGES = {"JPEG","PNG","JPG","GIF","PSD","PDF"};

    public static String[] MESSAGE_ERROR_FILE_EXCEL = {"Size to large 50Mb!","File không đúng định dạng!","File không có dữ liệu!","File error"};

    public static String[] MESSAGE_SUCCESS_FILE_EXCEL = {"Up load file success!"};
    /*-----------------------------------------------------*/

    public static Object convertValue(Cell cell, CellType cellType) {
        if(Objects.isNull(cell) || cell.getCellType().equals(CellType.BLANK)) {
            return null;
        }
        cell.setCellType(cellType);
        Object object;
        try {
            object = switch (cellType) {
                case STRING -> cell.getStringCellValue().trim().replaceAll("'", "");
                case NUMERIC -> Integer.valueOf(cell.getStringCellValue().trim().replaceAll("'", ""));
                default -> null;
            };
            return object;
        } catch (Exception ex) {
            return null;
        }
    }


    public static Integer validateRowLastNull(XSSFSheet xssfSheet, int currentRowIndex) {
        Row row = xssfSheet.getRow(currentRowIndex);
        if(row != null) {
            return currentRowIndex;
        }
        int countEmptyRow = 0;
        while(countEmptyRow <= 5 && isRowBlank(xssfSheet.getRow(currentRowIndex))) {
            countEmptyRow++;
        }

        return 0;
    }

    public static boolean isRowBlank(Row row) {
        for (int indexCell = 0; indexCell < 5; indexCell++) {
            Cell cell = row.getCell(indexCell);
            if (!validateFiledBlankOrNull(cell)) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateFiledBlankOrNull(Cell cellValue) {
        return cellValue == null || cellValue.getCellType() == CellType.BLANK ;
    }

    private static String regexDigit = "\\d+";
    private static int sizeRow = 10;

    public static boolean validateNumber(String cellValue) {
        return cellValue.matches(regexDigit);
    }

    public static boolean validateRowLastNull(Row row, XSSFSheet xssfSheet, int index, int sizeCell, int sizeCheckLastRowNull,int indexCellStart) {
        boolean check = false;
        for (int indexCell = indexCellStart; indexCell < sizeCell; indexCell++) {
            Cell cell = row.getCell(indexCell);
            if (validateNullOrBlank(cell)) {
                check = true;
            } else {
                return false;
            }
        }
        if (check) {
            Cell cell = null;
            check = false;
            int newIndex = index + sizeCheckLastRowNull;
            for (int i = index + 1; i <= newIndex; i++) {
                Row newRow = xssfSheet.getRow(i);
                for (int j = 1; j < sizeCell; j++) {
                    if(newRow!=null) {
                        cell = newRow.getCell(j);
                    }
                    if (validateNullOrBlankPhysical(cell)) {
                        return true;
                    }
                    if (validateNullOrBlank(cell)) {
                        check = true;
                    } else {
                        return false;
                    }
                }
            }
        }
        if (check) {
            return true;
        }
        return false;
    }

    public static boolean validateNullOrBlankPhysical(Cell cell) {
        try {
            validateNullOrBlank(cell);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static boolean validateNullOrBlank(Cell cell) {
        return  cell == null || cell.getCellType() == CellType.BLANK ;
    }

    public static boolean validateTimeRegex(String time) {
        return DateUtil.validateTimeRegex(time);
    }

    public static boolean validateDate(String date) {
        return DateUtil.validateDateRegex(date);
    }


    public static boolean checkFileDataEmpty(XSSFSheet xssfSheet, int sizeCell, int indexCellStart, int indexRowStart){
        for(int i = indexRowStart ; i < sizeRow ;i++){
            Row row = xssfSheet.getRow(i);
            for(int j=indexCellStart ; j<sizeCell;j++){
                if(!validateNullOrBlank(row.getCell(j))){
                    return false;
                }
            }
        }
        return true;
    }

    public static void checkFileExcel(MultipartFile file) {
        if (file.isEmpty()){
            throw new FileIsNullException();
        }
        if( Arrays.stream(FILE_EXCEL).noneMatch(x->x.equals(FilenameUtils.getExtension(file.getOriginalFilename())))){
            throw new FileExtensionException();
        }
        if (!FileUtil.checkSizeFile(file)){
            throw new FileSizeException();
        };

    }

    public static CellStyle cellStyle(Workbook workbook, boolean isBold, String fontNameStyle,
                                      boolean isCenter, boolean isBorder, short color) {
        CellStyle  cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(isBold);
        font.setFontName(fontNameStyle);
        font.setFontHeightInPoints((short) 13);
        font.setColor(color);
        cellStyle.setWrapText(true);
        if (isBorder) {
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
        }
        if (isCenter){
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
        }
        cellStyle.setFont(font);
        return cellStyle;
    }

}
