import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class Project01_D {
    public static void main(String[] args) {
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
        String file = "api.properties";
        Properties properties = new Properties();
        InputStream inputStream = Project01_D.class.getResourceAsStream(file);
        String client_id = "";
        String client_secret = "";
        try{
            properties.load(inputStream);
            client_id = properties.getProperty("id");
            client_secret = properties.getProperty("secret");
            inputStream.close();
        }catch(IOException e){
            e.printStackTrace();
        }

        BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
        try{
            System.out.print("주소를 입력하세요 : ");
            String address = io.readLine();
            String encodeAddress = URLEncoder.encode(address, "UTF-8");
            String requestUrl = apiUrl + encodeAddress;

            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            }else{
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String line;
            StringBuilder response = new StringBuilder();
            while( (line=br.readLine()) != null){
                response.append(line);
            }
            br.close();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject jsonObject = new JSONObject(tokener);
            System.out.println(jsonObject.toString());

            JSONArray addressArray = jsonObject.getJSONArray("addresses");
            for(int i=0; i<addressArray.length(); i++){
                JSONObject temp = (JSONObject) addressArray.get(i);
                System.out.println("address : " + temp.get("roadAddress"));
                System.out.println("jibunAddress : " + temp.get("jibunAddress"));
                System.out.println("경도 : " + temp.get("x"));
                System.out.println("위도 : " + temp.get("y"));
            }


        }catch(Exception e){
            e.printStackTrace();
        }


    }


}
