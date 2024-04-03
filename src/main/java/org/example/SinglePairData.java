package org.example;

import static org.example.CallApi.*;
import static org.example.ConvertToJson.conerttoJson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class SinglePairData
{
    public static ArrayList<String> get24hrSinglePairData(String coin) throws Exception {
        HashMap<String, String> parameters = new HashMap<>();
        HashMap<String, Object> payload = new HashMap<>();
        String path = "?exchange=" + URLEncoder.encode("coinswitchx", "UTF-8")
                + "&symbol=" + URLEncoder.encode(coin, "UTF-8");
        path = path.replaceAll("%2F", "/");
        path = path.replaceAll("%2C", ",");

        String response = makeRequest("GET","/trade/api/v2/24hr/ticker"+path, payload, parameters);

        return conerttoJson(response);
    }
}
