package kr.blue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InfoAPI {
    //Get API information from api.properties
    private String client_id;
    private String client_secret;

    public InfoAPI() {
        String file = "api.properties";
        Properties properties = new Properties();
        InputStream inputStream = InfoAPI.class.getResourceAsStream(file);
        try {
            properties.load(inputStream);
            client_id = properties.getProperty("id");
            client_secret = properties.getProperty("secret");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getClient_id() {
        return client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }
}
