package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.domain.SP500Stock;
import com.shareportfolio.antplanet.domain.User;
import com.shareportfolio.antplanet.domain.UserStock;
import com.shareportfolio.antplanet.dto.RecommendedStockDto;
import com.shareportfolio.antplanet.dto.UserStockWeightDto;
import com.shareportfolio.antplanet.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStockService userStockService;
    @Autowired
    private SP500Service sp500Service;
    @Autowired
    private PortfolioRecommendationService portfolioRecommendationService;

    // 사용자의 포트폴리오 목록 페이지
    @GetMapping
    public String listPortfolios(HttpSession session, Model model) {
        // 로그인 여부 확인
        String userId = (String) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login"; // 로그인이 안 되어 있으면 로그인 페이지로 리디렉션
        }
        // USER_ID로 User 객체 조회
        User user = userService.findById(userId); // UserService에서 User 조회
        if (user == null) {
            return "redirect:/login"; // 사용자를 찾을 수 없으면 로그인 페이지로 리디렉션
        }

        // 사용자 포트폴리오 조회
        List<Portfolio> portfolios = portfolioService.getPortfoliosByUser(user);
        model.addAttribute("portfolios", portfolios);
        return "portfolio/list"; // Thymeleaf 템플릿 (templates/portfolio/list.html)
    }

    // 특정 포트폴리오 상세 페이지
    @GetMapping("/{id}")
    public String portfolioDetail(@PathVariable("id") Long id, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        List<UserStock> userStocks = portfolio.getStocks();

       List<Map<String, Object>> stockDetails = userStockService.getStockDetailsByPortfolioId(id);

        // 주식별 수익률 및 포트폴리오 통합 잔고 계산
        double totalInvestment = userStocks.stream()
                .mapToDouble(stock -> stock.getPurchasePrice() * stock.getQuantity())
                .sum();

        double totalCurrentValue = userStocks.stream()
                .mapToDouble(stock -> stock.getSp500Stock().getCurrentPrice() * stock.getQuantity())
                .sum();
        double portfolioReturn = Math.round(((totalCurrentValue - totalInvestment) / totalInvestment) * 100);

        // 추천 주식 계산
        List<RecommendedStockDto> recommendedStocks = portfolioRecommendationService.recommendStocks(id);
        recommendedStocks.forEach(stock -> {
            int percentage = (int) Math.round(stock.getSimilarity() * 100);
            stock.setSimilarityPercentage(percentage + "%");
        });

        // 차트 데이터 준비
        List<String> chartLabels = userStocks.stream()
                .map(stock -> stock.getSp500Stock().getStockCode())
                .toList();
        List<UserStockWeightDto> weights = userStockService.calculatePortfolioWeights(id);
        List<Double> chartData = weights.stream()
                .map(UserStockWeightDto::getWeight)
                .toList();

        model.addAttribute("portfolio", portfolio);
        model.addAttribute("stockDetails", stockDetails);
        model.addAttribute("totalInvestment", totalInvestment);
        model.addAttribute("totalCurrentValue", totalCurrentValue);
        model.addAttribute("portfolioReturn", portfolioReturn);
        model.addAttribute("recommendedStocks", recommendedStocks);
        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "portfolio/detail";
    }

    // 포트폴리오 생성 폼
    @GetMapping("/create")
    public String createPortfolioForm(HttpSession session, Model model) {
        // 로그인 여부 확인
        String userId = (String) session.getAttribute("USER_ID");
        if (userId == null) {
            return "redirect:/login"; // 로그인이 안 되어 있으면 로그인 페이지로 리디렉션
        }
        model.addAttribute("portfolio", new Portfolio());
        return "portfolio/create"; // Thymeleaf 템플릿 (templates/portfolio/create.html)
    }

    // 포트폴리오 생성 처리
    @PostMapping("/create")
    public String createPortfolio(@ModelAttribute Portfolio portfolio, HttpSession session) {
        String userId = (String) session.getAttribute("USER_ID");
        User user = userService.findById(userId); // UserService에서 User 조회
        if (user == null) {
            return "redirect:/login"; // 사용자를 찾을 수 없으면 로그인 페이지로 리디렉션
        }
        portfolio.setUser(user);
        portfolioService.createOrUpdatePortfolio(portfolio);
        return "redirect:/portfolio";
    }

    // 포트폴리오 삭제
    @PostMapping("/{id}/delete")
    public String deletePortfolio(@PathVariable("id") Long id) {
        portfolioService.deletePortfolio(id);
        return "redirect:/portfolio";
    }

    @GetMapping("/{id}/edit")
    public String editPortfolio(@PathVariable("id") Long id, Model model) {
        Portfolio portfolio = portfolioService.getPortfolioById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));

        List<UserStock> userStocks = portfolio.getStocks();

        model.addAttribute("portfolio", portfolio);
        model.addAttribute("userStocks", userStocks);

        return "portfolio/edit";
    }

    @PostMapping("/{id}/update")
    public String updatePortfolio(@PathVariable("id") Long id,
                                  @RequestParam(name = "stocks", required = false) List<UserStock> stocks,
                                  @RequestParam(name = "newStockCode", required = false) String newStockCode,
                                  @RequestParam(name = "newStockQuantity", required = false) Integer newStockQuantity,
                                  @RequestParam(name = "newStockPurchasePrice", required = false) Double newStockPurchasePrice,
                                  RedirectAttributes redirectAttributes){
        Portfolio portfolio = portfolioService.getPortfolioById(id)
                .orElseThrow(() -> new IllegalArgumentException("Portfolio not found"));
        if (!sp500Service.isSP500Stock(newStockCode)) {
            redirectAttributes.addFlashAttribute("errorMessage", "The stock code is not part of the S&P 500.");
        }
        // Update existing stocks
        if (stocks != null) {
            for (UserStock stock : stocks) {
                UserStock existingStock = userStockService.getStockById(stock.getId());
                existingStock.setQuantity(stock.getQuantity());
                existingStock.setPurchasePrice(stock.getPurchasePrice());
                userStockService.updateUserStock(existingStock);
            }
        }

        // Add new stock
        if (newStockCode != null && !newStockCode.isEmpty() && newStockQuantity != null && newStockPurchasePrice != null) {
            SP500Stock sp500Stock = sp500Service.getStockByCode(newStockCode)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid stock code"));

            UserStock newStock = new UserStock();
            newStock.setPortfolio(portfolio);
            newStock.setSp500Stock(sp500Stock);
            newStock.setQuantity(newStockQuantity);
            newStock.setPurchasePrice(newStockPurchasePrice);
            userStockService.updateUserStock(newStock);
        }

        return "redirect:/portfolio/" + id;
    }

    @PostMapping("/{id}/delete/{stockId}")
    public String deleteStock(@PathVariable("id") Long id, @PathVariable("stockId") Long stockId) {
        userStockService.deleteStockById(stockId);
        return "redirect:/portfolio/" + id + "/edit";
    }
}
