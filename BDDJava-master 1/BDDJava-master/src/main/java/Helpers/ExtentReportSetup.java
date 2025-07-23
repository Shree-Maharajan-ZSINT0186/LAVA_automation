package Helpers;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportSetup {
	
	private ExtentReportSetup() {
        // Private constructor to hide the implicit public one
    }
	
	public static ExtentReports extent;
	public static ExtentSparkReporter sparkReport;
	public static ExtentSparkReporter sparkReportFailed;
	public static ExtentTest test;
	public static ExtentReports extentfailed;
	public static ExtentReports extentSummary;
	public static ExtentTest testFailed;
	public static ExtentReports extentCheckStatus;
	public static ExtentTest testCheckStatus;
	//public static Instant timestamp = Instant.now();
	//public static String currentDate = WebActions.getCurrentDate();
	// Generate unique timestamp
	public static String currentDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

	
    public static void setUpExtentReport() throws IOException {
    	System.out.println("data:" +currentDate);
    	extent = new ExtentReports();
		ExtentSparkReporter sparkReport = new ExtentSparkReporter("src/reports/LAVA_Regression_Fail_" +currentDate+ ".html");
		File configFile = new File("src/main/java/Helpers/extent-config.xml");
		sparkReport.loadXMLConfig(configFile);
		ExtentSparkReporter sparkReportFailed = new ExtentSparkReporter("src/reports/FailedReports/LAVA_Failed_Report_1.html");
		sparkReportFailed.filter().statusFilter().as(new Status[] { Status.FAIL, Status.SKIP }).apply();
		sparkReportFailed.loadXMLConfig(configFile);
		extent.attachReporter(sparkReport, sparkReportFailed);
		
    }
    
    public static ExtentTest createtheTest(String featureName) {
        test = extent.createTest(featureName);
        return test;
    }


    public static ExtentReports getExtentReport() {
        return extent;
    }

    public static ExtentTest getTest() {
        return test;
    }
    
    public static void setUpExtentReportSummary() throws IOException {
    	extentSummary = new ExtentReports();
		ExtentSparkReporter sparkReport = new ExtentSparkReporter("src/reports/LAVA_Regression_Pass_" +currentDate+ ".html");
		File configFile = new File("src/main/java/Helpers/extent-config.xml");
		sparkReport.loadXMLConfig(configFile);
		ExtentSparkReporter sparkReportFailed = new ExtentSparkReporter("src/reports/FailedReports/LAVA_Failed_Report_2.html");
		sparkReportFailed.filter().statusFilter().as(new Status[] {Status.FAIL,Status.SKIP }).apply(); sparkReportFailed.loadXMLConfig(configFile);
		extentSummary.attachReporter(sparkReport,sparkReportFailed);

	}
}

