package com.shareportfolio.antplanet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SP500Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stockCode; // 주식 코드 (예: AAPL)

    @Column(nullable = false)
    private String stockName; // 주식 이름 (예: Apple Inc.)

    @Column(nullable = true)
    private Double currentPrice; // 현재 가격

    @Column(nullable = true)
    private Double previousClosePrice; // 전일 종가

    @Column(nullable = true)
    private Double dayHigh; // 고가

    @Column(nullable = true)
    private Double dayLow; // 저가

    @Column(nullable = true)
    private Double openPrice; // 시가

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // 마지막 업데이트 시간
}

