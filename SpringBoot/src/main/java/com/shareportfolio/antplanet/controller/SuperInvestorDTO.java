package com.shareportfolio.antplanet.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SuperInvestorDTO {
    private String name; // SuperInvestor 이름
    private List<StockDTO> stocks; // 주식 리스트

    @Getter
    @Setter
    public static class StockDTO {
        private String stockCode; // 주식 코드
        private int quantity; // 매수 수량
        private double weight; // 비중
    }
}

