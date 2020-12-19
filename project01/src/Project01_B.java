import org.json.JSONArray;
import org.json.JSONObject;

public class Project01_B {
    public static void main(String[] args) {
        //Json-java 이용 (org.json)
        JSONArray students = new JSONArray();

        JSONObject student = new JSONObject();
        student.put("name", "홍길동");
        student.put("phone", "010-1111-1111");
        student.put("address", "서울시");
        System.out.println(student);
        students.put(student);

        student = new JSONObject();
        student.put("name", "둘리");
        student.put("phone", "010-2222-1111");
        student.put("address", "광주");
        students.put(student);

        //최종적으로 감쌀 JSONObject
        JSONObject object = new JSONObject();
        object.put("students", students);

        System.out.println(object);
        System.out.println(object.toString(2));


    }
}
