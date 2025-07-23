package Helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static Helpers.GlobalData.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Class Description: This class is including all the Selenium generic actions
 *
 */
public class WebActions {

	public static WebDriver driver;
	public static AppiumDriver mobileDriver;
	public static HashMap<String, String> deviceTypeHashMap = new HashMap<String, String>();
	public static AppiumDriverLocalService service;

	public static String executionStartDate;
	public static String exeStartDate;
	public static String reportStartDate;
	public static String scenarioName;
	public static String stepName;
	public static ArrayList<String> tags = new ArrayList<>();
	public static String testStatus;
	static String yamlFileName = "config";
	private static final Logger logger = Logger.getLogger(WebActions.class.getName());
	public static List<String> failedScenarioName= new ArrayList<>();
	public static String parentWindowName = null;
	public static String childWindowName = null;
	public static String categoryFinal=null;
	public static String price=null;

	/**
	 * Method Description: This method is used to Launch the driver using Driver Manager
	 * @return
	 * @throws Throwable
	 */
	public static WebDriver launchDriver() throws Throwable{
		try{
			env = YamlLoader.loadYamlFile(yamlFileName);
			String browser = String.valueOf(env.get("Browser"));
			if(driver == null || driver.toString().contains("(null)")){
				switch(browser.toLowerCase()) {
					case "chrome":
						WebDriverManager.chromedriver().setup();
						ChromeOptions chromeOptions = new ChromeOptions();
						chromeOptions.addArguments("--disable-extensions");
						chromeOptions.addArguments("--disable-popup-blocking");
						chromeOptions.addArguments("--clear-browsing-data");
						WebActions.driver = new ChromeDriver(chromeOptions);
						break;
					case "edge":
						WebDriverManager.edgedriver().setup();
						driver = new EdgeDriver();
						break;
					case "firefox":
						WebDriverManager.firefoxdriver().setup();
						driver = new FirefoxDriver();
						break;
					default:
						throw new AssertionError("Please provide a valid browser name");
				}
				WebActions.driver.manage().window().maximize();
//				driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(50));
//				int width = 1044;
//		        int height = 688;
//		        Dimension dimension = new Dimension(width, height);
//		        driver.manage().window().setSize(dimension);
//				Dimension windowSize = driver.manage().window().getSize();
//		        int screenWidth = windowSize.getWidth();
//		        int screenHeight = windowSize.getHeight();
//		        System.out.println("Screen Resolution: " + screenWidth + "x" + screenHeight);
			}
			//launchApplication();

		}catch(Exception e) {

			e.printStackTrace();
			//Utils.failedTestLog("Launching Driver is failed");
			ScreenShotCapture.importScreenToReports("launchDriverFail");
			Assert.fail(e.getMessage());
		}
		return driver;
	}

	/**
	 * Method Description: This method is used to start the appium server
	 * @return
	 * @throws Throwable
	 */
	/*
	 * public static void launchAppium() throws Throwable { try { service = new
	 * AppiumServiceBuilder().withArgument(GeneralServerFlag.BASEPATH, "/wd/hub/")
	 * .withAppiumJS(new
	 * File("C:\\Program Files\\nodejs\\node_modules\\npm\\node_modules\\qrcode-terminal\\lib\\main.js"
	 * ))
	 * .withArgument(GeneralServerFlag.SESSION_OVERRIDE).withIPAddress("127.0.0.1").
	 * usingPort(4723).withLogFile(new File("logFile.txt")).build();
	 * service.clearOutPutStreams(); service.start(); setWaitTime(5000);
	 * System.out.println("Appium Server Started Successfully"); } catch (Exception
	 * e) { e.printStackTrace();
	 * ScreenShotCapture.importScreenToReportsWithoutWait("Launch Appium Failed");
	 * Assert.fail(e.getMessage()); } }
	 */

	public static void launchAppium() throws Throwable {
		try {
			String command = "cmd.exe /c start appium --address 127.0.0.1 --port 4723 --base-path wd/hub";
			Process p = Runtime.getRuntime().exec(command);
			setWaitTime(5000);
			System.out.println("Appium Server Started Successfully");
		} catch (Exception e) {
			e.printStackTrace();
			ScreenShotCapture.importScreenToReportsWithoutWait("Launch Appium Failed");
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to start the Android Emulator device via Command Prompt
	 * @return
	 * @throws Throwable
	 */
	public static void launchEmulator(String deviceOS) throws Throwable {
		try {
			if (deviceOS.equalsIgnoreCase("Android")) {
				System.out.println("inside launch emulator method");
				/*
				 * String command = "cmd /c start cmd.exe /K \" cd "
				 * +System.getProperty("user.dir")+"\\src\\test\\resources\\APK && emulator" +
				 * ".bat\"";
				 */
				String command = "cmd.exe /c start cd C:\\Users\\kiruthika.ganesa\\AppData\\Local\\Android\\Sdk\\emulator && emulator -avd Pixel_7_API_35";
				Process childCmd = Runtime.getRuntime().exec(command);

				setWaitTime(7000);
				System.out.println("Emulator Started Successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ScreenShotCapture.importScreenToReportsWithoutWait("Launch emulator Failed");
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Launch the Android and iOS drivers
	 * @return
	 * @throws Throwable
	 */
	public static AppiumDriver launchMobileDriver(String deviceOS, String device) throws Throwable{
		try{
			if (deviceOS.equalsIgnoreCase("Android")) {
				deviceTypeHashMap.put(Thread.currentThread().getName(), "Android");
				UiAutomator2Options options = new UiAutomator2Options();
				options.setCapability("avd", device);
				options.setCapability("avdLaunchTimeout", "180000");
				options.setApp(System.getProperty("user.dir")+"/src/test/resources/APK/Amazon Shopping.apk");
				setWaitTime(4000);
				mobileDriver = new AppiumDriver(new URL("http://localhost:4723/wd/hub/"), options);
				System.out.println("App installed successfully");
			}else if(deviceOS.equalsIgnoreCase("iOS")) {

			}
			mobileDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

		}catch(Exception e) {

			e.printStackTrace();
			//Utils.failedTestLog("Launching Driver is failed");
			ScreenShotCapture.importScreenToReports("launchDriverFail");
			Assert.fail(e.getMessage());
		}
		return mobileDriver;
	}

	/**
	 * Method Description: This method is used to Launch the application
	 * @throws Throwable
	 */
	public static void launchApplication(String hostKey) throws Throwable{
		try{
			String host= YamlLoader.getHostFromYamlBasedOnEnv(yamlFileName, hostKey);
			if (driver != null) {
				System.out.println("Driver is initialized successfully."+driver);
			} else {
				System.out.println("Driver initialization failed."+driver);
			}
			driver.get(host);
			System.out.println(driver.getCurrentUrl());
		}catch(Exception e) {
			Utils.failedTestLog("Launching Application is failed");
			ScreenShotCapture.importScreenToReports("launchFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Click the Web element
	 * @param fileName
	 * @param ele
	 */
	public static void clickOn(String fileName, String ele)throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			if(driver.findElement(setOfLocators).isDisplayed()) {
				driver.findElement(setOfLocators).click();
			}
		} catch (Exception e) {
			Utils.failedTestLog(ele+"("+setOfLocators+") is not clickable at this moment");
			ScreenShotCapture.importScreenToReports("clickonFailed");
			e.printStackTrace();
			//Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to find the locator element
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static String findElement(String fileName, String ele) throws Throwable{
		String locators = null;
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			locators = (setOfLocators.toString()).split(": ")[1];
		}catch(Exception e) {
			Utils.failedTestLog("can't able to find this element "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("findElement_fail");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
		return locators;
	}

	/**
	 * Method Description: This method is used to find the locator element
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static void SendKeys(String fileName, String ele, Keys value) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			if(driver.findElement(setOfLocators).isDisplayed()) {
				driver.findElement(setOfLocators).clear();
				driver.findElement(setOfLocators).sendKeys(value);
			}
		}catch (Exception e) {
			Utils.failedTestLog("can't able to enter text for this element "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("enterTexton_fail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Clear a text from text box
	 * @param fileName
	 * @param ele
	 * @throws Throwable
	 */
	public static void clearText(String fileName, String ele) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			if(driver.findElement(setOfLocators).isDisplayed()) {
				driver.findElement(setOfLocators).clear();
			}
		}catch (Exception e) {
			Utils.failedTestLog("can't able to clear text for this locator "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("clearText_fail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Clear the text on text box based on Action KEYS
	 * @param fileName
	 * @param ele
	 * @throws Throwable
	 */
	public static void clearTextBasedOnkeys(String fileName, String ele) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			if(driver.findElement(setOfLocators).isDisplayed()) {
				driver.findElement(setOfLocators).sendKeys(Keys.CONTROL+"a");
				driver.findElement(setOfLocators).sendKeys(Keys.DELETE);
			}
		}catch (Exception e) {
			Utils.failedTestLog("can't able to clear text for this locator "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("clearTextBasedon_fail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Enter the value on text box
	 * @param fileName, ele, value
	 * @throws Throwable
	 */
	public static void enterTextOn(String fileName, String ele, String value) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			if(driver.findElement(setOfLocators).isDisplayed()) {
				//driver.findElement(setOfLocators).clear();
				driver.findElement(setOfLocators).sendKeys(value);
			}
		}catch (Exception e) {
			Utils.failedTestLog("can't able to enter text for this element "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("enterTexton_fail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Enter the value on text box for iframe window
	 */
	public void enterTextByWebEle(){
		WebElement iframeElement = driver.findElement(By.id("name"));
		iframeElement.sendKeys("Text inside iframe");
	}

	/**
	 * Method Description: This method is used to Select Value from Drop down
	 * @param fileName
	 * @param ele
	 * @param action
	 * @param value
	 * @throws Throwable
	 */
	public static void selectValueFromDropdown(String fileName, String ele, String action, String value) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			Select selectVal = new Select(driver.findElement(setOfLocators));
			switch(action.toLowerCase()){
				case "select by value":
					selectVal.selectByValue(value);
					break;
				case "select by text":
					selectVal.selectByVisibleText(value);
					break;
				case "select by index":
					selectVal.selectByIndex(Integer.parseInt(value));
					break;
				default:
					throw new AssertionError("Wrong action provided");
			}
		}catch (Exception e) {
			Utils.failedTestLog("Not selected any value for this locator "+ele+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("SelectValueFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Get element text
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public String getElementText(By ele){
		String text = driver.findElement(ele).getText();
		return text;
	}

	/**
	 * Method Description: This method is used to Get the text based on filename
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static String getElementText(String fileName, String ele) throws Throwable{
		String text="";
		try {
			pageLoadWait();
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			text = driver.findElement(setOfLocators).getText();
		}catch (Exception e) {
			Utils.failedTestLog("Getting text for this locator "+ele+"("+setOfLocators+")"+" is failed");
			ScreenShotCapture.importScreenToReports("GetTextFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return text;
	}

	/**
	 * Method Description: This method is used to Get placeholder value from text box using filename
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static String getPlaceholderVal(String fileName, String ele) throws Throwable{
		String text="";
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName,ele);
			text = driver.findElement(setOfLocators).getAttribute("placeholder");
		}catch (Exception e) {
			Utils.failedTestLog("Getting Placeholder for this locator "+ele+"("+setOfLocators+")"+" is failed");
			ScreenShotCapture.importScreenToReports("GetPlaceFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return text;
	}

	/**
	 * Method Description: This method is used to Get text using getAttribute("") method
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static String getHTMLAttribute(String fileName, String ele) throws Throwable{

		setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
		waitForElementToVisible(fileName,ele);
		String text = driver.findElement(setOfLocators).getAttribute("innerHTML");
		return text;
	}

	public static String getValueAttribute(String fileName, String ele) throws Throwable{
		System.out.print("attribute value");
		setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
		waitForElementToVisible(fileName,ele);
		String text = driver.findElement(setOfLocators).getAttribute("value");
		System.out.print("text"+text);
		return text;
	}


	/**
	 * Method Description: This method is used to Scroll the page based on the Webelement
	 * @param fileName
	 * @param ele
	 * @throws Throwable
	 */
	public static void scroll(String fileName, String ele) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			JavascriptExecutor jex = (JavascriptExecutor) driver;
			int size = getElementSize(fileName, ele);
			if(size!=0) {
				WebElement elem = driver.findElement(setOfLocators);
				jex.executeScript("arguments[0].scrollIntoView({ block: 'center', inline: 'center' });", elem);
			}else {
				System.out.println("element Not Present");
			}
		}catch (Exception e) {
			Utils.WarningTestLog("Scroll to "+ele+"("+setOfLocators+")"+" is failed");
			ScreenShotCapture.importScreenToReports("ScrollEleFail");
			e.printStackTrace();
		}
	}

	/**
	 * Method Description: This method is used to Scroll the page based on the Webelement
	 * @param fileName
	 * @param ele
	 * @throws Throwable
	 */
	public static void scrollDynamicElement(WebElement ele) throws Throwable{
		try {
			JavascriptExecutor jex = (JavascriptExecutor) driver;
			jex.executeScript("arguments[0].scrollIntoView({ block: 'center', inline: 'center' });", ele);
		}catch (Exception e) {
			Utils.WarningTestLog("Scroll to "+ele+"("+setOfLocators+")"+" is failed");
			ScreenShotCapture.importScreenToReports("ScrollEleFail");
			e.printStackTrace();
		}
	}

	/**
	 * Method Description: This method is used to Scroll the page based on the size
	 * @param size
	 * @throws Throwable
	 * @throws IOException
	 * @throws Throwable
	 */
	public static void scrollDownJS(int size) throws Throwable {
		try {
			JavascriptExecutor js = (JavascriptExecutor) WebActions.driver;
			js.executeScript("window.scrollBy(0,'"+size+"')", "");
		}catch(Exception e) {
			Utils.failedTestLog("Page scroll down is not working");
			ScreenShotCapture.importScreenToReports("ScrollFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Mouse hover the element
	 * @param fileName
	 * @param mainEle
	 * @throws Throwable
	 */
	public static void mouseHover(String fileName, String mainEle) throws Throwable {
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, mainEle);
			WebElement mainMenu = driver.findElement(setOfLocators);

			Actions action = new Actions(driver);
			action.moveToElement(mainMenu).perform();
		}catch(Exception e) {
			Utils.failedTestLog("Mousehover is not working for "+mainEle+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("mousehoverFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Mouse hover the element
	 * @param fileName
	 * @param mainEle
	 * @throws Throwable
	 */
	public static void mouseHoverJS(String fileName, String mainEle) throws Throwable {
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, mainEle);
			WebElement element = driver.findElement(setOfLocators);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

			// Trigger a "mouseover" event
			String script = "var event = new MouseEvent('mouseover', {"
					+ " 'bubbles': true,"
					+ " 'cancelable': true"
					+ "});"
					+ "arguments[0].dispatchEvent(event);";
			((JavascriptExecutor) driver).executeScript(script, element);

		}catch(Exception e) {
			Utils.failedTestLog("Mousehover is not working for "+mainEle+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("mousehoverJSFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to mouse hover and click the element
	 * @param fileName
	 * @param mainEle
	 * @param subEle
	 * @throws Throwable
	 */
	public static void mouseHoverClick(String fileName, String mainEle, String subEle) throws Throwable {
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, mainEle);
			WebElement mainMenu = driver.findElement(setOfLocators);

			Actions action = new Actions(driver);
			action.moveToElement(mainMenu).perform();

			By subLocator = JSONReader.getLocatorsFromJSONFile(fileName, subEle);
			WebElement subMenu = driver.findElement(subLocator);

			action.moveToElement(subMenu).click().build().perform();

		}catch(Exception e) {
			Utils.failedTestLog("Mousehover click is not working for "+mainEle+"("+setOfLocators+")");
			ScreenShotCapture.importScreenToReports("mousehovclickFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to mouse hover the element
	 * @param ele
	 * @throws Throwable
	 */
	public static void mouseHoverByEle(By ele) throws Throwable {
		try {
			WebElement mainMenu = driver.findElement(ele);
			Actions action = new Actions(driver);
			action.moveToElement(mainMenu).perform();
		}catch(Exception e) {
			Utils.failedTestLog("Mouse Hover is not working for the "+ele);
			ScreenShotCapture.importScreenToReports("mousehoverByeleFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to Wait for the element to visible
	 * @param fileName
	 * @param ele
	 */
	public static void waitForElementToVisible(String fileName, String ele) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions.visibilityOfElementLocated(setOfLocators));
		} catch (Exception e) {
			Utils.failedTestLog(ele+"("+setOfLocators+")"+" is not visible at this moment");
			ScreenShotCapture.importScreenToReports("waitFail");
			e.printStackTrace();
			//Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to Handle the browser window
	 * @throws Throwable
	 */
	public static void windowHandles() throws Throwable {
		try {
			String parentWindow = driver.getWindowHandle();
			Set<String> handles =  driver.getWindowHandles();
			// Switch to new window opened
			for(String windowHandle  : handles) {
				if(!windowHandle.equals(parentWindow)) {
					driver.switchTo().window(windowHandle);
				}
			}
		}catch(Exception e) {
			Utils.failedTestLog("Window handles is failed");
			ScreenShotCapture.importScreenToReports("WinFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	/**
	 * Method Description: This method is used to Get the timestamp
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("mmHHSS");
		Date date = new Date();
		return formatter.format(date);
	}

	/**
	 * Method Description: This method is used to Get the timestamp
	 * @return
	 */
	public static String getCurrentDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd-mmHHSS");
		Date date = new Date();
		return formatter.format(date);
	}

	/**
	 * Method Description: This method is used to Verify Is element displayed or not
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static boolean isElementDisplayed(String fileName, String ele) throws Throwable{
		boolean element = false;
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
			wait.until(ExpectedConditions.visibilityOfElementLocated(setOfLocators));
			element = driver.findElement(setOfLocators).isDisplayed();
		}catch(Exception e) {
			Utils.WarningTestLog(ele+"("+setOfLocators+")"+ " is not displayed in page");
			ScreenShotCapture.importScreenToReports("iseleFail");
			e.printStackTrace();

		}
		return element;
	}

	/**
	 * Method Description: This method is used to Verify Is the element clickable or not
	 * @param fileName
	 * @param ele
	 * @throws Throwable
	 */
	public static void isElementClickable(String fileName, String ele) throws Throwable{
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			WebElement clickableElement = driver.findElement(setOfLocators);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
			wait.until(ExpectedConditions.elementToBeClickable(clickableElement));
		}catch(Exception e) {
			Utils.failedTestLog(ele+"("+setOfLocators+")"+" is not clickable at this moment");
			ScreenShotCapture.importScreenToReports("isEleClickFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Method Description: This method is used to Get the size of the element
	 * @param fileName
	 * @param ele
	 * @return
	 * @throws Throwable
	 */
	public static int getElementSize(String fileName, String ele) throws Throwable{
		int eleSize = 0;
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			eleSize = driver.findElements(setOfLocators).size();
		}catch(Exception e) {
			Utils.failedTestLog(ele+"("+setOfLocators+") is not there or size is zero ");
			ScreenShotCapture.importScreenToReports("getSizeFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return eleSize;
	}

//	/**
//	 * Method Description: This method is used to Get data from Json
//	 * @param jsonObjName
//	 * @param key
//	 * @return
//	 * @throws Throwable
//	 */
//
//	public static String getDatafromJson(String jsonObjName, String key) throws Throwable {
//		try {
//
//			String dataBlock=JSONReader.getJSONDataBlock(jsonObjName);
//			JSONParser parser = new JSONParser();
//			JSONObject jsonObject = null;
//			Object parsedObject = parser.parse(dataBlock);
//			if (parsedObject instanceof JSONObject) {
//	            jsonObject = (JSONObject) parsedObject;
//	            key = jsonObject.get(key).toString();
//			}
//		}catch(Exception e) {
//			 Utils.failedTestLog(key+"( is not present in the Json ");
//			  ScreenShotCapture.importScreenToReports("getDataFromFail");
//			  e.printStackTrace();
//			  Assert.fail(e.getMessage());
//		}
//		return key;
//	}

	/**
	 * Method Description: This method is used to Get the Dynamic locator
	 * @param fileName,ele,subLocator,dynamicValue
	 * @return
	 * @throws Throwable
	 */
	public static String getDynamicLocatorValue(String fileName, String ele, String subLocator, String dynamicValue) throws Throwable{
		String locators = null;
		try {
			By subElement;
			String ele1;
			String element1;
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			if(subLocator.equals("")) {
				subElement = null;ele1 ="";element1 = "";
			}
			else {
				subElement = JSONReader.getLocatorsFromJSONFile(fileName, subLocator);
				ele1 =(subElement.toString()).split("xpath: ")[1];
				element1 = "::"+ele1;
			}
			String element2 = setOfLocators.toString()+element1;
			String locator= element2.split("xpath: ")[1];
			locators = String.format(locator, dynamicValue);
		}catch(Exception e) {
			Utils.failedTestLog(ele+"("+setOfLocators+") is not there or size is zero ");
			ScreenShotCapture.importScreenToReports("getJsonFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return locators;
	}

	/**
	 * Method Description: This method is used to Find the customized chropath
	 * @param fileName,locator,subLocator
	 * @return
	 * @throws Throwable
	 */
	public static String findCustomizedChropath(String fileName, String locator, String subLocator) throws Throwable{
		String locators = null;
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, locator);
			By subEle = JSONReader.getLocatorsFromJSONFile(fileName, subLocator);
			String ele1 =(subEle.toString()).split("xpath: ")[1];
			String element1 = "::"+ele1;
			String element2 = setOfLocators.toString()+element1;
			locators= element2.split("xpath: ")[1];
		}catch(Exception e) {
			Utils.failedTestLog(locator+"("+setOfLocators+") is not there or size is zero ");
			ScreenShotCapture.importScreenToReports("getChropathFail");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return locators;
	}

	/**
	 * Method Description: This method is used to Wait until the page load complete
	 * @throws Exception
	 */
	public static void pageLoadWait() throws Exception {
		int maxWaitTime = 60;
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		boolean pageLoaded = false;
		long startTime = System.currentTimeMillis();
		while (!pageLoaded && System.currentTimeMillis() - startTime < maxWaitTime * 1000) {
			pageLoaded = (Boolean) jsExecutor.executeScript("return document.readyState === 'complete'");
		}
		WebActions.setWaitTime(1000);
	}

	/**
	 * Method Description: This method is used to Get Yaml file env value
	 * @return
	 */
	public static String getYamlDataBasedOnEnv() {
		String executionInstance = null;
		try {
			env = YamlLoader.loadYamlFile(yamlFileName);
			executionInstance = env.get("Execution Instance").toString();
		}catch(Exception e) {
			e.printStackTrace();

		}
		return executionInstance;
	}

	/**
	 * Method Description: This method is used to Resize the window
	 */
	public void zoomOutBrowserSize(double zoomLevel) {
		try {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			String setZoomLevelScript = "document.body.style.zoom = arguments[0]";
			jsExecutor.executeScript(setZoomLevelScript, zoomLevel);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method Description: This method is used to Delete the directory
	 */
	public static boolean deleteDirectory(File directory) {
		if (directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					if (file.isDirectory()) {
						if (!deleteDirectory(file)) {
							return false; // If a subdirectory couldn't be deleted, return false
						}
					} else {
						try {
							Files.delete(file.toPath());
						} catch (IOException e) {
							logger.severe("Failed to delete a file. Error: " + e.getMessage());
							return false;
						}
					}
				}
			}
		} else {
			logger.severe("Not a directory: " + directory.getAbsolutePath());
			return false;
		}
		// After deleting all files and subdirectories, try to delete the directory itself
		return directory.delete();
	}

	public static void deleteFile(String filePath) {
		Path pathToDelete = Paths.get(filePath);

		if (Files.exists(pathToDelete)) {
			try {
				Files.delete(pathToDelete);
				logger.info("File deleted successfully.");
			} catch (IOException e) {
				logger.severe("Failed to delete the file. Error: " + e.getMessage());
			}
		} else {
			logger.severe("The file does not exist.");
		}
	}

	/**
	 * Method Description: Helper method to extract individual tags from a tag expression
	 * @param tagExpression
	 * @return
	 */
	public static List<String> extractTags(String tagExpression) {
		List<String> tags = new ArrayList<>();
		Pattern pattern = Pattern.compile("@[^\\s]+");
		Matcher matcher = pattern.matcher(tagExpression);
		while (matcher.find()) {
			tags.add(matcher.group());
		}
		return tags;
	}

	public static void setWaitTime(int waitTime) throws InterruptedException {
		Thread.sleep(waitTime);
	}

	public String getPropertyValue(String key) throws IOException {
		String value = null;
		FileInputStream fis = null;
		Properties prop = null;
		//Enable Below two line for azure execution and Remove secret.properties for azure push
		/*********************/
		String workingDirectory = System.getenv("System_DefaultWorkingDirectory");
		fis = new FileInputStream(workingDirectory+"\\secret.properties");
		/**************/
		//Enable Below line for local Execution
		//      fis = new FileInputStream("secret.properties");
		/********************/
		prop = new Properties();
		prop.load(fis);
		value = prop.getProperty(key);
		return value;
	}

	public static void switchToWindow(String windowTitle) throws Throwable {
		try {

			Set<String> handles = WebActions.driver.getWindowHandles();
			if(handles.size()>1) {
				// Switch to new window opened
				for(String windowHandle  : handles) {
					WebActions.driver.switchTo().window(windowHandle);
					if(WebActions.driver.getTitle().equals(windowTitle))
						//WebActions.driver.switchTo().window(windowHandle);
						break;
				}
			}
		}catch(Exception e) {
			Utils.failedTestLog("Cant able to switch to the "+windowTitle+" window");
			ScreenShotCapture.importScreenToReports("windowswitch");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void closeWindow(String windowTitle) throws Throwable {
		try {

			Set<String> handles = WebActions.driver.getWindowHandles();

			// Switch to new window opened
			for(String windowHandle  : handles) {

				if(WebActions.driver.getTitle().equals(windowTitle)) {
					WebActions.driver.close();
				}else {
					WebActions.driver.switchTo().window(windowHandle);
				}
			}
		}catch(Exception e) {
			Utils.failedTestLog("Cant able to close the "+windowTitle+" window");
			ScreenShotCapture.importScreenToReports("windowclose");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void switchNextToParentWindow(String windowTitle) throws Throwable {
		try {

			Set<String> handles = WebActions.driver.getWindowHandles();

			if(handles.size()>1) {
				// Switch to new window opened
				for(String windowHandle  : handles) {

					if((WebActions.driver.getTitle().equals(windowTitle)))
						WebActions.driver.switchTo().window(windowHandle);
				}
			}
		}catch(Exception e) {
			Utils.failedTestLog("Cant able to switch to the "+windowTitle+" parent window");
			ScreenShotCapture.importScreenToReports("windowparentswitch");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}



	public static void closeChildWindows() throws Throwable {
		try {
			String originalHandle = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();
			if(handles.size()>1) {
				for (String handle : handles) {
					if (!handle.equals(originalHandle)) {
						driver.switchTo().window(handle);
						driver.close();
					}
				}
				driver.switchTo().window(originalHandle);
			}

		}catch(Exception e) {
			Utils.failedTestLog("Cant able to close the child window");
			ScreenShotCapture.importScreenToReports("windowparentswitch");
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	public static void deletFileInDirectory(String directoryPath) {
		// Specify the directory path
		//String directoryPath = "path_to_your_directory";

		// Create a File object representing the directory
		File directory = new File(directoryPath);

		// List all files in the directory
		File[] files = directory.listFiles();

		// Delete each file
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					// Delete the file
					if (file.delete()) {
						System.out.println("Deleted file: " + file.getName());
					} else {
						System.out.println("Failed to delete file: " + file.getName());
					}
				}
			}
		}
	}

	public static void fluentOptionClick(String fileName, String data) throws Throwable {
		try {
			//String value = WebActions.getDynamicLocatorValue(fileName, "fluTextDropDown", "", data);
			String value = WebActions.getDynamicLocatorValue(fileName, "tenantSelectionDropdownBox", "", data);
			WebActions.driver.findElement(By.xpath(value)).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String decodeTheGivenValue(String encodedValue) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
		String decodedValue = new String(decodedBytes);
		return decodedValue;
	}

	public static void performTabAndSpace(int tabCount) {
		Actions actions = new Actions(driver);

		// Press Tab key 10 times
		for (int i = 0; i < tabCount; i++) {
			actions.sendKeys("\t").perform();
		}

		// Press Space key
		actions.sendKeys(" ").perform();
	}

	public static void JSclickOn(String fileName, String ele) throws Throwable {
		try {
			setOfLocators = JSONReader.getLocatorsFromJSONFile(fileName, ele);
			waitForElementToVisible(fileName, ele);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(80));
			wait.until(ExpectedConditions.elementToBeClickable(setOfLocators));
			// Find the WebElement using the locator
			WebElement element = driver.findElement(setOfLocators);
			// Use JavascriptExecutor to perform the click
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			jsExecutor.executeScript("arguments[0].click();", element);
		} catch (Exception e) {
			Utils.failedTestLog(ele + "(" + setOfLocators + ") is not clickable at this moment");
			ScreenShotCapture.importScreenToReports("clickonFailed");
			e.printStackTrace();
			Assert.fail(e.getMessage());

		}
	}

	public static void performESCFunction(int escCount) {
		Actions actions = new Actions(driver);

		// Press Esc key
		for (int i = 0; i < escCount; i++) {
			actions.sendKeys(Keys.ESCAPE).perform();
		}

	}
	public static void performEnter(int enterCount) {

		Actions actions = new Actions(driver);
		// Press Esc key
		for (int i = 0; i < enterCount; i++) {
			actions.sendKeys(Keys.ENTER).perform();
		}
	}

	public static boolean validateStrings(String s1, String s2, String key) {
		boolean value = false;
		if (s1 == null && s2 == null) {

			System.out.println("Expected and Actual values are null");
			value = false;
		}
		if (s1.equals(s2))
		{
			System.out.println("Matched Key : \""+key+"\" Expected : \""+s1+"\" and Actual : \""+s2+"\" are equal");
			Utils.stepInfoLog("Matched Key : \""+key+"\" Expected : \""+s1+"\" and Actual : \""+s2+"\" are equal");
			value = true;
		} // Compare the content of the strings.
		else
		{
			System.out.println("Mismatch found for key : \""+key+"\" Expected : \""+s1+"\" and Actual : \""+s2+"\" are not equal");
			Utils.failedStepInfoLog("Mismatch found for key : \""+key+"\" Expected : \""+s1+"\" and Actual : \""+s2+"\" are not equal");
			value = false;
		}
		return value;
	}


}
