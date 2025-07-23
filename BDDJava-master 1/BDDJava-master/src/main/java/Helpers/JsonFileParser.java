package Helpers;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class JsonFileParser {

        public static JSONObject getJsonFileObject(String filename)
        {
            JSONObject jo = new JSONObject();
            try
            {
                Object jsonobj = new JSONParser().parse(new FileReader(filename)); 
                jo = (JSONObject) jsonobj;
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return jo;
        }
}
