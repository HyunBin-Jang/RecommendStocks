package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.SP500Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SP500StockRepository extends JpaRepository<SP500Stock, Long> {
    /**
     * 특정 주식 코드를 기준으로 주식 정보를 조회합니다.
     * @param stockCode 주식 코드 (예: AAPL)
     * @return SP500Stock 엔터티
     */
    Optional<SP500Stock> findByStockCode(String stockCode);
}