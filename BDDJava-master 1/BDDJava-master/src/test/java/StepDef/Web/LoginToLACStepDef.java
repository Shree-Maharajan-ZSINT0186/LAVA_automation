package StepDef.Web;


import org.junit.Assert;

import Helpers.ExtentReportSetup;
import Helpers.ScreenShotCapture;
import Helpers.Utils;
import Helpers.WebActions;
import Helpers.YamlLoader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginToLACStepDef{
	
	public String loginLocators = "LoginPageLocators";
	static String yamlFileName = "config";

	@Given("Agent want to launch the application")
	public void agentWantToLaunchTheApplication() throws Exception {
		ExtentReportSetup.test = ExtentReportSetup.createtheTest("Login To LAC");
		try {
			Utils.passedTestLog("Agent Launched the application Successfully");
		} catch (Exception e) {
			Utils.failedTestLog("There is an issue while launching the application");
			ScreenShotCapture.importScreenToReports("Agent_launch");
			e.printStackTrace();
		}
		
	}
	@And("Agent should see the login page")
	public void agentOpenTheLoginPage() throws Exception {
		try {
			WebActions.waitForElementToVisible(loginLocators, "loginPageTitle");
			boolean LoginPageTitle = WebActions.isElementDisplayed(loginLocators, "loginPageTitle");
			String LoginPageTitleMsg = WebActions.getElementText(loginLocators, "loginPageTitle");
			if (LoginPageTitle) {
				 Assert.assertTrue("Login page title is displayed successfully." +LoginPageTitleMsg, LoginPageTitle);
			} else {
				Assert.fail("Login page title is NOT displayed");
			}
			Utils.passedTestLog("Agent is navigated to the Login Page");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in Login page");
			ScreenShotCapture.importScreenToReports("Login_Page");
			e.printStackTrace();
		}
	    
	}
	
	@And("Agent select the Tenant value")
	public void agentSelectTheTenantValue() throws Exception {
		try {
			WebActions.clickOn(loginLocators, "tenantDropdownField");
			WebActions.clickOn(loginLocators, "tenantSelectionTestCan");
			WebActions.setWaitTime(5000);
			Utils.passedTestLog("Tenent value is selected sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in selecting Tenant value");
			ScreenShotCapture.importScreenToReports("Tenant_value");
			e.printStackTrace();
		}
	}
	
	
	@When("Agent enter {string} and {string}")
	public void agentEnterTheUserNameAndPassword(String username, String password) throws Throwable {
		try {
			String userName = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Agent_username");
			String Password = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Agent_password");
			WebActions.enterTextOn(loginLocators, "agentUsernameTextbox", userName);
			WebActions.enterTextOn(loginLocators, "agentPasswordTextbox", WebActions.decodeTheGivenValue(Password));
			Utils.passedTestLog("Username and password are entered sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in entering Username and password");
			ScreenShotCapture.importScreenToReports("Username_password");
			e.printStackTrace();
		}
	}
	
	@When("Agent click on the login button")
	public void agentClickOnTheLoginButton() throws Throwable {
		try {
			WebActions.clickOn(loginLocators, "submitButton");
			Utils.passedTestLog("Submit button is clicked sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue with clicking the submit button.");
			ScreenShotCapture.importScreenToReports("Submit_btn");
			e.printStackTrace();
		}
	}
	@Then("Agent should see the dashboard")
	public void agentShouldSeeTheDashboard() throws Exception {
	    
		try {
			boolean HomeDashboardPageTitle = WebActions.isElementDisplayed(loginLocators, "homeDashboardPage");
			String HomeDashboardPageTitleText = WebActions.getElementText(loginLocators, "homeDashboardPage");
			if (HomeDashboardPageTitle) {
				 Assert.assertTrue("Dashboard page title is displayed successfully." +HomeDashboardPageTitleText, HomeDashboardPageTitle);
			} else {
				Assert.fail("Dashboard page title is NOT displayed");
			} 
			Utils.passedTestLog("Agent is logged in to the application sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in logging");
			ScreenShotCapture.importScreenToReports("Dashboard_Page");
			e.printStackTrace();
		}
	}
	



}