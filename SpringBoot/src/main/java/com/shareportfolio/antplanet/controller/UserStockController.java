package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.service.UserStockService;
import com.shareportfolio.antplanet.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/userstocks")
public class UserStockController {

    @Autowired
    private UserStockService userStockService;

    @Autowired
    private PortfolioService portfolioService;

    // 주식 생성 폼
    @GetMapping("/{portfolioId}/create")
    public String createUserStockForm(@PathVariable Long portfolioId, Model model) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioById(portfolioId);
        if (portfolio.isPresent()) {
            UserStock userStock = new UserStock();
            userStock.setPortfolio(portfolio.get());
            model.addAttribute("userStock", userStock);
            return "userstocks/create"; // templates/userstocks/create.html
        } else {
            return "redirect:/portfolio?error=PortfolioNotFound";
        }
    }

    // 주식 생성 처리
    @PostMapping("/{portfolioId}/create")
    public String createUserStock(@PathVariable Long portfolioId, @ModelAttribute UserStock userStock) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioById(portfolioId);
        if (portfolio.isPresent()) {
            userStock.setPortfolio(portfolio.get());
            userStockService.updateUserStock(userStock);
            return "redirect:/userstocks/" + portfolioId;
        } else {
            return "redirect:/portfolio?error=PortfolioNotFound";
        }
    }

}
