package com.shareportfolio.antplanet.repository;

import com.shareportfolio.antplanet.domain.SuperInvestor;
import com.shareportfolio.antplanet.domain.SuperInvestorStock;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperInvestorStockRepository extends JpaRepository<SuperInvestorStock, Long> {
    @Modifying
    @Transactional
    void deleteBySuperinvestor(SuperInvestor superinvestor);
}

