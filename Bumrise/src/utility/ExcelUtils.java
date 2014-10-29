package utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import config.Constants;
import executionEngine.DriverScript;

public class ExcelUtils {
	public static XSSFSheet ExcelWSheet;
	public static XSSFWorkbook ExcelWBook;
	public static XSSFCell Cell;
	public static XSSFRow Row;

	// This method will set the File path and will open the Excel file
	// The Excel Path and SheetName will be passed as arguments to this method
	public static void setExcelFile(String Path) throws Exception {
		try {
			FileInputStream ExcelFile = new FileInputStream(Path);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			// ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			Log.error("Class Utils | Method setExcelFile | Exception desc : "
					+ e.getMessage());
			DriverScript.bResult = false;
		}
	}

	// This method is to read the test data from the Excel cells
	// In this we arguments are passed as Row Num and Col Num and Sheet Name
	public static String getCellData(int RowNum, int ColNum, String SheetName)
			throws Exception {
		ExcelWSheet = ExcelWBook.getSheet(SheetName);
		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String CellData = Cell.getStringCellValue();
			return CellData;
		} catch (Exception e) {
			Log.error("Class Utils | Method getCellData | Exception desc : "
					+ e.getMessage());
			DriverScript.bResult = false;
			return " ";
		}
	}

	// This method is to get the row count used in the excel sheet
	public static int getRowCount(String SheetName) {
		int number = 0;
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			number = ExcelWSheet.getLastRowNum() + 1;
		} catch (Exception e) {
			Log.error("Class Utils | Method getRowCount | Exception desc : "
					+ e.getMessage());
			DriverScript.bResult = false;
		}
		return number;
	}

	// This method is to get the Row number of the test case
	public static int getRowContains(String sTestCaseName, int colNum,
			String SheetName) throws Exception {
		int i = 0;
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int rowCount = ExcelUtils.getRowCount(SheetName);
			for (i = 0; i < rowCount; i++) {
				if (ExcelUtils.getCellData(i, colNum, SheetName)
						.equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
		} catch (Exception e) {
			Log.error("Class Utils | Method getRowContains | Exception desc : "
					+ e.getMessage());
			DriverScript.bResult = false;
		}
		return i;
	}

	// This method is to get the count of the steps of the test case
	public static int getTestStepsCount(String SheetName, String sTestCaseID,
			int iTestCaseStart) throws Exception {

		try {
			for (int i = iTestCaseStart; i <= ExcelUtils.getRowCount(SheetName); i++) {
				if (!sTestCaseID.equals(ExcelUtils.getCellData(i,
						Constants.Col_TestCaseID, SheetName))) {
					int number = i;
					return number;
				}
			}
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			int number = ExcelWSheet.getLastRowNum() + 1;
			return number;
		} catch (Exception e) {
			Log.error("Class Utils | Method getTestStepsCount | Exception desc : "
					+ e.getMessage());
			DriverScript.bResult = false;
			return 0;
		}
	}

	// This method is used to write values in the Excel file
	@SuppressWarnings("static-access")
	public static void setCellData(String Result, int RowNum, int ColNum,
			String SheetName) throws Exception {
		try {
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
			Row = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum, Row.RETURN_BLANK_AS_NULL);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
			} else {
				Cell.setCellValue(Result);
			}
			// Constant variables TestData path and TestData filename
			FileOutputStream fileOut = new FileOutputStream(
					Constants.Path_TestData);
			ExcelWBook.write(fileOut);
			fileOut.close();
			ExcelWBook = new XSSFWorkbook(new FileInputStream(
					Constants.Path_TestData));
		} catch (Exception e) {
			DriverScript.bResult = false;
		}
	}
}
