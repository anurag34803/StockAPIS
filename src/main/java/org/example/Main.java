package org.example;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.List;

import static org.example.SinglePairData.get24hrSinglePairData;
import static org.example.WriteData.writeDataToExcel;

import static Depth.DepthOfCoin.getDepthOfCoin;

public class Main {
    // Column name for pair data
    static List<String> c1 = List.of("","openPrice", "lowPrice", "highPrice", "currentPrice", "baseVolume", "quoteVolume", "time");

    // Column name for market depth
    static List<String> c2 = List.of("","time", "bids", "asks", "bids%", "asks%");
    static String coins[] = {"QKC/INR","RLC/INR","MTL/INR",
            "AVAX/INR","SOL/INR","ANKR/INR",
            "ETC/INR","APT/INR","THETA/INR"};

    public static void main(String[] args) throws Exception {

        Redirect System.out to null output stream
        System.setOut(new PrintStream(new OutputStream() {
           @Override
           public void write(int b) throws IOException {
               // Do nothing, suppress output
           }
       }));

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule the task to run every 1 minute
        for(String coin : coins) {
            scheduler.scheduleAtFixedRate(() -> {
                // Call your function here
                try {
                    ArrayList<String> coinData = get24hrSinglePairData(coin);
                    writeDataToExcel(coinData,c1,"CoinData.xlsx");
                    //System.out.println("Data written to Excel successfully. for coin " + coin);
                    ArrayList<String> Depth = getDepthOfCoin(coin);
                    writeDataToExcel(Depth,c2,"MarketDepth.xlsx");
                    //System.out.println("Data written to Excel successfully. for coin " + coin);

                } catch (Exception e) {
                    System.err.println("Error writing data to Excel: " + e.getMessage());
                }
            }, 0, 1, TimeUnit.MINUTES);
        }
    }
}
