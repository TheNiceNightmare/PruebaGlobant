package com.cryptoexchange;

public class Transaction {
    private String type;
    private double amount;
    private double price;

    public Transaction(String type, double amount, double price) {
        this.type = type;
        this.amount = amount;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }
}
