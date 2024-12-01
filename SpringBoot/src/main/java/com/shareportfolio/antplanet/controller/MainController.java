package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.domain.SuperInvestor;
import com.shareportfolio.antplanet.service.PortfolioService;
import com.shareportfolio.antplanet.service.SuperInvestorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private final PortfolioService portfolioService;

    public MainController(PortfolioService portfolioService, SuperInvestorService superInvestorService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/")
    public String mainPage(HttpSession session, Model model) {
        // 로그인 여부 확인
        Object userId = session.getAttribute("USER_ID");
        model.addAttribute("isLoggedIn", userId != null);
        model.addAttribute("userName", userId);
        if (userId != null) {
            // 사용자 포트폴리오 가져오기
            Portfolio userPortfolio = portfolioService.getAnyPortfolioByUserId((String) userId);

            if (userPortfolio != null) {
                Map<String, Double> userChartData = portfolioService.
                        calculatePortfolioWeights(userPortfolio.getId());
                model.addAttribute("userChartLabels", userChartData.keySet());
                model.addAttribute("userChartData", userChartData.values());
            }
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "index";
    }
}
