package config;

public class Constants {

	// This is the list of System Variables
	// Declared as 'public', so that it can be used in other classes of this
	// project
	// Declared as 'static', so that we do not need to instantiate a class
	// object
	// Declared as 'final', so that the value of this variable can't be changed

	public static final String URL = "http://www.bumrise.com";
	public static final String Path_TestData = "src/dataEngine/DataEngine.xlsx";
	public static final String Path_OR = "src/config/OR";
	public static final String File_TestData = "DataEngine.xlsx";

	// List of Data Sheet Column Numbers
	public static final int Col_TestCaseID = 0;
	public static final int Col_TestScenarioID = 1;
	public static final int Col_PageObject = 4;
	public static final int Col_ActionKeyword = 5;
	public static final int Col_RunMode = 2;
	public static final int Col_DataSet = 6;
	public static final int Col_TestStepResult = 7; 
	public static final int Col_TestCaseResult = 3;
	
	

	// List of Data Engine Excel sheets
	public static final String Sheet_TestSteps = "Test Steps";
	public static final String Sheet_TestCases = "Test Cases";
	
	//Variables for the PASS result and FAIL result
	public static final String KEYWORD_FAIL = "FAIL";
	public static final String KEYWORD_PASS = "PASS";

	// List of Test Data
	//blic static final String UserName = "rambinator";
	//public static final String Password = "Cacamaca100";

}
