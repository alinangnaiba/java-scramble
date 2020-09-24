package com.alyn.sample.app.data;

public class ProductReference {
    private int productId;
    private String productName;
    private String productManufacturingCity;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductManufacturingCity() {
        return productManufacturingCity;
    }

    public void setProductManufacturingCity(String productManufacturingCity) {
        this.productManufacturingCity = productManufacturingCity;
    }
}
