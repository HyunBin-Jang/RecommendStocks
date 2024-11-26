package com.shareportfolio.antplanet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Superinvestor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포트폴리오 고유 ID

    private String name;   // 포트폴리오 이름

    @OneToMany(mappedBy = "superinvestor")
    private List<SuperInvestorStock> stocks;
}
