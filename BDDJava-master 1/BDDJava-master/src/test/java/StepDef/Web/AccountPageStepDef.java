package StepDef.Web;

import Helpers.ScreenShotCapture;
import Helpers.Utils;
import Helpers.WebActions;
import io.cucumber.java.en.When;

public class AccountPageStepDef{
	
	public String AccountPageLocators =  "AccountPageLocators";
			

	@When("Agent click on the profile icon")
	public void agentClickOnTheProfileIcon() throws Exception {
	    try {
			WebActions.clickOn(AccountPageLocators,"profileIcon");
			Utils.passedTestLog("The Agent successfully clicked on the profile icon");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue while clicking the profile icon");
			ScreenShotCapture.importScreenToReports("Profile_icon");
			e.printStackTrace();
		}
	}
	@When("Agent click the Logout button")
	public void agentClickTheLogoutButton() throws Exception {
		 try {
				WebActions.clickOn(AccountPageLocators,"logoutButton");
				Utils.passedTestLog("The Agent successfully clicked on the logout button");
			} catch (Throwable e) {
				Utils.failedTestLog("There is an issue while clicking the logout button");
				ScreenShotCapture.importScreenToReports("Logout_btn");
				e.printStackTrace();
			}
	}



}