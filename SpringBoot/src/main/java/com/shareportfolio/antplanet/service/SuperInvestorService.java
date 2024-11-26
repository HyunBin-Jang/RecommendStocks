package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.controller.SuperInvestorDTO;
import com.shareportfolio.antplanet.domain.SP500Stock;
import com.shareportfolio.antplanet.domain.SuperInvestor;
import com.shareportfolio.antplanet.domain.SuperInvestorStock;
import com.shareportfolio.antplanet.repository.SP500StockRepository;
import com.shareportfolio.antplanet.repository.SuperInvestorRepository;
import com.shareportfolio.antplanet.repository.SuperInvestorStockRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperInvestorService {
    @Autowired
    private SuperInvestorRepository superInvestorRepository;

    @Autowired
    private SP500StockRepository sp500StockRepository;

    @Autowired
    private SuperInvestorStockRepository superInvestorStockRepository;

    public void saveOrUpdateSuperinvestors(List<SuperInvestorDTO> superInvestorDTOs) {
        for (SuperInvestorDTO dto : superInvestorDTOs) {
            saveOrUpdateSuperinvestor(dto);
        }
    }


    @Transactional
    private void saveOrUpdateSuperinvestor(SuperInvestorDTO superInvestorDTO) {
        // Superinvestor 찾거나 생성
        SuperInvestor superInvestor = superInvestorRepository.findByName(superInvestorDTO.getName())
                .orElseGet(() -> {
                    SuperInvestor newInvestor = new SuperInvestor();
                    newInvestor.setName(superInvestorDTO.getName());
                    return superInvestorRepository.save(newInvestor);
                });

        // 기존 주식 데이터 삭제
        superInvestorStockRepository.deleteBySuperinvestor(superInvestor);

        // 새로운 주식 데이터 저장
        for (SuperInvestorDTO.StockDTO stockDTO : superInvestorDTO.getStocks()) {
            SP500Stock sp500Stock = sp500StockRepository.findByStockCode(stockDTO.getStockCode())
                    .orElse(null);

            if (sp500Stock == null) {
                continue;
            }
            SuperInvestorStock superInvestorStock = new SuperInvestorStock();
            superInvestorStock.setSp500Stock(sp500Stock);
            superInvestorStock.setQuantity(stockDTO.getQuantity());
            superInvestorStock.setWeight(stockDTO.getWeight());
            superInvestorStock.setSuperinvestor(superInvestor);

            superInvestorStockRepository.save(superInvestorStock);
        }
    }
}




