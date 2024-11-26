package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    List<UserStock> findByPortfolio(Portfolio portfolio); // 특정 포트폴리오의 주식 조회
}