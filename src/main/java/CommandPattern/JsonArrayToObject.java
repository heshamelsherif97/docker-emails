package CommandPattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonArrayToObject {
    String message;
    JSONArray jsonresult;
    public JsonArrayToObject(String message, JSONArray array){
        this.message = message;
        this.jsonresult = array;
    }

    public JSONObject convertArray(){
        String out = "{";
        for(int i=0; i<jsonresult.length();i++){
            int j = i+1;
            out += "\""+j+"\":"+jsonresult.getJSONObject(i);
            if(i < jsonresult.length()-1){
                out += ",";
            }
        }
        out += "}";

        if (out.equals("{}")){
            out = "{\"message\":\""+this.message+"\"}";
        }
        return new JSONObject(out);
    }
}
