package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.repository.UserStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStockService {

    @Autowired
    private UserStockRepository userStockRepository;

    public List<UserStock> getStocksByPortfolio(Portfolio portfolio) {
        return userStockRepository.findByPortfolio(portfolio);
    }

    public Optional<UserStock> getUserStockById(Long id) {
        return userStockRepository.findById(id);
    }

    public UserStock createOrUpdateUserStock(UserStock userStock) {
        return userStockRepository.save(userStock);
    }

    public void deleteUserStock(Long id) {
        userStockRepository.deleteById(id);
    }
}
