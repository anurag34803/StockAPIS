package Depth;

import static org.example.CallApi.*;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.ArrayList;

import static Depth.ConversionToJson.conerttoJson;
public class DepthOfCoin {

    public static  ArrayList<String> getDepthOfCoin(String coin) throws Exception {
        HashMap<String, String> parameters = new HashMap<>();
        HashMap<String, Object> payload = new HashMap<>();
        String path = "?exchange=" + URLEncoder.encode("coinswitchx", "UTF-8")
                + "&symbol=" + URLEncoder.encode(coin, "UTF-8");
        path = path.replaceAll("%2F", "/");
        String response = makeRequest("GET","/trade/api/v2/depth" +path, payload, parameters);

        return conerttoJson(response);
    }
}
