package com.shareportfolio.antplanet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @Column(nullable = false)
    private String stockCode; // 주식 코드 (예: AAPL)

    @Column(nullable = false)
    private String stockName; // 주식 이름 (예: Apple Inc.)

    @Column(nullable = false)
    private double purchasePrice; // 매입 가격

    @Column(nullable = false)
    private int quantity; // 매수 수량

    @Column(nullable = false)
    private double currentPrice; // 현재 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio; // 소속된 포트폴리오
}
