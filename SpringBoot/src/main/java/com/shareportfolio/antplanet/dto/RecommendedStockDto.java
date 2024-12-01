package com.shareportfolio.antplanet.dto;

import com.shareportfolio.antplanet.domain.SP500Stock;

public class RecommendedStockDto {
    private SP500Stock stock;
    private String superInvestorName;
    private Double similarity;
    private String similarityPercentage;

    public RecommendedStockDto(SP500Stock stock, String superInvestorName, Double similarity) {
        this.stock = stock;
        this.superInvestorName = superInvestorName;
        this.similarity = similarity;
    }
    public void setSimilarityPercentage(String similarityPercentage) {
        this.similarityPercentage = similarityPercentage;
    }

    public SP500Stock getStock() {
        return stock;
    }

    public String getSuperInvestorName() {
        return superInvestorName;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public String getSimilarityPercentage() {
        return similarityPercentage;
    }
}
