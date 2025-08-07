package StepDef.Web;

import Helpers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import jdk.jshell.execution.Util;
import org.junit.Assert;

public class AssignApplicationToExaminerStepDef {
    public String AssignApplicationToExaminer = "AssignApplicationToExaminerLocators";
    static String yamlFileName = "config";
    FakerClassLibrary faker = new FakerClassLibrary();
    public String ExaminerLoginLocators = "ExaminerLoginPageLocators";
    public String ExaminerCreateTitleLocators = "ExaminerCreateTitleLocators";
    public String ExaminerCancelApplication = "ExaminerCancelApplicationLocator";
    public String loginLocators = "LoginPageLocators";
    public String applicationLocators = "ApplicationPageLocators";
    @Given("Launch the workbench applications")
    public void launchTheApplication() throws Throwable {

        try {
            Utils.highlightedStepInfoLog("Examiner assign application to myself");
//            //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Login To LAC");
            WebActions.launchApplication("Examiner_host");
//
//            WebActions.waitForElementToVisible(ExaminerLoginLocators, "loginPageTitle");
//
//            //Selecting Tenant value
//            WebActions.clickOn(ExaminerLoginLocators, "tenantDropdownField");
//            WebActions.clickOn(ExaminerLoginLocators, "tenantSelectionTestCan"); // change this to test selenium
//            WebActions.setWaitTime(6000);
//            String tenantValue = WebActions.getElementText(ExaminerLoginLocators, "tenantValueText");
//            System.out.println("tenant" +tenantValue);
//            Utils.stepInfoLog("The added tenant value is: " +tenantValue);
//
//            //Entering Username and Password
//            String userName = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName,  "Examiner", "Examiner_username");
//            String Password = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Examiner", "Examiner_password");
//            WebActions.enterTextOn(ExaminerLoginLocators, "examinerUsernameTextbox", userName);
//            WebActions.enterTextOn(ExaminerLoginLocators, "examinerPasswordTextbox", WebActions.decodeTheGivenValue(Password));
//
//            //Clicking the Submit button
//            WebActions.clickOn(ExaminerLoginLocators, "submitButton");
//            WebActions.setWaitTime(4000);
//
//            // Checking for the Grid or focused view
//            // getElementSize with return 0 and 1. 0 is false and 1 is true

            // Verifying whether logged in
            int loginTerminationPopup = WebActions.getElementSize(ExaminerLoginLocators, "logginTerminationPopup2");
            System.out.println("val:" +loginTerminationPopup);
            if(loginTerminationPopup == 1) {
                WebActions.setWaitTime(1000);
                WebActions.clickOn(ExaminerLoginLocators, "sessionTerminatePopup");
            }else {
                System.out.println("Login is done");
            }
            // verify incomplete tasks
            WebActions.setWaitTime(3000);
            int incompletePopUp = WebActions.getElementSize(ExaminerCreateTitleLocators, "incompletePopUp");
            System.out.println("val incompletePopUp:" +incompletePopUp);
            if(incompletePopUp == 1) {
                WebActions.setWaitTime(1000);
                WebActions.JSclickOn(ExaminerCreateTitleLocators, "incompletePopupNoButton");
            }else {
                System.out.println("No incomplete works,so proceeding");
            }
            WebActions.setWaitTime(2000);
            boolean HomeDashboardPageTitle = WebActions.isElementDisplayed(ExaminerLoginLocators, "homePage");
            String HomeDashboardPageTitleText = WebActions.getElementText(ExaminerLoginLocators, "homePage");
            if (HomeDashboardPageTitle) {
                Assert.assertTrue("work queue title is displayed" +HomeDashboardPageTitleText, HomeDashboardPageTitle);
            } else {
                Assert.fail("work queue title is NOT displayed");
            }

            Utils.passedTestLog("Examiner is logged in to the workbench application sucessfully");
        } catch (Exception e) {
            Utils.failedTestLog("Examiner failed to log in");
            ScreenShotCapture.importScreenToReports("examiner Login");
            e.printStackTrace();
            Assert.fail("Examiner Failed to login");
        }
    }

    @And("Navigate to workqueue and assign myself")
    public void navigateToWorkQueue() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {

            // click workqueue
            WebActions.setWaitTime(2000);
            WebActions.clickOn(AssignApplicationToExaminer, "workqueueTab");
            WebActions.setWaitTime(1000);

            // enter application id
            String applicationId = Store.getApplicationId();
//            String applicationId = "APP0351";
            WebActions.enterTextOn(AssignApplicationToExaminer, "applicationIDTextbox", applicationId);
            WebActions.clickOn(AssignApplicationToExaminer, "clearMyselfExaminerDropdown");
            WebActions.JSclickOn(AssignApplicationToExaminer, "refreshIcon");
            WebActions.setWaitTime(1500);

            //select checkbox
//            WebActions.clickOn(AssignApplicationToExaminer,"applicationIDTextbox");
//            WebActions.performTabAndSpace(7);

            WebActions.checkBoxClick(AssignApplicationToExaminer, "applicationCheckBox");
            // assign myself button
            WebActions.clickOn(AssignApplicationToExaminer, "assignMySelfButton");
            WebActions.clickOn(AssignApplicationToExaminer, "ConfirmAssignYesButton");
            WebActions.setWaitTime(2000);

            // missing competency
            WebActions.clickOn(AssignApplicationToExaminer, "missingCompetencyAssignButton");
            Utils.passedTestLog("Examiner searched for required application" +applicationId+ "and assignes to myself");

        } catch (Throwable e) {
            Utils.failedTestLog("Examiner failed to search for the required application  and assign it to self.");
            Assert.fail("Application searched was not found");
            ScreenShotCapture.importScreenToReports("seacrh application by examiner to assign to myself");
            e.printStackTrace();
        }
    }

    @Then("Search and click Application to process")
    public void searchApplicationToProcess() throws Exception {
        try {

            WebActions.JSclickOn(AssignApplicationToExaminer, "refreshIcon");
            WebActions.setWaitTime(1500);

            //click process icon
            WebActions.clickOn(AssignApplicationToExaminer, "processIcon");
            Utils.passedTestLog("Process icon for the searched application is clicked successfully");
        } catch (Throwable e) {
            Utils.failedTestLog("Failed to click the Process icon for the searched application.");
            Assert.fail("Failed to search the application to process");
            ScreenShotCapture.importScreenToReports("examiner click cancel application button");
            e.printStackTrace();
        }
    }

    @Given("Examiner clicks the cancel button")
    public void examinerCancelApplication() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            Utils.highlightedStepInfoLog("Examiner cancel application");
            WebActions.clickOn(ExaminerCancelApplication, "cancelButton");
            WebActions.setWaitTime(500);
            Utils.passedTestLog("Application cancellation process initiated successfully by clicking the button.");
        } catch (Throwable e) {
            Utils.failedTestLog("Failed to click the cancel application button");
            Assert.fail("Application cancellation failed");
            ScreenShotCapture.importScreenToReports("examiner click cancel application button");
            e.printStackTrace();
        }
    }

    @And("gives the reason for cancellation")
    public void ReasonForCancellation() throws Exception {
        try {
            WebActions.clickOn(ExaminerCancelApplication, "reasonButton");
            WebActions.setWaitTime(500);
            WebActions.checkBoxClick(ExaminerCancelApplication, "reasonCheckBox1");
            WebActions.setWaitTime(500);
            WebActions.clickOn(ExaminerCancelApplication, "selectButton");
            WebActions.setWaitTime(500);
            WebActions.clickOn(ExaminerCancelApplication, "confirmButton");
            Utils.passedTestLog("selected the reason for cancellation from checkbox and confirmed the cancellation");
        } catch (Throwable e) {
            Utils.failedTestLog("Failed to complete cancellation");
            ScreenShotCapture.importScreenToReports("application_cancellation");
            e.printStackTrace();
        }

    }

    @Then("launch agent and navigate to application tab")
    public void checkCancelledApplicationInAgent() throws Throwable {
          //launch agent application
        WebActions.launchApplication("Agent_host");
        WebActions.setWaitTime(1500);
        WebActions.clickOn(applicationLocators, "applicationTab");
        int loginTerminationPopup = WebActions.getElementSize(loginLocators, "logginTerminationPopup2");
        System.out.println("val:" + loginTerminationPopup);
        if (loginTerminationPopup == 1) {
            WebActions.setWaitTime(1000);
            WebActions.clickOn(loginLocators, "sessionTerminatePopup");
        } else {
            System.out.println("Login is done");
        }

        // Verifying whether logged in
        boolean HomeDashboardPageTitle = WebActions.isElementDisplayed(loginLocators, "homeDashboardPage");
        String HomeDashboardPageTitleText = WebActions.getElementText(loginLocators, "homeDashboardPage");
        if (HomeDashboardPageTitle) {
            Assert.assertTrue("Dashboard page title is displayed successfully." + HomeDashboardPageTitleText, HomeDashboardPageTitle);
        } else {
            Assert.fail("Dashboard page title is NOT displayed");
        }


    }

    @Then("Search and check the cancelled application")
    public void checkCancelledApplicationStatus()throws Exception  {
        try {
              String applicationId = Store.getApplicationId();
//            String applicationId = "APP0256";
            WebActions.enterTextOn(applicationLocators, "applicationFilterTextBox", applicationId);
            WebActions.setWaitTime(500);
            String actualStatus=  WebActions.getElementText(applicationLocators,"applicationStatus");
            Assert.assertEquals("Cancelled", actualStatus);
        }catch (Throwable e) {
            Utils.failedTestLog("There is an issue while checking the status of cancelled application");
            ScreenShotCapture.importScreenToReports("There is an issue while filtering the application");
            e.printStackTrace();
        }
    }

}
