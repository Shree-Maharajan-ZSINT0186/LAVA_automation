package StepDef.Web;
import org.junit.Assert;

import Helpers.ExtentReportSetup;
import Helpers.FakerClassLibrary;
import Helpers.JSONReader;
import Helpers.ScreenShotCapture;
import Helpers.Utils;
import Helpers.WebActions;
import Helpers.YamlLoader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Enumeration;
public class CreateApplicationStepDef extends FakerClassLibrary{
	
	public String loginLocators = "LoginPageLocators";
	public String applicationLocators = "ApplicationPageLocators";
	public String titlesAndParcelsTabLocators = "TitlesAndParcelsTabLocators";
	public String
			partiesTabLocators = "PartiesTabLocators";
	public String servicesTabLocators ="ServicesTabLocators";
	static String yamlFileName = "config";
	FakerClassLibrary faker = new FakerClassLibrary();
	public Dictionary<String, String> expected_Details_Dict = new Hashtable<>();
	public Dictionary<String, String> actual_Details_Dict = new Hashtable<>();
	public String RefferenceID;
	
	
	@Given("Launch the application")
	public void launchTheApplication() throws Throwable {
	    
		try {
			//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Login To LAC");
			WebActions.launchApplication("Agent_host");

			WebActions.waitForElementToVisible(loginLocators, "loginPageTitle");
			
			//Selecting Tenant value
			WebActions.clickOn(loginLocators, "tenantDropdownField");
			WebActions.clickOn(loginLocators, "tenantSelectionTestCan"); // change this to test selenium
			WebActions.setWaitTime(6000);
			String tenantValue = WebActions.getElementText(loginLocators, "tenantValueText");
			System.out.println("tenant" +tenantValue);
			Utils.stepInfoLog("The added tenant value is: " +tenantValue);
			
			//Entering Username and Password
			String userName = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Agent","Agent_username");
			String Password = YamlLoader.getUserNameAndPasswordFromYamlBasedOnURL(yamlFileName, "Agent","Agent_password");
			WebActions.enterTextOn(loginLocators, "agentUsernameTextbox", userName);
			WebActions.enterTextOn(loginLocators, "agentPasswordTextbox", WebActions.decodeTheGivenValue(Password));
			
			//Clicking the Submit button
			WebActions.clickOn(loginLocators, "submitButton");
			WebActions.setWaitTime(4000);
			
			// Checking for the Grid or focused view 
			// getElementSize with return 0 and 1. 0 is false and 1 is true
			int loginTerminationPopup = WebActions.getElementSize(loginLocators, "logginTerminationPopup2");
			System.out.println("val:" +loginTerminationPopup);
			if(loginTerminationPopup == 1) {
				WebActions.setWaitTime(1000);
				WebActions.clickOn(loginLocators, "sessionTerminatePopup");
			}else {
				System.out.println("Login is done");
			}
			
			// Verifying whether logged in
			boolean HomeDashboardPageTitle = WebActions.isElementDisplayed(loginLocators, "homeDashboardPage");
			String HomeDashboardPageTitleText = WebActions.getElementText(loginLocators, "homeDashboardPage");
			if (HomeDashboardPageTitle) {
				 Assert.assertTrue("Dashboard page title is displayed successfully." +HomeDashboardPageTitleText, HomeDashboardPageTitle);
			} else {
				Assert.fail("Dashboard page title is NOT displayed");
			} 
			Utils.passedTestLog("Agent is logged in to the application sucessfully");
		} catch (Exception e) {
			Utils.failedTestLog("There is an issue in logging");
			ScreenShotCapture.importScreenToReports("Login");
			e.printStackTrace();
			//Assert.fail("Dashboard page title is NOT displayed");
		}
	}
	@Given("Navigate to the Application tab")
	public void navigateToTheApplicationTab() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to Application Tab");
	    try {
			WebActions.clickOn(applicationLocators, "applicationTab");
			Utils.passedTestLog("Agent Navigated to the Application Tab");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue while navigating to the Application tab");
			ScreenShotCapture.importScreenToReports("Application_tab");
			e.printStackTrace();
		}
	}

	@Given("Agent create a new application")
	public void enterApplicationDetails() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Creating New Application");
		try {
			//Clicking on the New Application button
			WebActions.clickOn(applicationLocators, "newApplicationButton");
			
			int minlength = Integer.parseInt(JSONReader.getJSONDataBlockKey("testdata", "MinAndMaxLength", "referenceNumber_MinLength"));
			int maxlength= Integer.parseInt(JSONReader.getJSONDataBlockKey("testdata", "MinAndMaxLength", "referenceNumber_MaxLength"));
	     
			//Entering value in the File reference text box
			String expectedReferenceNumber = faker.getReferenceNumber(faker.getRandomNumberBetween(minlength, maxlength));
			expected_Details_Dict.put("Reference Number", expectedReferenceNumber);
			WebActions.enterTextOn(applicationLocators, "fileReferenceTextBox", expectedReferenceNumber);
			Utils.stepInfoLog("The added ReferenceNumber value is: " +expectedReferenceNumber);
			
			
			//Entering value in the Description text box
			String expectedDescriptionValue = faker.getDescription();
			System.out.println("description from faker"+expectedDescriptionValue);
			expected_Details_Dict.put("Description", expectedDescriptionValue);
			WebActions.enterTextOn(applicationLocators, "descriptionTextBox", expectedDescriptionValue);
			Utils.stepInfoLog("The added Description is: " +expectedDescriptionValue);
		
			//Address field
			WebActions.clickOn(applicationLocators, "addressTextBoxClick");
			WebActions.setWaitTime(1000);
			String expectedAddress = faker.getStreetAddress();
			expected_Details_Dict.put("Address", expectedAddress);
			WebActions.enterTextOn(applicationLocators, "addressTextBoxClick", expectedAddress);
			Utils.stepInfoLog("The added Address value is: " +expectedAddress);
			
			//Submit
			WebActions.clickOn(applicationLocators, "applicationCreatebutton");
			WebActions.setWaitTime(3000);
			RefferenceID = WebActions.getValueAttribute(applicationLocators, "fileReferenceTextBox");
			Utils.passedTestLog("Application is created");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue while creating application");
			ScreenShotCapture.importScreenToReports("Application_details");
		} 
	}
	@Then("Application Id should be created")
	public void applicationIdShouldBeCreated() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Validation For Application ID");
		try {
			boolean applicationIDValidation = WebActions.isElementDisplayed(applicationLocators, "applicationID");
			String applicationID = WebActions.getElementText(applicationLocators, "applicationID");
			System.out.println("appId:" +applicationID);
			if (applicationIDValidation) {
				 Assert.assertTrue("Application ID is displayed successfully." +applicationID, applicationIDValidation);
			} else {
				Assert.fail("Application ID is not displayed successfully");
			} 
			Utils.stepInfoLog("The generated Application Id is:" +applicationID);
			Utils.passedTestLog("Application is created sucessfully");
			
			
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue while creating application, so Application ID is not generated");
			ScreenShotCapture.importScreenToReports("Application_ID");
		}
	}
	@Then("The added application details should be saved sucessfully")
	public void applicationDetailsCheck() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Validation for Application Details");
		try {
			System.out.println("checking the details after creating application");
			WebActions.clickOn(applicationLocators, "applicationTab");
			WebActions.enterTextOn(applicationLocators, "applicationSearchFilter", RefferenceID);
			WebActions.clickOn(applicationLocators, "applicationEditButton");
			WebActions.setWaitTime(2000);
			System.out.print("after opening the application tab");
			String actualAddress = WebActions.getValueAttribute(applicationLocators, "addressTextBoxClick");
			System.out.println("actual address"+actualAddress);
			actual_Details_Dict.put("Address", actualAddress);
			String actualDescription = WebActions.getValueAttribute(applicationLocators, "descriptionTextBoxValue");	
			actual_Details_Dict.put("Description", actualDescription);
			String ActualRefferenceNumber = WebActions.getValueAttribute(applicationLocators, "fileReferenceTextBox");
			actual_Details_Dict.put("Reference Number", ActualRefferenceNumber);
			System.out.println("actual_Details_Dict"+actual_Details_Dict);
			// Verify each key-value pair
			List<Boolean> results = new ArrayList<>();
			 
	        // Add boolean results from methods

	        Enumeration<String> keys = expected_Details_Dict.keys();
	        while (keys.hasMoreElements()) {
	            String key = keys.nextElement();
	            String expectedValue = expected_Details_Dict.get(key);
	            String actualValue = actual_Details_Dict.get(key);
	            
	            results.add(WebActions.validateStrings(expectedValue, actualValue, key));
	        }
	        for (int i = 0; i < results.size(); i++) {
	            if (results.get(i)) {
	                // If any value is false, assert fail with a message indicating which check failed.
	            	System.out.println("passed");
	            }else {
	          
	            	 Utils.failedTestLog("The added values in the create new application is not displayed");
	            	 Assert.fail("Validation failed: The application details are not displayed");
	            	
	            }
	        }
	        Utils.passedTestLog("The added values in the create new application is displayed");
		} catch (Throwable e) {
			System.out.print("details checking after creating application");
			e.printStackTrace();
			Utils.failedTestLog("The added values in the create new application is not displayed: " + e.getMessage());
			ScreenShotCapture.importScreenToReports("Application_details");
			Assert.fail("Validation failed");
		}
	    
	}
	
	@When("Agent should be navigated to the Title tab")
		public void navigateToTheTitleTab() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to the Title tab");
		try {
			WebActions.clickOn(titlesAndParcelsTabLocators, "titlesAndParcelsTab");
			Utils.passedTestLog("Agent is navigated to the Title tab");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in navigating title tab"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Title_Tab");
			e.printStackTrace();
		}
		}
	
	@And("Agent click on the Title search Icon")
	public void agentClickOnTheTitleSearchIcon() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add title pop-up");
		try {
			WebActions.clickOn(titlesAndParcelsTabLocators, "titlesSearchIcon");
			Utils.passedTestLog("Add title pop-up is displayed");
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in clicking the search icon so add title pop-up is not displayed"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Add_title_popup");
			e.printStackTrace();
		}
	    
	}
	@When("Agent add a Title")
	public void agentAddATitle() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Adding details to the Title tab");
		try {
			WebActions.clickOn(titlesAndParcelsTabLocators, "addTitlesSearchIcon");
			WebActions.setWaitTime(7000);
			WebActions.enterTextOn(titlesAndParcelsTabLocators, "titleIdTextBox", 
					JSONReader.getJSONDataBlockKey("TitleAndParcelsData", "Title", "titleID"));
			WebActions.setWaitTime(1000);
			String titleID = JSONReader.getJSONDataBlockKey("TitleAndParcelsData", "Title", "titleID");
			String actualTitleID = WebActions.getValueAttribute(titlesAndParcelsTabLocators, "titleIdTextBox");
			System.out.println("titleid:" +titleID);
			System.out.println("Acttitleid:" +actualTitleID);
			Utils.stepInfoLog("The selected Title is: " +titleID);
			Utils.stepInfoLog("The Actual Title entered is: " +actualTitleID);
			WebActions.clickOn(titlesAndParcelsTabLocators, "addTitlesSearchIcon");
			WebActions.setWaitTime(6000);
			WebActions.clickOn(titlesAndParcelsTabLocators, "ownerTextbox");
			WebActions.performTabAndSpace(14);
			WebActions.clickOn(titlesAndParcelsTabLocators, "titleAddButton");
			//WebActions.waitForElementToVisible(titlesAndParcelsTabLocators, "titleIDCheck");
			//String actualTitleID = WebActions.getValueAttribute(titlesAndParcelsTabLocators, "titleIDCheck");
			Assert.assertEquals("The values are not equal!", titleID, actualTitleID);
			
			Utils.passedTestLog("Title is added sucessfully");
			
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in adding title"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Add_Title");
			e.printStackTrace();
		}
	}
	
	@Then("Agent should be navigated to the Parties tab")
	public void navigateToPartyTab() throws Exception{
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation To the Party Tab");
		try {
			WebActions.clickOn(partiesTabLocators, "partiesTab");
			Utils.passedTestLog("Agent is navigated to the party tab");
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in navigating to Party tab"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Add_Title");
			e.printStackTrace();
		}
		}

	@When("Agent click on the Parties search Icon")
	public void agentClickOnThePartiesSearchIcon() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Add party popup");
		try {
			WebActions.clickOn(partiesTabLocators, "partiesSearchIcon");
			Utils.passedTestLog("Add parties pop-up is displayed");
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in opening add parties tab"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Party_popup");
			e.printStackTrace();
		}
	}
	@When("Agent add a Party")
	public void agentAddAParty() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Adding details to the Party pop-up");
		try {
			WebActions.clickOn(partiesTabLocators, "selectPartiesSearchIcon");
			WebActions.setWaitTime(5000);
			WebActions.enterTextOn(partiesTabLocators, "partiesNameTextbox", 
					JSONReader.getJSONDataBlockKey("PartiesData", "Party", "name"));
			String partyName = JSONReader.getJSONDataBlockKey("PartiesData", "Party", "name");
			Utils.stepInfoLog("The selected Party name is:" +partyName);
			WebActions.setWaitTime(1000);
			WebActions.clickOn(partiesTabLocators, "selectPartiesSearchIcon");
			WebActions.setWaitTime(5000);
			WebActions.clickOn(partiesTabLocators, "partyAddressTextBox");
			WebActions.performTabAndSpace(15);
			WebActions.clickOn(partiesTabLocators, "addPartiesButton");
			WebActions.setWaitTime(2000);
			Utils.passedTestLog("Party is added sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in creating parties"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Add_party");
			e.printStackTrace();
		}
	}

	@Then("Agent should be navigated to the Service tab")
	public void agentShouldBeNavigatedToTheServiceTab() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Navigation to the Service tab");
		try {
			WebActions.clickOn(servicesTabLocators, "servicesTab");
			WebActions.setWaitTime(3000);
			Utils.passedTestLog("Agent is navigated to the service tab");
		} catch (Throwable e) {
			Utils.failedTestLog("There is a issues in navigating to the Service Tab"+e.getMessage());
			ScreenShotCapture.importScreenToReports("Service_Tab");
			e.printStackTrace();
		}
	}
	
	@Then("Validation for the service should be displayed")
		public void serviceValidation() throws Throwable {
		
		try {
			WebActions.clickOn(servicesTabLocators, "addServicesButton");
			WebActions.setWaitTime(1000);
			WebActions.enterTextOn(servicesTabLocators, "searchServicesBox", 
					JSONReader.getJSONDataBlockKey("ServiceData", "Service", "serviceName"));
			String serviceName = JSONReader.getJSONDataBlockKey("ServiceData", "Service", "serviceName");
			Utils.stepInfoLog("The selected service name is: " +serviceName);
			WebActions.clickOn(servicesTabLocators, "addServiceValue");
			WebActions.setWaitTime(2000);
			WebActions.JSclickOn(servicesTabLocators, "expandTitleAndParcel");
			WebActions.JSclickOn(servicesTabLocators, "validateIcon");
			
			String expectedErrors1 = JSONReader.getJSONDataBlockKey("ServiceData", "validation", "MortgageRegValidation1");
			String expectedErrors2 = JSONReader.getJSONDataBlockKey("ServiceData", "validation", "MortgageRegValidation2");
			String expectedErrors3 = JSONReader.getJSONDataBlockKey("ServiceData", "validation", "MortgageRegValidation3");
			
			String[] expectedError = {expectedErrors1,expectedErrors2,expectedErrors3};
			List<String> expectedMortgageRegistrationErrors = new ArrayList<>();
			for (String expectedErrorValue : expectedError) {
				expectedMortgageRegistrationErrors.add(expectedErrorValue);
			}
			System.out.println("expectedMortgageRegistrationErrors" +expectedMortgageRegistrationErrors);
			String actualRRRNotSelected = WebActions.getElementText(servicesTabLocators, "validationErrorMsgOne");
			String actualRightholderNotSelected = WebActions.getElementText(servicesTabLocators, "validationErrorMsgTwo");
			String actualDocumentError = WebActions.getElementText(servicesTabLocators, "validationErrorMsgThree");
			
			String[] actualErrors = {actualRRRNotSelected,actualRightholderNotSelected,actualDocumentError};
			List<String> actualMortgageRegistrationErrors = new ArrayList<>();
			for (String actualErrorValue : actualErrors) {
				actualMortgageRegistrationErrors.add(actualErrorValue);
			}
			System.out.println("actualMortgageRegistrationErrors" +actualMortgageRegistrationErrors);
			//Assert.assertEquals(expectedMortgageRegistrationErrors.equals(actualMortgageRegistrationErrors), 
				    //"The values are not equal! Actual: " + actualMortgageRegistrationErrors + ", Expected: " + expectedMortgageRegistrationErrors);
			Assert.assertEquals("The values are not equal!", expectedMortgageRegistrationErrors, actualMortgageRegistrationErrors);
	
			Utils.stepInfoLog("The validation actual value is: " +actualMortgageRegistrationErrors);
			Utils.stepInfoLog("The validation expected value is: " +expectedMortgageRegistrationErrors);
			
			Utils.passedTestLog("Validation message are displayed correctly");
			WebActions.performEnter(1);
			WebActions.setWaitTime(1000);
			
		} catch (InterruptedException e) {
			Utils.failedTestLog("Validation messages are not equal" +e.getMessage());
			ScreenShotCapture.importScreenToReports("Validation_1");
			e.printStackTrace();
		}
	}
	
	@Then("Agent add a Service")
	public void agentAddAService() throws Exception {
		//ExtentReportSetup.test = ExtentReportSetup.createtheTest("Adding a Service");
		try {
			System.out.println("came in add service");
			WebActions.setWaitTime(3000);
			WebActions.JSclickOn(servicesTabLocators, "RRRDropDownBox");
			WebActions.setWaitTime(1000);
			WebActions.JSclickOn(servicesTabLocators, "RRRValueSelect");
			WebActions.clickOn(servicesTabLocators, "rightHolders");
			WebActions.clickOn(servicesTabLocators, "rightHoldersValue");
			WebActions.setWaitTime(1000);
			WebActions.performTabAndSpace(1);
			//WebActions.performESCFunction(1);
			WebActions.setWaitTime(2000);
			System.out.println("date place came");
			WebActions.JSclickOn(servicesTabLocators, "effectiveDate");
			WebActions.setWaitTime(1000);
			WebActions.performEnter(1);
			WebActions.JSclickOn(servicesTabLocators, "expiryData");
			WebActions.setWaitTime(1000);
			WebActions.performEnter(1);
			WebActions.clickOn(servicesTabLocators, "typeBox");
			WebActions.enterTextOn(servicesTabLocators, "filterBox",
					JSONReader.getJSONDataBlockKey("ServiceData", "Service", "typeValue"));
			WebActions.clickOn(servicesTabLocators, "typeValue");
			WebActions.performESCFunction(1);
			String TypeValue = JSONReader.getJSONDataBlockKey("ServiceData", "Service", "typeValue");
			Utils.stepInfoLog("The selected type value is: " +TypeValue);
			WebActions.clickOn(servicesTabLocators, "amountField");
			WebActions.enterTextOn(servicesTabLocators, "amountField",
					JSONReader.getJSONDataBlockKey("ServiceData", "Service", "amountValue"));
			String amountValue = JSONReader.getJSONDataBlockKey("ServiceData", "Service", "amountValue");
			Utils.stepInfoLog("The selected amount value is: " +amountValue);
			WebActions.setWaitTime(1000);
			WebActions.clickOn(servicesTabLocators, "documentSearchIcon");
			WebActions.setWaitTime(4000);
			WebActions.JSclickOn(servicesTabLocators, "documentIDTextBox");
			WebActions.enterTextOn(servicesTabLocators, "documentIDTextBox", 
					JSONReader.getJSONDataBlockKey("ServiceData", "Service", "documentId"));
			String documentID = JSONReader.getJSONDataBlockKey("ServiceData", "Service", "documentId");
			Utils.stepInfoLog("The selected documentId is: " +documentID);
			WebActions.setWaitTime(1000);
			WebActions.JSclickOn(servicesTabLocators, "documentTextBoxSearchIcon");
			WebActions.setWaitTime(4000);
			WebActions.JSclickOn(servicesTabLocators, "documentRefTextBox");
			WebActions.performTabAndSpace(16);
			WebActions.clickOn(servicesTabLocators, "documentAddButton");
			WebActions.setWaitTime(3000);
    		WebActions.JSclickOn(servicesTabLocators, "validateButtom");
			WebActions.setWaitTime(4000);
			WebActions.performEnter(1);
			WebActions.setWaitTime(1000);
			WebActions.clickOn(servicesTabLocators, "lodgeButton");
			WebActions.setWaitTime(2000);
			
			WebActions.setWaitTime(3000);
			WebActions.JSclickOn(servicesTabLocators, "expandTitleAndParcel");
			WebActions.JSclickOn(servicesTabLocators, "validateButtom");
			Utils.stepInfoLog("Application is Lodged sucessfully");
			WebActions.setWaitTime(3000);
			String actualValidationSucessMsg = WebActions.getElementText(servicesTabLocators, "validationSucessMsg");
			String expectedValidationSucessMsg = JSONReader.getJSONDataBlockKey("ServiceData", "validation", "validationSucessMsg");
			System.out.println("actualValidationSucessMsg" +actualValidationSucessMsg);
			System.out.println("expectedValidationSucessMsg" +expectedValidationSucessMsg);
			if(actualValidationSucessMsg ==expectedValidationSucessMsg) {
				Utils.stepInfoLog("Matched validation message is found : \" Expected : \""+expectedValidationSucessMsg+"\" and Actual : \""+actualValidationSucessMsg+"\" are equal");
			}else {
			Utils.failedStepInfoLog("Mismatch validation message is found : \" Expected : \""+expectedValidationSucessMsg+"\" and Actual : \""+actualValidationSucessMsg+"\" are not equal");
			Assert.fail("Different validation message is displayed" );
			Assert.assertEquals("The values are not equal!", expectedValidationSucessMsg, actualValidationSucessMsg);
			
			}
			WebActions.performEnter(1);
//			WebActions.JSclickOn(servicesTabLocators, "valPopupClose");
			Utils.passedTestLog("Service is added sucessfully");
		} catch (Throwable e) {
			Utils.failedTestLog("There is an issue in adding a service: " + " " + e.getMessage());
			ScreenShotCapture.importScreenToReports("Add_service");			
			Assert.fail("Different validation message is displayed: " + " " + e.getMessage());	
			e.printStackTrace();
		}
	}










}