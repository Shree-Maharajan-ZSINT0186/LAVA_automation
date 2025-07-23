package StepDef.API;

import java.io.File;

import org.json.simple.JSONObject;
import org.junit.Assert;

import API.APICommon;
import Helpers.JSONReader;
import Helpers.Utils;
import io.cucumber.java.en.*;
import io.restassured.response.Response;

public class API {
	
	public API() {
	}
	
	static JSONReader jsonreader = new JSONReader();
	JSONObject jsonBody=null;
	String endPoint = null;
	String baseURL  = null;
	APICommon api = new APICommon();
	public String orderId = null;
			
	
	
	@Given("^I want to fetch the payload \"([^\"]*)\"$")
	public void I_want_to_fetch_the_payload(String objName) throws Throwable {
		 baseURL = api.getBaseUrl();
		jsonBody = JSONReader.getJSONPlayLoad(objName);
		Utils.passedTestLog("Fetch the Payload");
	}
	
	@When("^Get the end points \"([^\"]*)\"$")
	public void Get_the_end_points(String objName) throws Throwable {
		endPoint = JSONReader.getEndPointsFromJSONFile("endPoints", objName);
		Utils.passedTestLog("Get the End Points");
	}
	
	@Then("^Upload the images$")
	public void Upload_the_images() throws Throwable {
		File f1 = new File(jsonBody.get("file").toString());
		String path =System.getProperty("user.dir")+f1;
		String[] formType= {"text","file"};
		String[] formKey= {"additionalMetadata","file"};
		String[] formValue= {jsonBody.get("additionalMetadata").toString(),path};
		endPoint = api.updateParamInRequest(jsonBody.get("petId").toString(),endPoint);
		Response res = api.postRequestWithFormData(baseURL,endPoint,null,formType,formKey,formValue);
		 Assert.assertEquals(res.statusCode(), 200);
		Utils.passedTestLog("Upload the Images");
	}

	@When("^Create new pet store$")
	public void Create_new_pet_store() throws Throwable {
		Response res = api.postRequest(baseURL, endPoint, jsonBody.toString());
		System.out.println(res.statusCode());
		 Assert.assertEquals(res.statusCode(), 200);
		Utils.passedTestLog("Create new Pet Store");
	}
	
	@When("^Get Pet ID$")
	public void Get_Pet_ID() throws Throwable {
		endPoint = api.updateParamInRequest(jsonBody.get("petId").toString(),endPoint);
		Response res = api.getRequest(baseURL, endPoint);
		 Assert.assertEquals(res.statusCode(), 200);
		Utils.passedTestLog("Launch the application Successfully");
	}
	
	@When("^Delete Pet ID$")
	public void Delete_Pet_ID() throws Throwable {
		endPoint = api.updateParamInRequest(jsonBody.get("petId").toString(),endPoint);
		Response res = api.deleteRequest(baseURL, endPoint,null);
		 Assert.assertEquals(res.statusCode(), 200);
		Utils.passedTestLog("Launch the application Successfully");
	}
	
	@When("^Get the end points for \"([^\"]*)\"$")
	public void Get_the_end_points_for(String objName) throws Throwable {
		endPoint = JSONReader.getEndPointsFromJSONFile("endPoints", objName);
		Utils.passedTestLog("Get the End Points");
	}
	
	@When("^Get user ID$")
	public void Get_User_ID() throws Throwable {
		endPoint = api.updateParamInRequest(jsonBody.get("BookId").toString(),endPoint);
		Response res = api.getRequest(baseURL, endPoint);
		System.out.println(res.statusCode());
		 Assert.assertEquals(res.statusCode(), 200);
		Utils.passedTestLog("Launch the application Successfully");
	}
	
	@Then("^Create a new order$")
	public void Create_a_new_order() throws Throwable {
		Response res = api.postRequestWithAuth(baseURL, endPoint, jsonBody.toString(),"2fad838d2e26504ceab3879ec28fd4508cfd783eee8351d8b515668d3c4d19a6");
		orderId = res.path("orderId");
		System.out.println(orderId);
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 201);
		Utils.passedTestLog("Create new Pet Store");
	}
	
	@Then("^Edit The Order$")
	public void Edit_The_Order() throws Throwable {
		endPoint = api.updateParamInRequest(jsonBody.get("orderID").toString(),endPoint);
		Response res = api.postRequestWithAuth(baseURL, endPoint, jsonBody.toString(),"2fad838d2e26504ceab3879ec28fd4508cfd783eee8351d8b515668d3c4d19a6");
		//orderId = res.path("orderId");
		//System.out.println(orderId);
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(), 201);
		Utils.passedTestLog("Create new Pet Store");
	}
}


