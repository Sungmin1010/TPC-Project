import com.google.gson.JsonObject;
import kr.blue.InfoAPI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Project01_D2 {
    //첫번째 실습을 활용한 개인 예제 - 주소를 검색하면 우편번호를 알려주는 클래스
    public static void main(String[] args) {
        String result = "";
        //api 인증을 위한 값 가져오기
        InfoAPI info = new InfoAPI();
        String url = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?";

        //주소 입력 받기
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("주소를 입력 하세요 : ");
        try {
            String query = bufferedReader.readLine();
            String queryEncoded = URLEncoder.encode(query, "UTF-8");
            URL request = new URL(url+"query="+queryEncoded);
            HttpURLConnection con = (HttpURLConnection)request.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", info.getClient_id());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", info.getClient_secret());
            int code = con.getResponseCode();
            BufferedReader br ;
            if(code == 200){
               br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else{
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String line = "";
            StringBuilder sb = new StringBuilder();
            while( (line = br.readLine()) != null ){
                sb.append(line);
            }
            br.close();

            JSONTokener tokener = new JSONTokener(sb.toString());
            JSONObject object = new JSONObject(tokener);

            JSONArray addressArray = object.getJSONArray("addresses");
            for(int i=0; i<addressArray.length(); i++){
                JSONObject temp = (JSONObject) addressArray.get(i);
                JSONArray tempArray = (JSONArray) temp.get("addressElements");
                for(int j=0; j<tempArray.length(); j++){
                    JSONObject elementObject = (JSONObject) tempArray.get(j);
                    JSONArray typeArray = (JSONArray) elementObject.get("types");
                    String typeValue = (String)typeArray.get(0);
                    if(typeValue.equals("POSTAL_CODE")){
                        //System.out.println((String)elementObject.get("longName"));
                        result = (String) elementObject.get("longName");
                    }
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("우편번호는 "+ result + "입니다.");

    }
}
