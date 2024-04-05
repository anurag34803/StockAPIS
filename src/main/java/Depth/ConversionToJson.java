package Depth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static org.example.TimeFormat.timeFormatter;

public class ConversionToJson {
        public static ArrayList<String> conerttoJson(String s)
        {
            ArrayList<String> Depthdata = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            try {
                // Parse JSON string
                JsonNode jsonNode = mapper.readTree(s).get("data");
                //System.out.println(jsonNode);

                //inserting data into coindata
                String coinName = jsonNode.get("symbol").asText();
                coinName = coinName.replace("/INR","");

                Depthdata.add(coinName);

                Depthdata.add(timeFormatter(jsonNode.get("timestamp").asText()));

                StringBuilder bids = new StringBuilder();
                double Totalbids = 0;
                for(int i=0;i<jsonNode.get("bids").size();i++) {
                    Totalbids += jsonNode.get("bids").get(i).get(0).asDouble();
                    bids.append(jsonNode.get("bids").get(i));
                }


                StringBuilder asks = new StringBuilder();
                double Totalasks = 0;

                for(int i=0;i<jsonNode.get("asks").size();i++) {
                    Totalasks += jsonNode.get("asks").get(i).get(0).asDouble();
                    asks.append(jsonNode.get("asks").get(i));
                }


                Depthdata.add(bids.toString());
                Depthdata.add(asks.toString());

                double Total = Totalbids + Totalasks;

                Totalbids = (Totalbids/Total) * 100;
                Totalasks = (Totalasks/Total) * 100;

                Depthdata.add(Double.toString(Totalbids));
                Depthdata.add(Double.toString(Totalasks));

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return Depthdata;
        }
}
