package com.cryptoexchange;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private List<Transaction> transactionHistory = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
}
