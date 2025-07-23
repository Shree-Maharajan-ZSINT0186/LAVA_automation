//package Runner;
//
//import static Helpers.GlobalData.env;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.TimeZone;
//
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.runner.RunWith;
//
//import Helpers.GlobalData;
//import Helpers.MSGraphMailTrigger;
//import Helpers.WebActions;
//import io.cucumber.junit.CucumberOptions;
//import io.cucumber.junit.Cucumber;
//
//@RunWith(Cucumber.class)
//
//@CucumberOptions(
//		features = "@target/failedrerun.txt", 
//		glue = "StepDef", 
//		monochrome = true,
//		publish=true,
//		plugin = {
//				"pretty",
//				"StepDef.Web.StepAndScenarioNameListener",
//				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
//				"json:target/rerun-cucumber-report.json" ,
//				"junit:target/cucumber-reports/xml-report.xml" // XML report
//		})
//
//public class TestRunnerRerun{
//	@BeforeClass
//	public static void setUp() throws Throwable {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
//        Date date = new Date();    
//        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // IST timezone
//        WebActions.executionStartDate = sdf.format(date);
//        TestRunner.testcaseRerunExe = true;
//	}
//	
//	@AfterClass 
//	public static void embedReport() throws Exception {	
//		String tagString = String.join(",", GlobalData.runnerTags);
//		if(String.valueOf(env.get("SentMail")).equalsIgnoreCase("Yes")) {
//		MSGraphMailTrigger.sendMailWithAttachment("Summary" ,tagString, WebActions.executionStartDate);
//		}
//	    //homepage.logoutFromApplication();
//			WebActions.driver.manage().deleteAllCookies();
//			WebActions.driver.quit();
//	}
//}