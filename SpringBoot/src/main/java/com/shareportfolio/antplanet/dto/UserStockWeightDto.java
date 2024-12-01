package com.shareportfolio.antplanet.dto;

public class UserStockWeightDto {
    private String stockCode; // SP500Stock의 stockCode
    private Double weight;    // 비중

    public UserStockWeightDto(String stockCode, Double weight) {
        this.stockCode = stockCode;
        this.weight = weight;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
