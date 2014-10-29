package config;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import static executionEngine.DriverScript.OR;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import executionEngine.DriverScript;

import utility.Log;

public class ActionKeywords {
	public static WebDriver driver;

	public static void openBrowser(String object, String data) {
		try {
			Log.info("Opening browser");
			driver = new FirefoxDriver();
		} catch (Exception e) {
			Log.info("not able to open the browser ---" + e.getMessage());
			// Set the value of the result variable to false
			DriverScript.bResult = false;
		}
	}

	public static void maximizeBrowser(String object, String data) {
		try {
			Log.info("maximize window");
			driver.manage().window().maximize();
		} catch (Exception e) {
			Log.info("Not able to maximize the browser ----" + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void navigate(String object, String data) {
		try {
			Log.info("navigate to website");
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.get(Constants.URL);
		} catch (Exception e) {
			Log.info("Not able to navigate to the website ----- "
					+ e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void click(String object, String data) {
		try {
			Log.info("click on element " + object);
			// Fetch the xpath of the element in the OR property file
			driver.findElement(By.xpath(OR.getProperty(object))).click();
		} catch (Exception e) {
			Log.info("Not able to click ----" + e.getMessage());
			DriverScript.bResult = false;
		}
	}
	//This method accepts two values (object name and data)
	public static void input(String object, String data) {
		try {
			Log.info("entering the text "+ object);
			driver.findElement(By.xpath(OR.getProperty(object))).sendKeys(
					data);
		} catch (Exception e) {
			Log.info("Not able to enter the text ---- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void close_popUp(String object, String data) {
		try {
			Log.info("closing the ad pop-up");
			String mainWindow = driver.getWindowHandle();
			System.out.println(mainWindow);
			Set<String> handler = driver.getWindowHandles();
			for (String handlesname : handler) {
				driver.switchTo().window(handlesname);

				String windowTitle = driver.getTitle();
				System.out.println(windowTitle);
				if (!handlesname.contains(mainWindow)) {
					driver.close();
				} else {
					System.out.println("this is the main window");
					driver.switchTo().defaultContent();
				}
				driver.switchTo().window(mainWindow);
				System.out.println(driver.getTitle());
			}
		} catch (Exception e) {
			Log.info("Not able to close the popup ---- " + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void waitFor(String object, String data) throws Exception {
		try {
			Log.info("wait for 8 seconds");
			Thread.sleep(8000);
		} catch (Exception e) {
			Log.info("Not able to wait ----" + e.getMessage());
			DriverScript.bResult = false;
		}
	}

	public static void closeBrowser(String object, String data) {
		try {
			Log.info("closing the browser");
			driver.quit();
		} catch (Exception e) {
			Log.info("Not able to close the browser ----" + e.getMessage());
			DriverScript.bResult = false;
		}
	}

}
