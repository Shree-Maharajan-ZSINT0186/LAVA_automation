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

    public String servicesTabLocators = "ServicesTabLocators";
    @Given("Launch workbench the applications")
    public void launchTheApplication() throws Throwable {

        try {
            Utils.highlightedStepInfoLog("Examiner assigns the application to himself");

//            //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Login To LAC");
            WebActions.launchApplication("Examiner_host");

            WebActions.waitForElementToVisible(ExaminerLoginLocators, "loginPageTitle");

            //Selecting Tenant value
            WebActions.clickOn(ExaminerLoginLocators, "tenantDropdownField");
            WebActions.clickOn(ExaminerLoginLocators, "tenantSelectionTestCan"); // change this to test selenium
            WebActions.setWaitTime(6000);
            String tenantValue = WebActions.getElementText(ExaminerLoginLocators, "tenantValueText");
            System.out.println("tenant" +tenantValue);
            Utils.stepInfoLog("The added tenant value is: " +tenantValue);

            //Entering Username and Password
            String userName = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName,  "Examiner", "Examiner_username");
            String Password = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Examiner", "Examiner_password");
            WebActions.enterTextOn(ExaminerLoginLocators, "examinerUsernameTextbox", userName);
            WebActions.enterTextOn(ExaminerLoginLocators, "examinerPasswordTextbox", WebActions.decodeTheGivenValue(Password));

            //Clicking the Submit button
            WebActions.clickOn(ExaminerLoginLocators, "submitButton");
            WebActions.setWaitTime(4000);

            // Checking for the Grid or focused view
            // getElementSize with return 0 and 1. 0 is false and 1 is true
            int loginTerminationPopup = WebActions.getElementSize(ExaminerLoginLocators, "logginTerminationPopup2");
            System.out.println("val:" +loginTerminationPopup);
            if(loginTerminationPopup == 1) {
                WebActions.setWaitTime(1000);
                WebActions.clickOn(ExaminerLoginLocators, "sessionTerminatePopup");
            }else {
                System.out.println("Login is done");
            }

            // Verifying whether logged in

            WebActions.setWaitTime(4000);
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
            Utils.failedTestLog("There is an issue in logging");
            ScreenShotCapture.importScreenToReports("examiner Login");
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
            WebActions.enterTextOn(AssignApplicationToExaminer, "applicationIDTextbox",applicationId);
            WebActions.clickOn(AssignApplicationToExaminer,"clearMyselfExaminerDropdown");
            WebActions.JSclickOn(AssignApplicationToExaminer,"refreshIcon");
            WebActions.setWaitTime(1500);

            //select checkbox
            WebActions.clickOn(AssignApplicationToExaminer,"applicationIDTextbox");
            WebActions.performTabAndSpace(7);

            WebActions.checkBoxClick(AssignApplicationToExaminer,"applicationCheckBox");
//             assign myself button
            WebActions.clickOn(AssignApplicationToExaminer,"assignMySelfButton");
            WebActions.clickOn(AssignApplicationToExaminer,"ConfirmAssignYesButton");
            WebActions.setWaitTime(2000);

            // missing competency
            WebActions.clickOn(AssignApplicationToExaminer,"missingCompetencyAssignButton");
            Utils.passedTestLog("Examiner searched for required application and assignes to myself");

            //click process icon
             WebActions.clickOn(AssignApplicationToExaminer,"processIcon");


        } catch (Throwable e) {
            Utils.failedTestLog("There is an issue while Examiner searched for required application");
            ScreenShotCapture.importScreenToReports("search application by examiner to assign to myself");
            e.printStackTrace();
        }
    }
    @And("Examiner resets the layout")
    public void examinerResetsLayout() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add title pop-up");
        try {
            System.out.println("entered into application process page");
            WebActions.setWaitTime(3000);
            WebActions.clickOn(AssignApplicationToExaminer,"windowsIcon");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"resetLayout");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"handlePopup");
            Utils.passedTestLog("Layout is reset");
        } catch (Throwable e) {
            Utils.failedTestLog("There is a issues in resetting the layout");
            ScreenShotCapture.importScreenToReports("reset_layout");
            e.printStackTrace();
        }

    }
    @And("Examiner clicks on the mortgage process icon")
    public void examinerClicksMortgageProcess() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            Utils.highlightedStepInfoLog("Agent Approves Application");
            WebActions.setWaitTime(7000);
            WebActions.clickOn(AssignApplicationToExaminer,"mortgageProcessIcon");
            Utils.passedTestLog("Examiner clicked on the mortgage process ion");
        } catch (Throwable e) {
            Utils.failedTestLog("Examiner clicking on the mortgage process icon failed");
            ScreenShotCapture.importScreenToReports("Click_mortgage_process_icon");
            e.printStackTrace();
        }
    }
    @And("Examiner validates and completes mortgage registration service")
    public void examinerValidatesAndCompletesMortgageRegistrationService() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add title pop-up");
        String givenService="Mortgage Registration Service";
        examinerValidatesAndCompletesService(givenService);
    }
    @And("Examiner clicks on the lease process icon")
    public void examinerClicksLeaseProcess() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            WebActions.setWaitTime(7000);
            WebActions.clickOn(AssignApplicationToExaminer,"LeaseProcessIcon");
            Utils.passedTestLog("Examiner clicked on the lease process ion");
        } catch (Throwable e) {
            Utils.failedTestLog("Examiner clicking on the lease process icon failed");
            ScreenShotCapture.importScreenToReports("Click_lease_process_icon");
            e.printStackTrace();
        }
    }
    @And("Examiner cancels the lease registration service")
    public void examinerCancelLeaseRegistrationService() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        String givenService = "Lease Registration Service";
        examinerCancelService(givenService);
    }

    @And("Examiner clicks on the Transfer Of Ownership process icon")
    public void examinerClicksTransferOfOwnershipProcess() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            WebActions.setWaitTime(7000);
            WebActions.clickOn(AssignApplicationToExaminer,"transferOfOwnershipProcessIcon");
            Utils.passedTestLog("Examiner clicked on the Transfer Of Ownership process icon");
        } catch (Throwable e) {
            Utils.failedTestLog("Examiner clicking on the Transfer Of Ownership process icon failed");
            ScreenShotCapture.importScreenToReports("Click_transferOfOwnership_process_icon");
            e.printStackTrace();
        }
    }
    @And("Examiner validates and completes Transfer Of Ownership service")
    public void examinerValidatesAndCompletesTransferOfOwnershipService() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add title pop-up");
        String givenService="Transfer of Ownership Service";
        examinerValidatesAndCompletesService(givenService);
    }
    @And("Examiner reverts the service")
    public void examinerRevertsService() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            WebActions.setWaitTime(7000);
            WebActions.clickOn(AssignApplicationToExaminer,"transferOfOwnershipRevertIcon");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"handlePopup");
            Utils.passedTestLog("Examiner reverts the service");
        } catch (Throwable e) {
            Utils.failedTestLog("Service revert back got failed");
            ScreenShotCapture.importScreenToReports("Click_transferOfOwnership_revert_icon");
            e.printStackTrace();
        }
    }
    @And("Examiner clicks on the approve icon")
    public void examinerClicksApprove() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            WebActions.setWaitTime(7000);
            WebActions.clickOn(AssignApplicationToExaminer,"approveIcon");
            WebActions.clickOn(AssignApplicationToExaminer,"handlePopup");
            Utils.passedTestLog("Examiner approved the application");
        } catch (Throwable e) {
            Utils.failedTestLog("Application approval failed");
            ScreenShotCapture.importScreenToReports("application_approval_failed");
            e.printStackTrace();
        }
    }

    public void examinerValidatesAndCompletesService(String givenService) throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add title pop-up");
        try {
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"totalValidateIcon");
            String actualValidationSucessMsg = WebActions.getElementText(servicesTabLocators, "ServiceValidationSucessMsg");
            String expectedValidationSucessMsg = JSONReader.getJSONDataBlockKey("ServiceData", "MortgageValidation", "validationSucessMsg");
            System.out.println("actualValidationSucessMsg" + actualValidationSucessMsg);
            System.out.println("expectedValidationSucessMsg" + expectedValidationSucessMsg);
            if (actualValidationSucessMsg.equals(expectedValidationSucessMsg)) {
                Utils.stepInfoLog("Matched validation message is found : \" Expected : \"" + expectedValidationSucessMsg + "\" and Actual : \"" + actualValidationSucessMsg + "\" are equal");
            } else {
                Utils.failedStepInfoLog("Mismatch validation message is found : \" Expected : \"" + expectedValidationSucessMsg + "\" and Actual : \"" + actualValidationSucessMsg + "\" are not equal");
                Assert.fail("Different validation message is displayed");
                Assert.assertEquals("The values are not equal!", expectedValidationSucessMsg, actualValidationSucessMsg);

            }

            System.out.println("Close Validate button");
            WebActions.performEnter(1);
//          WebActions.JSclickOn(servicesTabLocators, "ServiceValidationCloseButton");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"totalCompleteIcon");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(AssignApplicationToExaminer,"handlePopup");
            WebActions.setWaitTime(5000);
            if(WebActions.isElementDisplayed(AssignApplicationToExaminer,"totalCompletedView"))
            {
                Utils.passedTestLog(givenService+" is processed successfully");
            }
            else {
                throw new Exception(givenService+" was not completed – final status not visible.");
            }

        }
        catch (Throwable e) {
            System.out.println(e);
            Utils.failedTestLog(givenService+" Processing got failed");
            ScreenShotCapture.importScreenToReports("service_process_issue");
            e.printStackTrace();
        }
    }
    public void examinerCancelService(String givenService) throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {

            WebActions.clickOn(AssignApplicationToExaminer, "totalCancelIcon");
            WebActions.setWaitTime(500);
            WebActions.clickOn(AssignApplicationToExaminer, "reasonButton");
            WebActions.setWaitTime(500);
            WebActions.checkBoxClick(AssignApplicationToExaminer, "reasonCheckBox1");
            WebActions.setWaitTime(500);
            WebActions.clickOn(AssignApplicationToExaminer, "selectButton");
            WebActions.setWaitTime(500);
            WebActions.clickOn(AssignApplicationToExaminer, "confirmButton");
            WebActions.setWaitTime(5000);
            if(WebActions.isElementDisplayed(AssignApplicationToExaminer,"totalCancelledView"))
            {
                Utils.passedTestLog(givenService+" is processed successfully");
            }
            else {
                throw new Exception(givenService+" was not cancelled – final status not visible.");
            }

        } catch (Throwable e) {
            Utils.failedTestLog(givenService+" cancellation failed");
            ScreenShotCapture.importScreenToReports("examiner_cancel_leaseService");
            e.printStackTrace();
        }
    }
}
