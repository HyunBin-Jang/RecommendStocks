package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.User;
import com.shareportfolio.antplanet.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html로 매핑
    }

    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(@RequestParam("id") String id,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model model) {
        System.out.println(id + " " + password);
        boolean isAuthenticated = userService.authenticate(id, password);

        if (isAuthenticated) {
            model.addAttribute("id", id);
            session.setAttribute("USER_ID", id); // 세션에 사용자 정보 저장
            return "redirect:/"; // 로그인 성공 시 프로필 페이지로 이동
        } else {
            model.addAttribute("error", "존재하지 않는 아이디 혹은 비밀번호 입니다");
            return "login"; // 로그인 실패 시 다시 로그인 페이지
        }
    }

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login"; // 로그인 페이지로 이동
    }

    /**
     * 회원가입 페이지
     */
    @GetMapping("/register")
    public String registerPage() {
        return "/register"; // templates/user/register.html로 매핑
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/register")
    public String register(@RequestParam("id") String id,
                           @RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           Model model) {
        try {
            User newUser = new User();
            newUser.setId(id);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            userService.register(newUser);

            return "redirect:/login"; // 회원가입 성공 시 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "User already exists");
            return "/register"; // 회원가입 실패 시 다시 회원가입 페이지
        }
    }

    /**
     * 프로필 페이지
     */
    @GetMapping("/profile")
    public String userProfile(Model model, @RequestParam("username") String username) {
        User user = userService.getUser(username);
        model.addAttribute("user", user); // User 객체를 모델에 추가
        return "user/profile"; // templates/user/profile.html로 매핑
    }
}


