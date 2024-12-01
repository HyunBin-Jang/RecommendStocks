package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.Portfolio;
import com.shareportfolio.antplanet.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByUser(User user); // 특정 사용자의 포트폴리오 조회
    // 특정 사용자 ID로 포트폴리오 중 하나를 가져오기
    Optional<Portfolio> findFirstByUserId(String userId);
}
