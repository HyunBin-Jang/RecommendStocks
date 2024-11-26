package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private final PortfolioRepository portfolioRepository;
    public PortfolioService(PortfolioRepository portfolioRepository){this.portfolioRepository = portfolioRepository;}

}
