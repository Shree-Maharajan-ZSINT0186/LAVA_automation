package StepDef.Web;
import Helpers.*;
import org.junit.Assert;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Enumeration;
public class ExaminerCreateTitleStepDef extends FakerClassLibrary{

    public String ExaminerLoginLocators = "ExaminerLoginPageLocators";
    public String ExaminerCreateTitleLocators = "ExaminerCreateTitleLocators";
    public String titlesAndParcelsTabLocators = "TitlesAndParcelsTabLocators";
    public String partiesTabLocators = "PartiesTabLocators";
    public String servicesTabLocators ="ServicesTabLocators";
    static String yamlFileName = "config";
    FakerClassLibrary faker = new FakerClassLibrary();
    public Dictionary<String, String> expected_Details_Dict = new Hashtable<>();
    public Dictionary<String, String> actual_Details_Dict = new Hashtable<>();
    public String RefferenceID;


    @Given("Launch workbench the application")
    public void launchTheApplication() throws Throwable {

        try {
            //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Login To LAC");
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
            boolean HomeDashboardPageTitle = WebActions.isElementDisplayed(ExaminerLoginLocators, "homePage");
            String HomeDashboardPageTitleText = WebActions.getElementText(ExaminerLoginLocators, "homePage");
            if (HomeDashboardPageTitle) {
                Assert.assertTrue("work queue title is displayed" +HomeDashboardPageTitleText, HomeDashboardPageTitle);
            } else {
                Assert.fail("work queue title is NOT displayed");
            }
            int incompletePopUp = WebActions.getElementSize(ExaminerCreateTitleLocators, "incompletePopUp");
            System.out.println("val:" +incompletePopUp);
            if(incompletePopUp == 1) {
                WebActions.setWaitTime(1000);
                WebActions.JSclickOn(ExaminerCreateTitleLocators, "incompletePopupNoButton");
            }else {
                System.out.println("No incomplete works proceed with title creation");
            }
            Utils.passedTestLog("Examiner is logged in to the workbench application sucessfully");
        } catch (Exception e) {
            Utils.failedTestLog("There is an issue in logging");
            ScreenShotCapture.importScreenToReports("examiner Login");
            e.printStackTrace();
            //Assert.fail("Dashboard page title is NOT displayed");
        }
    }
    @And("Navigate to the standalone title tab")
    public void navigateToTheStandAloneTitleTab() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
        try {
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"SearchTab");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators, "titleTab");
            Utils.passedTestLog("Examiner navigated to standalone title tab");
        } catch (Throwable e) {
            Utils.failedTestLog("There is an issue while navigating to the standalone title tab");
            ScreenShotCapture.importScreenToReports("standalone title tab");
            e.printStackTrace();
        }
    }

    @And("Add party details of new title")
    public void addPartyDetailsOfNewTitle() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Creating New Application");
        try {
            //Clicking on the create title button
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators, "addTitleIcon");
            WebActions.clickOn(ExaminerCreateTitleLocators, "addTitleIcon");

            

            // in parties tab click search icon
            WebActions.clickOn(ExaminerCreateTitleLocators, "createTitleSearchPartiesIcon");
            WebActions.setWaitTime(5000);

            // enter party name in text box and search
            WebActions.clickOn(ExaminerCreateTitleLocators,"partiesClearButton");
            WebActions.setWaitTime(1000);
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators, "partiesNameTextbox");
            WebActions.enterTextOn(ExaminerCreateTitleLocators, "partiesNameTextbox",
                    JSONReader.getJSONDataBlockKey("PartiesData", "Party", "name"));
            String partyName = JSONReader.getJSONDataBlockKey("PartiesData", "Party", "name");
            Utils.stepInfoLog("The selected Party name is:" +partyName);
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators, "searchParties");
            WebActions.setWaitTime(5000);
            WebActions.clickOn(ExaminerCreateTitleLocators, "partiesNameTextbox");
            WebActions.performTabAndSpace(16);
            WebActions.clickOn(ExaminerCreateTitleLocators, "addPartyButton");
            Utils.passedTestLog("Party is added sucessfully");
        } catch (Throwable e) {
            Utils.failedTestLog("There is an issue while adding party");
            ScreenShotCapture.importScreenToReports("Application_details");
        }
    }
     @And("Add parcel details of new title")
    public void addParcelDetailsOfNewTitle() throws Exception {
        //ExtentReportSetup.test = ExtentReportSetup.createtheTest("Creating New Application");
        try {
            //Clicking on the parcel tab
            WebActions.setWaitTime(1500);
            WebActions.clickOn(ExaminerCreateTitleLocators, "createTitleParcelsTab");
            WebActions.clickOn(ExaminerCreateTitleLocators, "createTitleSearchParcelIcon");

            //enter in text box
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"parcelClearButton");
            WebActions.setWaitTime(1500);
            WebActions.enterTextOn(ExaminerCreateTitleLocators, "parclesIDTextbox",
                    JSONReader.getJSONDataBlockKey("TitleAndParcelsData", "Parcel", "parcelID"));
            WebActions.clickOn(ExaminerCreateTitleLocators, "searchParcels");
            WebActions.setWaitTime(2000);
            WebActions.clickOn(ExaminerCreateTitleLocators, "titleIDTextBox");

            WebActions.performTabAndSpace(15);
            // click add button
            WebActions.setWaitTime(3000);
            WebActions.clickOn(ExaminerCreateTitleLocators, "addParcelButton");

            Utils.passedTestLog("Parcel is added sucessfully");
        } catch (Throwable e) {
            Utils.failedTestLog("There is an issue while adding parcel");
            ScreenShotCapture.importScreenToReports("Application_details");
        }
    }

    @And("Get the newTitleID")
    public void getNewTitleID() throws Exception{
        try{
            //navigate to tilte tab
            WebActions.setWaitTime(1500);
            WebActions.clickOn(ExaminerCreateTitleLocators,"createTitleNewTitleTab");
            WebActions.setWaitTime(1500);
            String titleID= WebActions.getValueAttribute(ExaminerCreateTitleLocators,"newTitleID");
            System.out.println("Captured Title ID: " + titleID);
            Store.addTitleId(titleID);
        } catch(Throwable e){
            Utils.failedTestLog("There is an issue while getting titleID");
            ScreenShotCapture.importScreenToReports("Application_details");
        }
    }

    @And("enter the new title details")
    public void enterNewTitleDetails() throws Exception{
        try{

            //select legal framework
            WebActions.setWaitTime(1500);
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"newTitleLegalFrameworkDropdown");
            WebActions.JSclickOn(ExaminerCreateTitleLocators,"newTitleLegalFrameworkDropdown");
            WebActions.clickOn(ExaminerCreateTitleLocators,"selectLegalFramework");
            System.out.print("legal framework selected");

//            BAU relationship
            WebActions.setWaitTime(1500);
            WebActions.clickOn(ExaminerCreateTitleLocators,"BAUHeading");
            int parentTitle=WebActions.getElementSize(ExaminerCreateTitleLocators,"titleId");
            if(parentTitle==1){
                WebActions.clickOn(ExaminerCreateTitleLocators,"titleId");
                WebActions.clickOn(ExaminerCreateTitleLocators,"titleOption");
                WebActions.performTab(1);
            }




            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"relationship");
            WebActions.clickOn(ExaminerCreateTitleLocators,"relationship");
            WebActions.clickOn(ExaminerCreateTitleLocators,"relationshipRoot");

            //description
            String expectedDescriptionValue = faker.getDescription();
            WebActions.enterTextOn(ExaminerCreateTitleLocators,"description",expectedDescriptionValue);

            //search and add document
            WebActions.clickOn(ExaminerCreateTitleLocators,"documentSearch1");
            WebActions.setWaitTime(4000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"documentClearButton");
            WebActions.setWaitTime(1000);
            WebActions.JSclickOn(ExaminerCreateTitleLocators, "documentIDTextBox");
            WebActions.enterTextOn(ExaminerCreateTitleLocators, "documentIDTextBox",
                    JSONReader.getJSONDataBlockKey("createTitle", "document", "documentId"));
            String documentID = JSONReader.getJSONDataBlockKey("createTitle", "document", "documentId");
            WebActions.setWaitTime(1000);
            WebActions.JSclickOn(ExaminerCreateTitleLocators, "documentTextBoxSearchIcon");
            WebActions.setWaitTime(4000);
            WebActions.JSclickOn(ExaminerCreateTitleLocators, "documentRefTextBox");
            WebActions.setWaitTime(2000);
            WebActions.performTabAndSpace(16);
            WebActions.clickOn(ExaminerCreateTitleLocators, "documentAddButton");
            WebActions.setWaitTime(3000);


            //add primary RRR
            WebActions.clickOn(ExaminerCreateTitleLocators,"primaryRRRDropDown");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"freeHoldRRR");
            WebActions.setWaitTime(3000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"openBelow");
            String declaredValue="345798";
            WebActions.enterTextOn(ExaminerCreateTitleLocators,"declaredValue",declaredValue);

//             select party
            WebActions.clickOn(ExaminerCreateTitleLocators,"shareParties");
            WebActions.setWaitTime(1000);
            WebActions.clickOn(ExaminerCreateTitleLocators,"partyOption");
            WebActions.performTab(1);


            //click parcel dropdown
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"newTitleParcelDropdown");
            WebActions.clickOn(ExaminerCreateTitleLocators,"newTitleParcelDropdown");

            //select parcel from dropdown
            WebActions.setWaitTime(1500);
            WebActions.clickOn(ExaminerCreateTitleLocators,"selectParcel");
            WebActions.performTab(1);


//
//            //click validation button and check for success vlidation
//            WebActions.clickOn(ExaminerCreateTitleLocators,"validationButton");
//            String actualValidationSucessMsg = WebActions.getElementText(ExaminerCreateTitleLocators, "successValidationText");
//            String expectedValidationSucessMsg = JSONReader.getJSONDataBlockKey("createTitle", "validation", "validationSucessMsg");
//            System.out.println("actualValidationSucessMsg" +actualValidationSucessMsg);
//            System.out.println("expectedValidationSucessMsg" +expectedValidationSucessMsg);
//            if(actualValidationSucessMsg ==expectedValidationSucessMsg) {
//                Utils.stepInfoLog("Matched validation message is found : \" Expected : \""+expectedValidationSucessMsg+"\" and Actual : \""+actualValidationSucessMsg+"\" are equal");
//            }else {
//                Utils.failedStepInfoLog("Mismatch validation message is found : \" Expected : \""+expectedValidationSucessMsg+"\" and Actual : \""+actualValidationSucessMsg+"\" are not equal");
//                Assert.fail("Different validation message is displayed" );
//                Assert.assertEquals("The values are not equal!", expectedValidationSucessMsg, actualValidationSucessMsg);
//
//            }
//            System.out.println("close validation button");
//            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"closeValidationButton");
//            WebActions.clickOn(ExaminerCreateTitleLocators,"closeValidationButton");

            System.out.print("titles created"+Store.getTitleIds());
            WebActions.setWaitTime(1500);
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"ApproveButton");
            WebActions.isElementClickable(ExaminerCreateTitleLocators,"ApproveButton");
            WebActions.clickOn(ExaminerCreateTitleLocators,"ApproveButton");
            WebActions.setWaitTime(1500);
            WebActions.waitForElementToVisible(ExaminerCreateTitleLocators,"approveYesButton");
            WebActions.isElementClickable(ExaminerCreateTitleLocators,"approveYesButton");
            WebActions.clickOn(ExaminerCreateTitleLocators,"approveYesButton");
            WebActions.setWaitTime(6000);
        }catch(Throwable e){
            System.out.println("issue while approving title"+e.getMessage());
            Utils.failedTestLog("There is an issue while getting titleID");
            ScreenShotCapture.importScreenToReports("Application_details");

        }
    }

    @And("I create new titles for {int} iterations")
    public void createMultipleTitles(int iterations)throws Exception {
        for (int i = 1; i <= iterations; i++) {
            System.out.println("Creating title iteration " + i);
            navigateToTheStandAloneTitleTab();
            addPartyDetailsOfNewTitle();
            addParcelDetailsOfNewTitle();
            getNewTitleID();
            enterNewTitleDetails();
        }
    }

}

