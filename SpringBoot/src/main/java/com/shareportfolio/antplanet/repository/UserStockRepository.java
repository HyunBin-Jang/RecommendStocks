package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface UserStockRepository extends JpaRepository<UserStock, Long> {
    List<UserStock> findByPortfolio(Portfolio portfolio); // 특정 포트폴리오의 주식 조회

    @Query("""

            SELECT us.sp500Stock.stockCode AS stockCode, 
           (us.sp500Stock.currentPrice * us.quantity) / 
           SUM(us.sp500Stock.currentPrice * us.quantity) OVER (PARTITION BY us.portfolio.id) * 100 AS weight
            FROM UserStock us
            WHERE us.portfolio.id = :portfolioId
    """)
    List<Object[]> calculateWeightsByPortfolioId(@Param("portfolioId") Long portfolioId);

    @Query("SELECT new map( " +
            "us.sp500Stock.stockCode AS stockCode, " +
            "us.sp500Stock.stockName AS stockName, " +
            "us.quantity AS quantity, " +
            "us.purchasePrice AS purchasePrice, " +
            "us.sp500Stock.currentPrice AS currentPrice, " +
            "(us.sp500Stock.currentPrice - us.purchasePrice) / us.purchasePrice * 100 AS returnPercentage " +
            ") " +
            "FROM UserStock us WHERE us.portfolio.id = :portfolioId")
    List<Map<String, Object>> findStockDetailsByPortfolioId(@Param("portfolioId") Long portfolioId);
}
