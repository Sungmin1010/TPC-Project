import kr.blue.AddressVO;
import kr.blue.InfoAPI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

public class NaverMap implements ActionListener {

    Project01_F gui ;
    InfoAPI api;

    public NaverMap(Project01_F gui) {
        this.gui = gui;
        this.api = new InfoAPI();
    }
    public void mapImage_service(AddressVO vo){
        String URL_STATICMAP = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
        try{
            String pos = URLEncoder.encode(vo.getX()+" "+vo.getY(), "UTF-8");
            URL_STATICMAP += "center=" + vo.getX() + "," + vo.getY();
            URL_STATICMAP += "&level=16&w=700&h=500";
            URL_STATICMAP += "&markers=type:t|size:mid|pos:"+pos+"|label:"+URLEncoder.encode(vo.getRoadAddress(), "UTF-8");
            URL url = new URL(URL_STATICMAP);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", api.getClient_id());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", api.getClient_secret());
            int code = con.getResponseCode();
            if(code == 200){
                InputStream inputStream = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File(tempname+".jpg");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while(  (read = inputStream.read(bytes)) != -1  ){
                    outputStream.write(bytes, 0, read);
                }
                inputStream.close();
                outputStream.close();

                ImageIcon img = new ImageIcon(f.getName());
                gui.imageLabel.setIcon(img);
                gui.resAddress.setText(vo.getRoadAddress());
                gui.jibunAddress.setText(vo.getJibunAddress());
                gui.resX.setText(vo.getX());
                gui.resY.setText(vo.getY());

            }else{
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while( (line = br.readLine()) != null){
                    sb.append(line);
                }
                JSONTokener tokener = new JSONTokener(sb.toString());
                JSONObject jsonObject = new JSONObject(tokener);
                JSONObject errorObject = (JSONObject)jsonObject.get("error");
                String errorCode = (String)errorObject.get("errorCode");
                System.out.println("errorCode : "+errorCode);
                System.out.println("code : " + code);
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        AddressVO vo = null;
        try{
            String address = gui.address.getText();
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", api.getClient_id());
            con.setRequestProperty("X-NCP-APIGW-API-KEY", api.getClient_secret());
            int responseCode = con.getResponseCode();
            BufferedReader br ;
            if(responseCode == 200){
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }else{
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String line;
            StringBuffer response = new StringBuffer();
            while(  (line = br.readLine()) != null ){
                response.append(line);
            }
            br.close();
            con.disconnect();

            JSONTokener tokener = new JSONTokener(response.toString());
            JSONObject object = new JSONObject(tokener);
            System.out.println(object.toString());
            JSONArray addressArray = object.getJSONArray("addresses");
            for(int i= 0; i<addressArray.length(); i++){
                JSONObject addressObject = (JSONObject) addressArray.get(i);
                vo = new AddressVO();
                vo.setJibunAddress((String) addressObject.get("jibunAddress"));
                vo.setRoadAddress((String) addressObject.get("roadAddress"));
                vo.setX((String) addressObject.get("x"));
                vo.setY((String) addressObject.get("y"));
            }

            mapImage_service(vo);



        }catch(Exception err){
            System.out.println(err);
        }

    }
}
