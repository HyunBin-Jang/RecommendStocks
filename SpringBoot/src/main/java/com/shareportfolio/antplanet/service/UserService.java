package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.User;
import com.shareportfolio.antplanet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 사용자 인증 로직
     */
    public boolean authenticate(String id, String password) {
        return userRepository.findById(id)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    /**
     * 사용자 등록 (회원가입)
     */
    public User register(User user) {
        if (userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with this ID already exists");
        }
        return userRepository.save(user);
    }

    /**
     * 사용자 정보 조회
     */
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
