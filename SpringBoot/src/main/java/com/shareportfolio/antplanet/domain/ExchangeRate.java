package com.shareportfolio.antplanet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRate {
    @Id
    private Long id = 1L; // 고정된 ID 사용

    @Column(nullable = false)
    private String currencyPair = "USD/KRW"; // 고정된 통화쌍

    @Column(nullable = false)
    private Double exchangeRate; // 현재 환율

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // 마지막 업데이트 시간

}
