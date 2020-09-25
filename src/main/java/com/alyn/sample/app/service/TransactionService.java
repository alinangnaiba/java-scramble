package com.alyn.sample.app.service;

import com.alyn.sample.app.data.DataStore;
import com.alyn.sample.app.data.ProductReference;
import com.alyn.sample.app.data.Transaction;
import com.alyn.sample.app.pojo.Summary;
import com.alyn.sample.app.pojo.SummaryResponse;
import com.alyn.sample.app.pojo.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TransactionService {

    private DataStore dataStore;

    public TransactionService() {
        dataStore = DataStore.getInstance();
    }

    public TransactionResponse getTransactionById(int id) {

        TransactionResponse response = new TransactionResponse();
        List<Transaction> transactions = dataStore.getTransactions();
        List<ProductReference> productReference = dataStore.getProductReference();
        Optional<Transaction> transaction = transactions.stream().filter(t -> id == t.getTransactionId()).findFirst();
        if (transaction.isPresent()) {
            Transaction value = transaction.get();
            String productName = productReference.stream()
                    .filter(p -> p.getProductId() == value.getProductId()).findFirst()
                    .map(p -> p.getProductName())
                    .get();

            response.setTransactionId(value.getTransactionId());
            response.setProductName(productName);
            response.setTransactionAmount(value.getTransactionAmount());
            response.setTransactionDatetime(value.getTransactionDatetime());
        }

        return response;
    }

    public SummaryResponse getTransactionSummaryByProducts(int lastNdays) {

        LocalDateTime minDate = LocalDateTime.now().minusDays(lastNdays);
        SummaryResponse response = new SummaryResponse();
        List<Transaction> transactions = dataStore.getTransactions();

         List<Transaction> transactionFromDate = transactions.stream()
                .filter(t -> t.getTransactionDatetime().isEqual(minDate) || t.getTransactionDatetime().isAfter(minDate))
                .collect(Collectors.toList());
         Map<String, List<Transaction>> group = transactionFromDate.stream()
                 .collect(Collectors.groupingBy(Transaction::getProductName));
         List<Summary> summaries = new ArrayList<>();
         for(Map.Entry<String, List<Transaction>> entry : group.entrySet()) {
             String productName = entry.getKey();
             List<Transaction> tr = entry.getValue();
             BigDecimal total = tr.stream()
                     .map(Transaction::getTransactionAmount)
                     .reduce(BigDecimal.ZERO, BigDecimal::add);
             Summary summary = new Summary();
             summary.setProductName(productName);
             summary.setTotalAmount(total);

             summaries.add(summary);
         }
         response.setSummary(summaries);
        return response;
    }

    public SummaryResponse transactionSummaryByManufacturingCity(int lastNdays) {

        LocalDateTime minDate = LocalDateTime.now().minusDays(lastNdays);
        SummaryResponse response = new SummaryResponse();
        List<Transaction> transactions = dataStore.getTransactions();

        List<Transaction> transactionFromDate = transactions.stream()
                .filter(t -> t.getTransactionDatetime().isEqual(minDate) || t.getTransactionDatetime().isAfter(minDate))
                .collect(Collectors.toList());
        Map<String, List<Transaction>> group = transactionFromDate.stream()
                .collect(Collectors.groupingBy(Transaction::getProductManufacturingCity));
        List<Summary> summaries = new ArrayList<>();
        for(Map.Entry<String, List<Transaction>> entry : group.entrySet()) {
            String cityName = entry.getKey();
            List<Transaction> tr = entry.getValue();
            BigDecimal total = tr.stream()
                    .map(Transaction::getTransactionAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Summary summary = new Summary();
            summary.setCityName(cityName);
            summary.setTotalAmount(total);

            summaries.add(summary);
        }
        response.setSummary(summaries);
        return response;
    }
}
