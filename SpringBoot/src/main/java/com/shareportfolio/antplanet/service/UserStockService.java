package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.dto.UserStockWeightDto;
import com.shareportfolio.antplanet.repository.UserStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository userStockRepository;

    @Transactional
    public UserStock updateUserStock(UserStock userStock) {
        // UserStock 업데이트
        UserStock updatedStock = userStockRepository.save(userStock);
        return updatedStock;
    }

    public List<UserStockWeightDto> calculatePortfolioWeights(Long portfolioId) {
        List<Object[]> results = userStockRepository.calculateWeightsByPortfolioId(portfolioId);
        return results.stream()
                .map(result -> new UserStockWeightDto((String) result[0], (Double) result[1])) // stockCode 기반
                .collect(Collectors.toList());
    }


    public UserStock getStockById(Long id) {
        return userStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock not found"));
    }

    public void deleteStockById(Long id) {
        Portfolio portfolio = getStockById(id).getPortfolio();
        userStockRepository.deleteById(id);
    }

    public List<Map<String, Object>> getStockDetailsByPortfolioId(Long portfolioId) {
        return userStockRepository.findStockDetailsByPortfolioId(portfolioId);
    }
}
