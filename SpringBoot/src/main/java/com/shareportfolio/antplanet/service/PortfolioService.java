package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.repository.PortfolioRepository;
import com.shareportfolio.antplanet.repository.UserStockRepository;
import org.springframework.stereotype.Service;

import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.domain.User;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private UserStockRepository userStockRepository;
    public List<Portfolio> getPortfoliosByUser(User user) {
        return portfolioRepository.findByUser(user);
    }

    public Optional<Portfolio> getPortfolioById(Long id) {
        return portfolioRepository.findById(id);
    }

    public Portfolio createOrUpdatePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }

    public Portfolio getAnyPortfolioByUserId(String userId) {
        // 사용자 ID로 포트폴리오 중 하나 가져오기
        return portfolioRepository.findFirstByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("No portfolio found for user: " + userId));
    }

    public Map<String, Double> calculatePortfolioWeights(Long portfolioId) {
        List<Object[]> results = userStockRepository.calculateWeightsByPortfolioId(portfolioId);
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0], // Stock Code
                        result -> (Double) result[1]  // Weight
                ));
    }
}
