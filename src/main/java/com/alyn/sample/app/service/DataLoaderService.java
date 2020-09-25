package com.alyn.sample.app.service;

import com.alyn.sample.app.AppConfig;
import com.alyn.sample.app.data.CsvHandler;
import com.alyn.sample.app.data.DataStore;
import com.alyn.sample.app.data.ProductReference;
import com.alyn.sample.app.data.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoaderService {

    private final String FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private AppConfig config;
    private DataStore dataStore;

    public DataLoaderService(AppConfig config) {

        this.config = config;
        this.dataStore = DataStore.getInstance();
    }

    public List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        List<String> files = getFiles(config.getTransactionDirectory());
        List<String[]> csvData = new ArrayList<>();
        files.forEach(file -> {
            List<String[]> data = extractData(file);
            csvData.addAll(data);
        });

        transactions = mapTransactionData(csvData);
        return transactions;
    }

    public List<Transaction> loadTransactions(String path) {
        List<Transaction> transactions;
        List<String[]> data = extractData(path);
        transactions = mapTransactionData(data);
        return transactions;
    }

    public List<ProductReference> loadProductReference() {
        List<ProductReference> list;
        List<String> files = getFiles(config.getReferenceDirectory());
        List<String[]> csvData = new ArrayList<>();
        files.forEach(file -> {
            List<String[]> data = extractData(file);
            csvData.addAll(data);
        });

        list = mapProductReferenceData(csvData);

        return list;
    }

    private List<ProductReference> mapProductReferenceData(List<String[]> csvData) {
        List<ProductReference> reference = new ArrayList<>();
        for(String[] row : csvData) {
            ProductReference pr = new ProductReference();
            pr.setProductId(Integer.parseInt(row[0]));
            pr.setProductName(row[1]);
            pr.setProductManufacturingCity(row[2]);
            reference.add(pr);
        }

        return reference;
    }

    private List<Transaction> mapTransactionData(List<String[]> csvData) {
        List<Transaction> transactions = new ArrayList<>();
        List<ProductReference> productReference = dataStore.getProductReference();
        for(String[] row : csvData) {
            Transaction transaction = new Transaction();
            transaction.setTransactionId(Integer.parseInt(row[0]));
            int productId = Integer.parseInt(row[1]);
            transaction.setProductId(productId);
            transaction.setTransactionAmount(new BigDecimal(row[2]).setScale(1, RoundingMode.CEILING));
            DateTimeFormatter format = DateTimeFormatter.ofPattern(FORMAT_PATTERN);
            transaction.setTransactionDatetime(LocalDateTime.parse(row[3], format));
            Optional<ProductReference> ref = productReference.stream()
                    .filter(p -> p.getProductId() == productId).findFirst();
            if (ref.isPresent()){
                ProductReference r = ref.get();
                transaction.setProductName(r.getProductName());
                transaction.setProductManufacturingCity(r.getProductManufacturingCity());
            }

            transactions.add(transaction);
        }

        return transactions;
    }

    private List<String[]> extractData(String path) {
        CsvHandler handler = new CsvHandler(path);
        List<String[]> data = null;
        try {
            data = handler.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private List<String> getFiles(String path) {
        List<String> result = null;
        try (Stream<Path> walk = Files.walk(Paths.get(path))){
            result = walk.map(x -> x.toString())
                    .filter(f -> f.endsWith(".csv")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
