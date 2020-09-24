package com.alyn.sample.app.data;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private List<Transaction> transactions;
    private List<ProductReference> productReference;
    private static DataStore instance;

    private DataStore() {
        transactions = new ArrayList<>();
        productReference = new ArrayList<>();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }

        return instance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<ProductReference> getProductReference() {
        return productReference;
    }

    public void addTransactions(List<Transaction> newTransactions) {
        transactions.addAll(newTransactions);
    }

    public void addProductReference(List<ProductReference> newProductRef) {
        productReference.addAll(newProductRef);
    }
}
