package com.cryptoexchange;

import javax.swing.*;
import java.util.List;

public class CryptoExchange {
    private UserService userService = new UserService();
    private TransactionService transactionService = new TransactionService();
    private User currentUser = null;
    private CryptoMarket cryptoMarket = new CryptoMarket();

    public void showMainMenu() {
        String[] options = {"Register", "Login", "Exit"};
        int choice = JOptionPane.showOptionDialog(null, "Select an option:", "Crypto Exchange",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> registerUser();
            case 1 -> loginUser();
            case 2 -> System.exit(0);
            default -> showMainMenu();
        }
    }

    private void registerUser() {
        String name = JOptionPane.showInputDialog("Enter your name:");
        String email = JOptionPane.showInputDialog("Enter your email:");
        String password = JOptionPane.showInputDialog("Create a password:");
        User user = userService.registerUser(name, email, password);
        JOptionPane.showMessageDialog(null, "Registration successful! Your user ID is: " + user.getUserId());
        showMainMenu();
    }

    private void loginUser() {
        String email = JOptionPane.showInputDialog("Enter your email:");
        String password = JOptionPane.showInputDialog("Enter your password:");
        currentUser = userService.loginUser(email, password);

        if (currentUser != null) {
            showUserMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
            showMainMenu();
        }
    }

    private void showUserMenu() {
        String[] options = {"Deposit Money", "View Wallet Balance", "Buy Cryptocurrency", "View Transaction History", "Logout"};
        int choice = JOptionPane.showOptionDialog(null, "Select an option:", "User Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> depositMoney();
            case 1 -> viewWalletBalance();
            case 2 -> buyCryptocurrency();
            case 3 -> viewTransactionHistory();
            case 4 -> {
                currentUser = null;
                showMainMenu();
            }
            default -> showUserMenu();
        }
    }

    private void depositMoney() {
        String amountStr = JOptionPane.showInputDialog("Enter amount to deposit:");
        double amount = Double.parseDouble(amountStr);
        currentUser.deposit(amount);
        JOptionPane.showMessageDialog(null, "Deposited: $" + amount + "\nNew Balance: $" + currentUser.getWalletBalance());
        transactionService.addTransaction(new Transaction("Deposit", amount, currentUser.getWalletBalance()));
        showUserMenu();
    }

    private void viewWalletBalance() {
        String balanceInfo = "Fiat Balance: $" + currentUser.getWalletBalance();
        JOptionPane.showMessageDialog(null, balanceInfo);
        showUserMenu();
    }

    private void buyCryptocurrency() {
        String crypto = JOptionPane.showInputDialog("Enter cryptocurrency (BTC or ETH):");
        String amountStr = JOptionPane.showInputDialog("Enter amount to buy:");
        double amount = Double.parseDouble(amountStr);

        if (cryptoMarket.buyCryptocurrency(crypto, amount, currentUser)) {
            JOptionPane.showMessageDialog(null, "Bought: " + amount + " " + crypto);
            transactionService.addTransaction(new Transaction("Buy", amount, cryptoMarket.getPrice(crypto)));
        } else {
            JOptionPane.showMessageDialog(null, "Insufficient funds or cryptocurrency not available.");
        }
        showUserMenu();
    }

    private void viewTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        List<Transaction> transactions = transactionService.getTransactionHistory();
        for (Transaction transaction : transactions) {
            history.append(transaction.getType())
                    .append(": $").append(transaction.getAmount())
                    .append(" at price: $").append(transaction.getPrice()).append("\n");
        }
        JOptionPane.showMessageDialog(null, history.toString());
        showUserMenu();
    }

    public static void main(String[] args) {
        CryptoExchange app = new CryptoExchange();
        app.cryptoMarket.initializeMarket();
        app.showMainMenu();
    }
}
