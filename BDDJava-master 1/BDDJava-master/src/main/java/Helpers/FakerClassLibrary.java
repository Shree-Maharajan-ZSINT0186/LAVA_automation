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
        JSONObject jo = (JSONObject) JsonFileParser.getJsonFileObject("D:\\LAVA_automation\\BDDJava-master 1\\BDDJava-master\\src\\test\\resources\\testDataJSON\\Web\\testdata.json");
        // Extract JSON array
        JSONArray jsonArray = (JSONArray) jo.get("Descriptions");
 
        // Convert JSONArray to List<String>
        List<String> descriptionList = new ArrayList<>();
        for (Object obj : jsonArray) {
            descriptionList.add((String) obj);
        }
        return descriptionList.get(faker.number().numberBetween(0, 5));
    }
 
public String getEmailAddress()
    {
        return faker.internet().emailAddress();
    }
 

}