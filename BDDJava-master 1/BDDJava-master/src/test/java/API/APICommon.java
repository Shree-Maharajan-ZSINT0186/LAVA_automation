package API;

import java.io.File;

import org.openqa.selenium.json.JsonException;

import Helpers.JSONReader;
import StepDef.API.API;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class APICommon {
	public static Response response = null;
	//public RequestSpecification request = RestAssured.given();
	public  RequestSpecification request = RestAssured.given();
		
	/**
	 * Method Description: This method is used to Post request with Query Parameter
	 * 
	 * @param baseUrl,endPoint,jsonBody,queryParamKey,queryParamValue
	 * @return
	 * @throws Throwable
	 */
	public RequestSpecification postRequestWithQueryparam(String baseUrl, String endPoint, String jsonBody,
			String[] queryParamKey, String[] queryParamValue) {
		try {
			for (int i = 0; i < queryParamKey.length; i++) {
				request = request.queryParams(queryParamKey[i], queryParamValue[i]);
				if (jsonBody != null) {
					request = request.body(jsonBody);
				}
			}
			response = request.when().post(baseUrl + endPoint);
			return request;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return request;
	}

	/**
	 * Method Description: This method is used to Post request with Query Parameter and Form
	 * 
	 * @param baseUrl,endPoint,jsonBody,queryParamKey,queryParamValue,formDatType,formKey,formValue
	 * @return
	 * @throws Throwable
	 */
	public Response postRequestWithFormAndQuery(String baseUrl, String endPoint, String jsonBody, String[] formDatType,
			String[] formKey, String[] formValue, String[] queryParamKey, String[] queryParamValue) {
		try {
			if (jsonBody != null) {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and()
						.body(jsonBody);
			} else if (formDatType.length > 1) {
				request = request.contentType("multipart/form-data");
			} else {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and();
			}

			for (int i = 0; i < formDatType.length; i++) {
				switch (formDatType[i].toLowerCase()) {
				case "text":
					request = request.multiPart(formKey[i], "filename");
					break;
				case "file":
					request = request.multiPart(formKey[i], new File(formValue[i]));
					break;
				}
			}

			if (queryParamKey.length > 1) {
				for (int i = 0; i < queryParamKey.length; i++) {
					request = request.queryParam(queryParamKey[i], queryParamValue[i]);
				}
			}

			response = request.when().post(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}

	/**
	 * Method Description: This method is used to Post request
	 * 
	 * @param baseUrl,endPoint,jsonBody
	 * @return
	 * @throws Throwable
	 */
	public Response postRequest(String baseUrl, String endPoint, String jsonBody) {
		try {
			if (jsonBody != null) {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and()
						.body(jsonBody);
			} else {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and();
			}
			response = request.when().post(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}
	
	public Response postRequestWithAuth(String baseUrl, String endPoint, String jsonBody, String token) {
		try {
			if (jsonBody != null) {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).header("Authorization", "Bearer " + token).and()
						.body(jsonBody);
			} else {
				request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and();
			}
			response = request.when().post(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}

	/**
	 * Method Description: This method is used to Post request with Form Data
	 * 
	 * @param baseUrl,endPoint,jsonBody,formDataType,formKey,formValue
	 * @return
	 * @throws Throwable
	 */
	public Response postRequestWithFormData(String baseUrl, String endPoint, String jsonBody, String[] formDataType,
			String[] formKey, String[] formValue) throws JsonException {
		try {
			RestAssured.baseURI = baseUrl;
			request = request.header("Content-type", "multipart/form-data");
			for (int i = 0; i < formDataType.length; i++) {
				switch (formDataType[i].toLowerCase()) {
				case "text":
					request = request.formParam(formKey[i], formValue[i]);
					break;
				case "file":
					request = request.multiPart(formKey[i], new File(formValue[i]));
					break;
				}
			}
			if (jsonBody != null) {
				request = request.and().body(jsonBody);
			}
			response = request.when().post(endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}

	/**
	 * Method Description: This method is used to Put request
	 * 
	 * @param baseUrl,endPoint,jsonBody
	 * @return
	 * @throws Throwable
	 */
	public Response putRequest(String baseUrl, String endPoint, String jsonBody) {
		try {
			response = request.header("Content-Type", "application/json").accept(ContentType.JSON).and().body(jsonBody)
					.when().put(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}

	
	/**
	 * Method Description: This method is used to Get request with Form Data
	 * 
	 * @param baseUrl,endPoint
	 * @return
	 * @throws Throwable
	 */
	public Response getRequest(String baseUrl, String endPoint) {
		try {
			response = request.header("Content-Type", "application/json").accept(ContentType.JSON).and().when()
					.get(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}

	/**
	 * Method Description: This method is used to Delete request with Form Data
	 * 
	 * @param baseUrl,endPoint,jsonBody
	 * @return
	 * @throws Throwable
	 */
	public Response deleteRequest(String baseUrl, String endPoint, String jsonBody) {
		try {
			request = request.header("Content-Type", "application/json").accept(ContentType.JSON).and();

			if (jsonBody != null) {
				request = request.and().body(jsonBody);
			}
			response = request.when().delete(baseUrl + endPoint);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			;
		}
		return response;
	}
	
	/**
	 * Method Description: This method is used to getBaseUrl
	 * 
	 * @return
	 * @throws Throwable
	 */
	public String getBaseUrl() throws Throwable {
		String baseURL = null;
		try {
			baseURL = JSONReader.getEndPointsFromJSONFile("endPoints", "baseUrl").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseURL;
	}

	/**
	 * Method Description: This method is used to update {Param} in end points
	 * 
	 * @param paramValues,endPoint
	 * @return
	 * @throws Throwable
	 */
	public String updateParamInRequest(String paramValues, String endPoint) {
		String endPointFinal = null;
		try {
			endPointFinal = endPoint.replace("{Param}", paramValues);
			return endPointFinal;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return endPointFinal;
	}

}
