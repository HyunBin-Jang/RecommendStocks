package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.repository.UserStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository userStockRepository;

    @Transactional
    public UserStock updateUserStock(UserStock userStock) {
        // UserStock 업데이트
        UserStock updatedStock = userStockRepository.save(userStock);

        // 동일한 포트폴리오의 다른 UserStock들의 비중 업데이트
        updatePortfolioWeights(userStock.getPortfolio());

        return updatedStock;
    }

    private void updatePortfolioWeights(Portfolio portfolio) {
        // 동일 포트폴리오의 모든 UserStock 가져오기
        List<UserStock> stocks = userStockRepository.findByPortfolio(portfolio);

        // 전체 포트폴리오 가치 계산 (SP500Stock의 currentPrice를 기준으로 계산)
        double totalValue = stocks.stream()
                .mapToDouble(stock -> stock.getQuantity() * stock.getSp500Stock().getCurrentPrice())
                .sum();

        // 각 UserStock의 비중(weight) 업데이트
        for (UserStock stock : stocks) {
            double stockValue = stock.getQuantity() * stock.getSp500Stock().getCurrentPrice();
            double weight = (totalValue == 0) ? 0 : (stockValue / totalValue) * 100;
            stock.setWeight(weight);
        }

        // 변경 사항 저장
        userStockRepository.saveAll(stocks);
    }
    public UserStock getStockById(Long id) {
        return userStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
    }

    public void deleteStockById(Long id) {
        Portfolio portfolio = getStockById(id).getPortfolio();
        userStockRepository.deleteById(id);
        updatePortfolioWeights(portfolio);
    }
}
