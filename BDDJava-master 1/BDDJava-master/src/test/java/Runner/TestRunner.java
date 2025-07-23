package Runner;

import static Helpers.GlobalData.env;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.MessagingException;

import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import Helpers.ExtentReportSetup;
import Helpers.GlobalData;
import Helpers.ScreenShotCapture;
import Helpers.SendAttachmentInEmail;
import Helpers.Utils;
import Helpers.WebActions;
import Helpers.YamlLoader;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import io.cucumber.junit.Cucumber;

@RunWith(Cucumber.class)

@CucumberOptions(
		features = "src/test/resources/features/Web", 
		glue = "StepDef", 
		monochrome = true,
		publish = true,
		dryRun = false,
		snippets = SnippetType.CAMELCASE,
		//tags= "@15mins or @Hourly or @Daily or @3Days or @1Week",
		tags= "@application",
		plugin = {"pretty",
				"json:target/cucumber-reports/json-report.json", // JSON report
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"StepDef.Web.StepAndScenarioNameListener", 
				"rerun:target/failedrerun.txt", // This is where you specify your Extent Report plugin
				"junit:target/cucumber-reports/xml-report.xml" // XML report
		    })

public class TestRunner {
	
	WebActions actions = new WebActions();
	public static List<String> scenarioNameList = new ArrayList<>();
	static WebDriver driver = WebActions.driver;
	public static String dataBlock;
	static JSONObject jsonObject = null;
	public static boolean testcaseRerunExe = false;
	public static String AccountPageLocators =  "AccountPageLocators";
	
	@BeforeClass
	public static void setUp() throws Throwable {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date date = new Date();    
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // IST timezone
        WebActions.executionStartDate = sdf.format(date);
		Utils.clearFolder();
		ExtentReportSetup.setUpExtentReportSummary();
	    ExtentReportSetup.setUpExtentReport();
		Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
		String appVal =String.valueOf(testDataValues.get("ApplicationType"));
		if(appVal.equalsIgnoreCase("Web")) {
		WebActions.launchDriver();
		}
	}
	
	@AfterClass 
	public static void embedReport() throws Throwable {
		try {
			int failedSceSize = 0;
			env = YamlLoader.loadYamlFile("config");
			for(String status: GlobalData.scenarioStatus) {
				if(status.equalsIgnoreCase("failed")) {
					failedSceSize++;
				}
			}
			System.out.println(String.valueOf(env.get("SentMail")));
			if(failedSceSize==0 && GlobalData.scenarioStatus.size()!=0 && String.valueOf(env.get("SentMail")).equalsIgnoreCase("true")) {
				String tagString = String.join(",", GlobalData.runnerTags);
				SendAttachmentInEmail.sendAttachmentInEmail("Summary", tagString);
				//MSGraphMailTrigger.sendMailWithAttachment("Summary" ,tagString, WebActions.executionStartDate);
							
			}
			WebActions.setWaitTime(2000);
			WebActions.clickOn(AccountPageLocators,"profileIcon");
			WebActions.setWaitTime(1000);
			WebActions.clickOn(AccountPageLocators,"logoutButton");
			Utils.passedTestLog("The Agent successfully LoggedOff the application");
			WebActions.setWaitTime(2000);
			WebActions.driver.manage().deleteAllCookies(); WebActions.driver.quit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebActions.driver.manage().deleteAllCookies(); WebActions.driver.quit();
		}
		 
	}
	
//	@AfterClass
	public static void appiumServerTearDown() throws Throwable {
		try {
			
			boolean running = WebActions.service.isRunning();
			System.out.println("Service" +running);
			if (running) {
				System.out.println("server is running");
				WebActions.service.stop();
				System.out.println("Appium server stopped successfully..........");
			}
//			WebActions.service.stop();
//			System.out.println("Appium server stopped successfully..........");
		} catch (Exception e) {
			e.printStackTrace();
			ScreenShotCapture.importScreenToReportsWithoutWait("Appium server teardown failed");
			Assert.fail(e.getMessage());
		}
	}
	
	//@AfterClass
	public static void EmulatorTearDown() throws Throwable {
		try {
			Runtime.getRuntime().exec("adb emu kill");	
		} catch (Exception e) {
			e.printStackTrace();
			ScreenShotCapture.importScreenToReportsWithoutWait("Emulator teardown failed");
			Assert.fail(e.getMessage());
		}
	}
}