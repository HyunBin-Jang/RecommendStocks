package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
