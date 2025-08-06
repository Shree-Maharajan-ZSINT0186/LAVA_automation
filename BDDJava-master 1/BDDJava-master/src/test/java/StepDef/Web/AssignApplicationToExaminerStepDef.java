package StepDef.Web;

import Helpers.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;

public class AssignApplicationToExaminerStepDef {
    public String AssignApplicationToExaminer = "AssignApplicationToExaminerLocators";
    static String yamlFileName = "config";
    FakerClassLibrary faker = new FakerClassLibrary();
    public String ExaminerLoginLocators = "ExaminerLoginPageLocators";
    public String ExaminerCreateTitleLocators = "ExaminerCreateTitleLocators";
    @Given("Launch workbench the applications")
    public void launchTheApplication() throws Throwable {

        try {

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
//            int loginTerminationPopup = WebActions.getElementSize(ExaminerLoginLocators, "logginTerminationPopup2");
//            System.out.println("val:" +loginTerminationPopup);
//            if(loginTerminationPopup == 1) {
//                WebActions.setWaitTime(1000);
//                WebActions.clickOn(ExaminerLoginLocators, "sessionTerminatePopup");
//            }else {
//                System.out.println("Login is done");
//            }
//
//            // Verifying whether logged in

            WebActions.setWaitTime(3000);
            int incompletePopUp = WebActions.getElementSize(ExaminerCreateTitleLocators, "incompletePopUp");
            System.out.println("val:" +incompletePopUp);
            if(incompletePopUp == 1) {
                WebActions.setWaitTime(1000);
                WebActions.JSclickOn(ExaminerCreateTitleLocators, "incompletePopupNoButton");
            }else {
                System.out.println("No incomplete works proceed with title creation");
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
            Utils.failedTestLog("There is an issue in logging in examiner application");
            ScreenShotCapture.importScreenToReports("examiner_Login");
            e.printStackTrace();
            //Assert.fail("Dashboard page title is NOT displayed");
        }
    }
    @Then("Navigate to workqueue and assign myself and click process")
    public void navigateToWorkQueue() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {

            // click workqueue
            WebActions.setWaitTime(2000);
            WebActions.clickOn(AssignApplicationToExaminer,"workqueueTab");
            WebActions.setWaitTime(1000);

            // enter application id
            String applicationId=Store.getApplicationId();
//            String applicationId="APP0212";
            Utils.stepInfoLog("application ID:"+applicationId);
            WebActions.enterTextOn(AssignApplicationToExaminer, "applicationIDTextbox",applicationId);
            WebActions.clickOn(AssignApplicationToExaminer,"clearMyselfExaminerDropdown");
            WebActions.JSclickOn(AssignApplicationToExaminer,"refreshIcon");
            WebActions.setWaitTime(1500);

            //select checkbox
//            WebActions.clickOn(AssignApplicationToExaminer,"applicationIDTextbox");
//            WebActions.performTabAndSpace(7);

            WebActions.checkBoxClick(AssignApplicationToExaminer,"applicationCheckBox");
            // assign myself button
            WebActions.clickOn(AssignApplicationToExaminer,"assignMySelfButton");
            WebActions.clickOn(AssignApplicationToExaminer,"ConfirmAssignYesButton");
            WebActions.setWaitTime(2000);

            // missing competency
            WebActions.clickOn(AssignApplicationToExaminer,"missingCompetencyAssignButton");
            Utils.passedTestLog("Examiner searched for required application and assignes to myself");

            //click process icon
             WebActions.clickOn(AssignApplicationToExaminer,"processIcon");
            Utils.passedTestLog("The application was successfully assigned to myself");

        } catch (Throwable e) {
            Utils.failedTestLog("There is an issue while Examiner assigning the application");
            ScreenShotCapture.importScreenToReports("seacrh application by examiner to assign to myself");
            e.printStackTrace();
        }
    }
}
