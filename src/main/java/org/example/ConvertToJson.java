package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.example.TimeFormat.timeFormatter;

public class ConvertToJson {

    private static double roundToTwoDecimalPlaces(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static ArrayList<String> conerttoJson(String s)
    {
        ArrayList<String> coindata = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Parse JSON string
            JsonNode jsonNode = mapper.readTree(s).get("data").get("coinswitchx");
            //System.out.println(jsonNode);

            //inserting data into coindata
            String coinName = jsonNode.get("symbol").asText();
            coinName = coinName.replace("/INR","");

            coindata.add(coinName);
            coindata.add(jsonNode.get("openPrice").asText());
            coindata.add(jsonNode.get("lowPrice").asText());
            coindata.add(jsonNode.get("highPrice").asText());

            // Suppose jsonNode.get("lastPrice").asText() returns the last price as a string
            String lastPriceString = jsonNode.get("lastPrice").asText();
            // Convert the string to a double
            double lastPrice = Double.parseDouble(lastPriceString);
            // Round the double to two decimal places
            double roundedLastPrice = roundToTwoDecimalPlaces(lastPrice);

            // Convert the rounded double back to a string and add it to the list
            coindata.add(String.valueOf(roundedLastPrice));
            coindata.add(jsonNode.get("baseVolume").asText());
            coindata.add(jsonNode.get("quoteVolume").asText());
            coindata.add(timeFormatter(jsonNode.get("at").asText()));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return coindata;
    }
}
