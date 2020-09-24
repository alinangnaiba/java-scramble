package com.alyn.sample.app.controller;

import com.alyn.sample.app.pojo.SummaryByProductsResponse;
import com.alyn.sample.app.pojo.TransactionResponse;
import com.alyn.sample.app.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssignmentController {

    @Autowired
    private TransactionService service;

    @RequestMapping("/")
    public String index() {

        return "Hello";
    }

    @RequestMapping("/assignment/transaction/{id}")
    public ResponseEntity<TransactionResponse> transaction(@PathVariable int id) {
        TransactionResponse response = service.getTransactionById(id);
        if (response.getTransactionId() == 0) {
            return ResponseEntity.unprocessableEntity().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/assignment/transactionSummaryByProducts/{lastNDays}")
    public ResponseEntity<SummaryByProductsResponse> transactionSummaryByProducts(@PathVariable int lastNDays) {
        SummaryByProductsResponse response = service.getTransactionSummaryByProducts(lastNDays);
        if (response.getSummary() == null){
            return ResponseEntity.unprocessableEntity().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/assignment/transactionSummaryByManufacturingCity/{lastNDays}")
    public ResponseEntity<SummaryByProductsResponse> transactionSummaryByManufacturingCity(@PathVariable int lastNDays) {
        SummaryByProductsResponse response = service.transactionSummaryByManufacturingCity(lastNDays);
        if (response.getSummary() == null){
            return ResponseEntity.unprocessableEntity().body(response);
        }
        return ResponseEntity.ok(response);
    }
}
