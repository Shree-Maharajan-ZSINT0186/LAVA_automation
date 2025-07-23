package StepDef.Web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import org.openqa.selenium.WebDriver;

import Helpers.ExtentReportSetup;
import Helpers.GlobalData;
import Helpers.ScreenShotCapture;
import Helpers.SendAttachmentInEmail;
import Helpers.Utils;
import Helpers.WebActions;
import Helpers.YamlLoader;
import Runner.TestRunner;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


public class Hooks {
	public static WebDriver driver;
	static WebActions actions = new WebActions();
	static String screenShotPath = null;
	public String AccountPageLocators =  "AccountPageLocators";
	
	
	@Before(order=1)
	public void beforeScenario(Scenario scenario) throws Exception{
		// Get the tags associated with the scenario
		Collection<String> scenarioTagsCollection = scenario.getSourceTagNames();
		for (String tag : scenarioTagsCollection) {
	        if (!GlobalData.runnerTags.contains(tag)) {
	            GlobalData.runnerTags.add(tag);
	        }
	    }
			
		int count=0;
		TestRunner.scenarioNameList.add(scenario.getName()); // Add the scenario name to the list
		for (String scenarioName : TestRunner.scenarioNameList) {
            if (scenarioName.equals(scenario.getName())) {
                count++;
            }
        }
		if(count>1) {
			ExtentReportSetup.test = ExtentReportSetup.createtheTest(scenario.getName() + "_Rerun");
		}else {
			ExtentReportSetup.test = ExtentReportSetup.createtheTest(scenario.getName());
		}
		ExtentReportSetup.test=ExtentReportSetup.getTest();
		Utils.reportHeader("Scenario: " + scenario.getName());
	}
	
// This method will open the browser
	@Before(order=0)
	public static void openBrowser() throws Throwable{
		ExtentReportSetup.setUpExtentReport();
		// Create a SimpleDateFormat object with IST timezone
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date date = new Date();    
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // IST timezone
        WebActions.exeStartDate = sdf.format(date);
        
		
	}	

	@After(order=0)
	public void afterScenarioFinish(Scenario scenario) throws Throwable{
		Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
		String appVal =String.valueOf(testDataValues.get("ApplicationType"));
		if(appVal.equalsIgnoreCase("Web")) {
			if (scenario.isFailed()) {
				Utils.getBrowserFailedLog(scenario.getName().replace(" ", "").replace("\"", ""));
			}	

		//WebActions.closeChildWindows();
		}
		ExtentReportSetup.extent.flush();
	}

	@After(order = 1)
	public void afterScenario(Scenario scenario) throws Exception {
		Map<String, Object> testDataValues=YamlLoader.loadYamlFile("config");
		String Sentmail =String.valueOf(testDataValues.get("SentMail"));
		String sceStatus = scenario.getStatus().toString();
		GlobalData.scenarioStatus.add(sceStatus);
		if (scenario.isFailed()) {
			int failureCount = 0;
			WebActions.testStatus = "Failed";
			WebActions.scenarioName = scenario.getName();
			String tagString = String.join(",", GlobalData.runnerTags);
			//ScreenShotCapture.importScreenToReports("Scenario_failed");
			Utils.failedSummaryTestLog(scenario.getName());
			for (String element : WebActions.failedScenarioName) {
	            if(element.equals(WebActions.scenarioName )) {
	            	failureCount++;
	            	break;
	            }           	
			}
			ExtentReportSetup.extent.flush();
	            if(failureCount!=0 && Sentmail.equalsIgnoreCase("true")) {
	            	ExtentReportSetup.extent.flush();
	            	SendAttachmentInEmail.sendAttachmentInEmail(WebActions.testStatus, tagString);
	            //	MSGraphMailTrigger.sendMailWithAttachment(WebActions.testStatus ,tagString, WebActions.exeStartDate);     	
	            }else {
	            	WebActions.failedScenarioName.add(WebActions.scenarioName);
	            }
		} else {
			String directoryPath = "src\\reports\\reports"; 
			File directory = new File(directoryPath);
			WebActions.deleteDirectory(directory);
			Utils.passedSummaryTestLog("Scenario Passed: " + scenario.getName());
			for (String tag : GlobalData.runnerTags) {
				String value = scenario.getSourceTagNames().toString().replace("[","");
				value = value.replace("]","");
				String[] tags = value.split(",");
				for(int i =0;i<tags.length;i++) {
					if(tags[i].equals(tag))
						WebActions.tags.add(tag);
				}
			}
			ExtentReportSetup.extent.flush();
		}
		ExtentReportSetup.extentSummary.getReport().getTestList().addAll(ExtentReportSetup.extent.getReport().getTestList());
		ExtentReportSetup.extentSummary.flush();
	}
	
	//@AfterAll
	public void afterScenarioApplicationLogout() throws Exception {
		try {
			WebActions.setWaitTime(5000);
			WebActions.clickOn(AccountPageLocators,"profileIcon");
			WebActions.setWaitTime(2000);
			WebActions.clickOn(AccountPageLocators,"logoutButton");
			Utils.passedTestLog("The Agent successfully LoggedOff the application");
			WebActions.driver.manage().deleteAllCookies(); 
			WebActions.driver.quit();
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue while logging off");
			ScreenShotCapture.importScreenToReports("Log_out");
			e.printStackTrace();
			WebActions.driver.manage().deleteAllCookies(); 
			WebActions.driver.quit();
		}
	}
}
