package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.SuperInvestor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuperInvestorRepository extends JpaRepository<SuperInvestor, Long> {
    Optional<SuperInvestor> findByName(String name);
}

