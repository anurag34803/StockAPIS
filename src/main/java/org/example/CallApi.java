package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.encoders.Hex;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CallApi {
    public static String makeRequest(String method, String endpoint, HashMap<String, Object> payload, HashMap<String, String> params) throws Exception {
        String secretKey = "fc2e57281210f06a8bbc40f09063b2ac354a8a2c643033e00ce297ea0918db17" ; // provided by coinswitch

        String apiKey =  "3d75b6abb90dda737da9a779dcb8dae393b8dae7240bdc7933ecbd3ebf10ae6b";  // provided by coinswitch

        String decodedEndpoint = endpoint;
        if (method.equals("GET") && !params.isEmpty()) {
            String query = new URI(endpoint).getQuery();
            endpoint += (query == null || query.isEmpty()) ? "?" : "&";
            endpoint += URLEncoder.encode(paramsToString(params), "UTF-8");
            decodedEndpoint = URLDecoder.decode(endpoint, "UTF-8");
        }

        String signatureMsg = signatureMessage(method, decodedEndpoint, payload);
        String signature = getSignatureOfRequest(secretKey, signatureMsg);

        Map<String, String> headers = new HashMap<>();
        String url = "https://coinswitch.co" + endpoint;

        headers.put("X-AUTH-SIGNATURE", signature);
        headers.put("X-AUTH-APIKEY", apiKey);
        if (method.equals("GET")){
            HttpResponse<String> response = callGetApi(url, headers, payload);
            //System.out.println(response.entrySet());
            return response.body().toString();
        }

        return "SUCCESS";
    }

    private static String paramsToString(Map<String, String> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    private static String signatureMessage(String method, String endpoint, HashMap<String, Object> payload) throws Exception {
        TreeMap<String, Object> treeMap = new TreeMap<>(payload);
        String sortedPayloadJson = new ObjectMapper().writeValueAsString(treeMap);
        return method + endpoint + sortedPayloadJson;
    }

    public static String getSignatureOfRequest(String secretKey, String requestString) {
        byte[] requestBytes = requestString.getBytes(StandardCharsets.UTF_8);
        byte[] secretKeyBytes = Hex.decode(secretKey);

        // Generate private key
        Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(secretKeyBytes, 0);

        // Sign the request
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(requestBytes, 0, requestBytes.length);
        byte[] signatureBytes = signer.generateSignature();

        String signatureHex = Hex.toHexString(signatureBytes);
        return signatureHex;
    }
    private static HttpResponse<String> callGetApi(String url, Map<String, String> headers, HashMap<String, Object> payload) throws Exception {
        HttpClient client = HttpClient.newBuilder().build();

        HttpRequest.Builder requestBuilder;

        requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(new URI(url));


        for (Map.Entry<String, String> entry : headers.entrySet()) {
            requestBuilder.header(entry.getKey(), entry.getValue());
        }

        HttpRequest request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
