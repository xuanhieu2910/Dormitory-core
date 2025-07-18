package teamit.hust.ktxcdshustbe.utility;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.exception.*;

import java.util.Arrays;

public class ValidateExcelUtils {
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

    public static void checkFileExcel(MultipartFile file){
        if (file.isEmpty()){
            throw new FileIsNullException();
        }
        if( Arrays.stream(Constants.FILE_EXCEL)
                .noneMatch(x->x.equals(FilenameUtils.getExtension(file.getOriginalFilename())))){
            throw new FileExtensionException();
        }
        if (!FileUtil.checkSizeFile(file)){
            throw new FileSizeException();
        };

    }

}
