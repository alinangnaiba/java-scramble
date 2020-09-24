package com.alyn.sample.app.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonPropertyOrder({"transactionId","productName", "transactionAmount", "transactionDatetime" })
public class TransactionResponse {

    private String productName;
    private int transactionId;
    private BigDecimal transactionAmount;
    private String transactionDatetime;

    @JsonProperty("transactionId")
    public int getTransactionId() {
        return this.transactionId;
    }
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    @JsonProperty("productName")
    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("transactionAmount")
    public BigDecimal getTransactionAmount() {
        return this.transactionAmount;
    }
    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @JsonProperty("transactionDatetime")
    public String getTransactionDatetime() {
        return this.transactionDatetime;
    }
    public void setTransactionDatetime(LocalDateTime transactionDatetime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.transactionDatetime = transactionDatetime.format(formatter);
    }
}
