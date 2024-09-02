package com.cryptoexchange;

public class User {
    private String name;
    private String email;
    private String password;
    private int userId;
    private double walletBalance;

    public User(String name, String email, String password, int userId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.walletBalance = 0;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void deposit(double amount) {
        this.walletBalance += amount;
    }
}
