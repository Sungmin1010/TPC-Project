import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;

public class Project01_C {
    public static void main(String[] args) {
        String src = "info.json";
        InputStream is = Project01_C.class.getResourceAsStream(src); //클래스랑 동일한 경로에서 src를 찾는다
        if(is==null){
            throw new NullPointerException("Cannot find resource file");
        }
        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        JSONArray students = object.getJSONArray("students");
        for(int i=0; i<students.length(); i++){
            JSONObject student  = (JSONObject)students.get(i);
            System.out.print(student.get("name")+ "\t");
            System.out.print(student.get("phone") + "\t");
            System.out.println(student.get("address") + "\t");
        }

    }
}
