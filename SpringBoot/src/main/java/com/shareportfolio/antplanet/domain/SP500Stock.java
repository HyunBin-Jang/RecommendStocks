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
    private String stockCode; // 주식 코드 (예: AAPL)

    @Column(nullable = false)
    private String stockName; // 주식 이름(KR) (예: 애플)

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 1)")
    private Double currentPrice; // 현재 가격

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // 마지막 업데이트 시간
}

