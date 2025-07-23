package Helpers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.aventstack.extentreports.MediaEntityBuilder;

/**
 * Class Description: This class is to Capturing the Screenshot
 *
 */
public class ScreenShotCapture {
	
	private ScreenShotCapture() {
        // Private constructor to hide the implicit public one
    }
	// ScreenShot Switch
	public static String screenshotSwitch;
	
	/**
	 * This method is used to Capturing screenshot.
	 * 
	 * @param driver
	 * @param screenShotName
	 * @return
	 * @throws IOException
	 */
	public static String capture(String screenShotName) throws IOException {
		Map<String, Object> snapData = YamlLoader.loadYamlFile("config");
		screenshotSwitch = snapData.get("Screenshot_switch").toString();
		if (screenshotSwitch.trim().equalsIgnoreCase("true")) {
			TakesScreenshot ts = (TakesScreenshot) WebActions.driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
				String dest = System.getProperty("user.dir") + File.separator + "src" + File.separator + "reports"
						+ File.separator + screenShotName + ".png";
				String dest1 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "reports"+
						File.separator + "reports"	+ File.separator + screenShotName + ".png";
				File destination = new File(dest);
				FileUtils.copyFile(source, destination);
				File destination1 = new File(dest1);
				FileUtils.copyFile(source, destination1);
		}
		return screenShotName;
	}
	
	public static void importScreenToReports(String screenShotName) throws Exception {
		WebActions.pageLoadWait();
		WebActions.setWaitTime(3000);
		ExtentReportSetup.getTest().info(MediaEntityBuilder.
				createScreenCaptureFromPath(capture(screenShotName)+".png").build());
		
	}
	public static void importScreenToReportsWithoutWait(String screenShotName) throws Exception {
		WebActions.pageLoadWait();
		ExtentReportSetup.getTest().info(MediaEntityBuilder.
				createScreenCaptureFromPath(capture(screenShotName)+".png").build());
		
	}
}
