import org.json.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

public class Project01_E {

    static String client_id;
    static String client_secret;

    public static void map_service(String point_x, String point_y, String address){
        String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
        try {
            String pos = URLEncoder.encode(point_x+" "+point_y, "UTF-8");//maker가 표시될 위치
            String url = URL_STATICMAP;
            url += "center="+point_x+","+point_y;
            url += "&level=16&w=700&h=500";
            url += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(address, "UTF-8");

            URL u = new URL(url);
            HttpURLConnection con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
            int responseCode = con.getResponseCode();
            BufferedReader br ;
            if(responseCode == 200){
                InputStream inputStream = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File(tempname+".jpg");

                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);

                while( (read = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes, 0, read);
                }
                inputStream.close();
            }else{
                //에러발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while( (inputLine = br.readLine()) != null){
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        String apiUrl = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
        String file = "api.properties";
        Properties properties = new Properties();
        InputStream inputStream = Project01_D.class.getResourceAsStream(file);

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
            io.close();

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

            //지도 사진 요청을 위한 변수 선언
            String x = ""; String y = ""; String z = "";

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
                x = (String) temp.get("x");
                y = (String) temp.get("y");
                z = (String) temp.get("roadAddress");
            }

            map_service(x, y, z);


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
