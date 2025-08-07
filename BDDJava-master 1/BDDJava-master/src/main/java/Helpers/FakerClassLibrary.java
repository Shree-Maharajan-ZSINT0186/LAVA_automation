package Helpers;

import com.github.javafaker.Faker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FakerClassLibrary{

    Faker faker = new Faker();
    static String userDirectory = System.getProperty("user.dir");

    public String getReferenceNumber(int length)
    {
        String refNumber = faker.number().digits(length);
        refNumber = "Aut_REF-"+refNumber;
        return refNumber;
    }

    public String getFirstName()
    {
        return faker.name().firstName();
    }

    public String getLastName()
    {
        return faker.name().lastName();
    }

    public String getPhoneNumber()
    {
        return faker.phoneNumber().cellPhone().strip();
    }

    public String getStreetAddress()
    {
        return faker.address().fullAddress();
    }

    public int getRandomNumberBetween(int minLength, int maxLength)
    {
        return faker.number().numberBetween(minLength, maxLength);
    }

    public long getRandomNumberBetween(long minLength, long maxLength)
    {
        return faker.number().numberBetween(minLength, maxLength);
    }
    
    public String getCurrentDate()
    {
        LocalDate currentDate = LocalDate.now();
        return currentDate.toString();
    }

    /*public String getFutureDate()
    {
        return faker.date().future(10, TimeUnit.DAYS).toString("DD/MM/YY");
    }*/

    public String getCityName()
    {
        return faker.address().city();
    }

    public String getStateName()
    {
        return faker.address().state();
    }
    
    public String getSampleText()
    {
        return faker.yoda().quote();
    }
    
    public String getDescription()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\testdata.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("Descriptions");
 
        // Convert JSONArray to List<String>
        List<String> descriptionList = new ArrayList<>();
        for (Object obj : jsonArray) {
            descriptionList.add((String) obj);
        }
        return descriptionList.get(faker.number().numberBetween(0, 5));
    }
    public String getAmount()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("amount");

        // Convert JSONArray to List<String>
        List<String> amountList = new ArrayList<>();
        for (Object obj : jsonArray) {
            amountList.add((String) obj);
        }
        return amountList.get(faker.number().numberBetween(0, 5));
    }
    public String getConditionName()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("conditionName");

        // Convert JSONArray to List<String>
        List<String> conditionNameList = new ArrayList<>();
        for (Object obj : jsonArray) {
            conditionNameList.add((String) obj);
        }
        return conditionNameList.get(faker.number().numberBetween(0, 2));
    }
    public String getConditionDescription()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("conditionDescription");

        // Convert JSONArray to List<String>
        List<String> conditionDescriptionList = new ArrayList<>();
        for (Object obj : jsonArray) {
            conditionDescriptionList.add((String) obj);
        }
        return conditionDescriptionList.get(faker.number().numberBetween(0, 2));
    }
    public String getMortgageDocument()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("mortgageDocument");

        // Convert JSONArray to List<String>
        List<String> mortgageDocumentList = new ArrayList<>();
        for (Object obj : jsonArray) {
            mortgageDocumentList.add((String) obj);
        }
        return mortgageDocumentList.get(faker.number().numberBetween(0, 2));
    }
    public String getLeaseDocument()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("leaseDocument");

        // Convert JSONArray to List<String>
        List<String> leaseDocumentList = new ArrayList<>();
        for (Object obj : jsonArray) {
            leaseDocumentList.add((String) obj);
        }
        return leaseDocumentList.get(faker.number().numberBetween(0, 2));
    }
    public String getTransferOfOwnershipDocument()
    {
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject(userDirectory+"\\src\\test\\resources\\testDataJSON\\Web\\ServiceData.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("transferOfOwnershipDocument");

        // Convert JSONArray to List<String>
        List<String> transferOfOwnershipDocumentList = new ArrayList<>();
        for (Object obj : jsonArray) {
            transferOfOwnershipDocumentList.add((String) obj);
        }
        return transferOfOwnershipDocumentList.get(faker.number().numberBetween(0, 2));
    }

 
    public String getEmailAddress()
    {
        return faker.internet().emailAddress();
    }
 

}