package com.cryptoexchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CryptoMarket {
    private Map<String, Double> prices = new HashMap<>();
    private double btcSupply = 100;
    private double ethSupply = 500;
    private Random random = new Random();

    public void initializeMarket() {
        prices.put("BTC", 50000.0);
        prices.put("ETH", 3000.0);
        // Start a thread to simulate price fluctuations
        new Thread(this::simulatePriceFluctuations).start();
    }

    public boolean buyCryptocurrency(String crypto, double amount, User user) {
        double totalCost = getPrice(crypto) * amount;
        if (totalCost <= user.getWalletBalance() && getSupply(crypto) >= amount) {
            user.deposit(-totalCost); // Deduct from user's balance
            // Update supply
            updateSupply(crypto, -amount);
            return true;
        }
        return false;
    }

    public double getPrice(String crypto) {
        return prices.getOrDefault(crypto, 0.0);
    }

    private double getSupply(String crypto) {
        return switch (crypto) {
            case "BTC" -> btcSupply;
            case "ETH" -> ethSupply;
            default -> 0;
        };
    }

    private void updateSupply(String crypto, double amount) {
        switch (crypto) {
            case "BTC" -> btcSupply += amount;
            case "ETH" -> ethSupply += amount;
        }
    }

    private void simulatePriceFluctuations() {
        while (true) {
            try {
                Thread.sleep(5000); // Simulate every 5 seconds
                for (String crypto : prices.keySet()) {
                    double fluctuation = (random.nextDouble() - 0.5) * 0.1; // +/- 10%
                    prices.put(crypto, prices.get(crypto) * (1 + fluctuation));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
