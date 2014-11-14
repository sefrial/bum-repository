package executionEngine;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;

import config.ActionKeywords;
import config.Constants;
import utility.ExcelUtils;
import utility.Log;

public class DriverScript {
	// This is a class method declared as 'public static'
	// So that it can be used outside the scope of main method
	public static Properties OR;
	public static ActionKeywords actionKeywords;
	public static String sActionKeyword;
	public static String sPageObject;
	public static Method method[];

	public static int iTestStep;
	public static int iTestLastStep;
	public static String sTestCaseID;
	public static String sRunMode;
	public static boolean bResult;
	public static String sData;

	public DriverScript() throws NoSuchMethodException, SecurityException {
		// Instantiating a new object of class 'ActionKeywords'
		actionKeywords = new ActionKeywords();

		// Load all the methods of the class 'ActionKeywords'
		method = actionKeywords.getClass().getMethods();
	}

	// First is the main method, execution starts from here
	public static void main(String[] args) throws Exception {
		// Get excel file path from Constants class variable
		// String sPath = Constants.Path_TestData;
		
		//File f = new File("");
		//System.out.println(f.getAbsolutePath());
		//System.exit(0);

		// Pass the Excel file path as argument
		ExcelUtils.setExcelFile(Constants.Path_TestData);

		// This will start the Log4j logging in the test case
		DOMConfigurator.configure("log4j.xml");

		// Declaring String variable for storing OR path
		String Path_OR = Constants.Path_OR;
		FileInputStream fs = new FileInputStream(Path_OR);

		// Create an object of properties
		OR = new Properties(System.getProperties());
		OR.load(fs);

		// Instantiate DriverScript object
		DriverScript startEngine = new DriverScript();
		startEngine.execute_TestCase();
	}

	// Second method is to figure out the test cases execution one by one and to
	// figure out test step execution one by one
	public void execute_TestCase() throws Exception {
		// this will return the total number of test cases mentioned in the Test
		// Cases sheet
		int iTotalTestCases = ExcelUtils.getRowCount(Constants.Sheet_TestCases);

		// This loop will execute number of times equal to Total number of test
		// cases
		for (int iTestcase = 1; iTestcase <= iTotalTestCases; iTestcase++) {
			// Setting the bResult value to 'true' before starting every test
			// case
			bResult = true;

			// This will get the Test case name from the Test Cases sheet
			sTestCaseID = ExcelUtils.getCellData(iTestcase,
					Constants.Col_TestCaseID, Constants.Sheet_TestCases);
			// This will get the the value from the Run Mode column for the
			// current test case
			sRunMode = ExcelUtils.getCellData(iTestcase, Constants.Col_RunMode,
					Constants.Sheet_TestCases);

			// This is the condition statement on Run Mode value
			if (sRunMode.equals("Yes")) {
				// Only if the value of the Run Mode is "Yes", this part of code
				// will execute
				iTestStep = ExcelUtils.getRowContains(sTestCaseID,
						Constants.Col_TestCaseID, Constants.Sheet_TestSteps);
				iTestLastStep = ExcelUtils.getTestStepsCount(
						Constants.Sheet_TestSteps, sTestCaseID, iTestStep);
				Log.startTestCase(sTestCaseID);
				// Setting the value of bResult variable to 'true' before
				// starting every test step
				bResult = true;
				// This loop will execute number of times equal to Total number
				// of test steps
				for (; iTestStep <= iTestLastStep; iTestStep++) {
					sActionKeyword = ExcelUtils.getCellData(iTestStep,
							Constants.Col_ActionKeyword,
							Constants.Sheet_TestSteps);
					sPageObject = ExcelUtils
							.getCellData(iTestStep, Constants.Col_PageObject,
									Constants.Sheet_TestSteps);
					// Use the data value and pass it to the methods
					sData = ExcelUtils.getCellData(iTestStep,
							Constants.Col_DataSet, Constants.Sheet_TestSteps);
					execute_Actions();
					// This is the result code , this code will execute after
					// each step
					// The execution flow will go in this only if the value of
					// bResult is 'false'
					if (bResult == false) {
						// If 'false' then store the test case result as Fail
						ExcelUtils.setCellData(Constants.KEYWORD_FAIL,
								iTestcase, Constants.Col_TestCaseResult,
								Constants.Sheet_TestCases);
						// End the test case in the logs
						Log.endTestCase(sTestCaseID);
						// by this break statement, the execution flow will not
						// execute any more test steps of the failed test case
						break;
					}
				}
				// this will execute after the last step of the test case, if
				// the value is not 'false' at any step
				if (bResult == true) {
					// Storing the result as Pass in the excel sheet
					ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestcase,
							Constants.Col_TestCaseResult,
							Constants.Sheet_TestCases);
					Log.endTestCase(sTestCaseID);
				}
			}
		}
	}

	// This method contains the code to perform the actions
	private static void execute_Actions() throws Exception {
		// This is a loop that will run for the number of actions in the
		// ActionKeyword class
		// method.length returns the total number of methods

		for (int i = 0; i < method.length; i++) {
			// This is now comparing the method name with the ActionKeyword
			// value got from excel
			if (method[i].getName().equals(sActionKeyword)) {
				// In case of match found, it will execute the matched method
				// and pass it three parameters
				method[i].invoke(actionKeywords, sPageObject, sData);
				// This code block will execute after every test step
				if (bResult == true) {
					// If the executed test step value is true, Pass the test
					// step in the Excel sheet
					ExcelUtils.setCellData(Constants.KEYWORD_PASS, iTestStep,
							Constants.Col_TestStepResult,
							Constants.Sheet_TestSteps);
					break;
				} else {
					// If the executed test step value is false, Fail the test
					// step in the Excel sheet
					ExcelUtils.setCellData(Constants.KEYWORD_FAIL, iTestStep,
							Constants.Col_TestStepResult,
							Constants.Sheet_TestSteps);
					// In case of 'false', the test execution will not reach the
					// last step of closing browser
					// So it makes sense to close the browser before moving on
					// to the next case
					ActionKeywords.closeBrowser("","");
					break;

				}
			}
		}
	}
}
