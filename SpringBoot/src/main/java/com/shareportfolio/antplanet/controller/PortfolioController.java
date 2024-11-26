package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.domain.User;
import com.shareportfolio.antplanet.service.PortfolioService;
import com.shareportfolio.antplanet.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private UserService userService;

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
    public String getPortfolioDetail(@PathVariable Long id, Model model) {
        Optional<Portfolio> portfolio = portfolioService.getPortfolioById(id);
        if (portfolio.isPresent()) {
            model.addAttribute("portfolio", portfolio.get());
            return "portfolio/detail"; // Thymeleaf 템플릿 (templates/portfolio/detail.html)
        } else {
            return "redirect:/portfolio?error=PortfolioNotFound";
        }
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
    public String deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return "redirect:/portfolio";
    }

    // 현재 사용자 정보를 가져오는 메서드 (예제용)
    private User getCurrentUser(Principal principal) {
        // 실제 구현은 SecurityContext에서 User를 가져와야 함
        User user = new User();
        user.setUsername(principal.getName());
        return user;
    }
}
