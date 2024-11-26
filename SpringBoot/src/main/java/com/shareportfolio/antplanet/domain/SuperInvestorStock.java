package com.shareportfolio.antplanet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class SuperInvestorStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockcode", nullable = false)
    private SP500Stock sp500Stock; // SP500Stock 참조

    @Column(nullable = false)
    private int quantity; // 매수 수량

    @Column(nullable = false)
    private double Weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "superinvestor_id", nullable = false)
    private Superinvestor superinvestor; // 소속된 포트폴리오
}
