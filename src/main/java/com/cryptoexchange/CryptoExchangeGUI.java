package com.cryptoexchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;

public class CryptoExchangeGUI extends JFrame {
    private UserService userService = new UserService();
    private TransactionService transactionService = new TransactionService();
    private User currentUser = null;
    private CryptoMarket cryptoMarket = new CryptoMarket();

    public CryptoExchangeGUI() {
        setTitle("Crypto Exchange");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showMainMenu();
    }

    private void showMainMenu() {
        String[] options = {"Register", "Login", "Exit"};
        int choice = JOptionPane.showOptionDialog(this, "Select an option:", "Crypto Exchange",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> showRegistrationScreen();
            case 1 -> showLoginScreen();
            case 2 -> System.exit(0);
            default -> showMainMenu();
        }
    }

    private void showRegistrationScreen() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Register", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            User user = userService.registerUser(name, email, password);
            JOptionPane.showMessageDialog(this, "Registration successful! Your user ID is: " + user.getUserId());
            showMainMenu();
        } else {
            showMainMenu();
        }
    }

    private void showLoginScreen() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            currentUser = userService.loginUser(email, password);

            if (currentUser != null) {
                showUserMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials. Please try again.");
                int retry = JOptionPane.showConfirmDialog(this, "Do you want to register?", "Login Failed", JOptionPane.YES_NO_OPTION);
                if (retry == JOptionPane.YES_OPTION) {
                    showRegistrationScreen();
                } else {
                    showMainMenu();
                }
            }
        } else {
            showMainMenu();
        }
    }

    private void showUserMenu() {
        String[] options = {"Deposit Money", "View Wallet Balance", "Buy Cryptocurrency", "View Transaction History", "Logout"};
        int choice = JOptionPane.showOptionDialog(this, "Select an option:", "User Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0 -> depositMoney();
            case 1 -> viewWalletBalance();
            case 2 -> showBuyCryptoScreen();
            case 3 -> viewTransactionHistory();
            case 4 -> {
                currentUser = null;
                showMainMenu();
            }
            default -> showUserMenu();
        }
    }

    private void depositMoney() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        try {
            double amount = Double.parseDouble(amountStr);
            currentUser.deposit(amount);
            JOptionPane.showMessageDialog(this, "Deposited: $" + amount + "\nNew Balance: $" + currentUser.getWalletBalance());
            transactionService.addTransaction(new Transaction("Deposit", amount, currentUser.getWalletBalance()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered. Please try again.");
        }
        showUserMenu();
    }

    private void viewWalletBalance() {
        String balanceInfo = "Fiat Balance: $" + currentUser.getWalletBalance();
        JOptionPane.showMessageDialog(this, balanceInfo);
        showUserMenu();
    }

    private void showBuyCryptoScreen() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JComboBox<String> cryptoDropdown = new JComboBox<>(new String[]{"BTC", "ETH"});
        JTextField amountField = new JTextField();

        panel.add(new JLabel("Select Cryptocurrency:"));
        panel.add(cryptoDropdown);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Buy Cryptocurrency", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String crypto = (String) cryptoDropdown.getSelectedItem();
            String amountStr = amountField.getText();
            try {
                double amount = validateDecimal(amountStr);
                if (cryptoMarket.buyCryptocurrency(crypto, amount, currentUser)) {
                    JOptionPane.showMessageDialog(this, "Bought: " + amount + " " + crypto);
                    transactionService.addTransaction(new Transaction("Buy", amount, cryptoMarket.getPrice(crypto)));
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds or cryptocurrency not available.");
                }
            } catch (NumberFormatException | ParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount entered. Please enter a valid decimal number.");
            }
        }
        showUserMenu();
    }

    private void viewTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        for (Transaction transaction : transactionService.getTransactionHistory()) {
            history.append(transaction.getType())
                    .append(": $").append(transaction.getAmount())
                    .append(" at price: $").append(transaction.getPrice()).append("\n");
        }
        JOptionPane.showMessageDialog(this, history.toString());
        showUserMenu();
    }

    private double validateDecimal(String amountStr) throws ParseException {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
        return decimalFormat.parse(amountStr).doubleValue();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CryptoExchangeGUI gui = new CryptoExchangeGUI();
            gui.setVisible(true);
        });
    }
}
