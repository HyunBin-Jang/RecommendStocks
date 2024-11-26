package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.SP500Stock;
import com.shareportfolio.antplanet.repository.SP500Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SP500Service {

    private final SP500Repository sp500Repository;

    public SP500Service(SP500Repository sp500Repository) {
        this.sp500Repository = sp500Repository;
    }

    /**
     * 모든 S&P 500 주식 정보를 반환합니다.
     * @return S&P 500 주식 정보 리스트
     */
    public List<SP500Stock> getAllStocks() {
        return sp500Repository.findAll();
    }

    /**
     * 특정 주식 코드를 기준으로 주식 정보를 반환합니다.
     * @param stockCode 주식 코드
     * @return SP500Stock 엔터티
     */
    public SP500Stock getStockByCode(String stockCode) {
        return sp500Repository.findByStockCode(stockCode);
    }

    /**
     * 새로운 S&P 500 주식을 저장하거나 기존 데이터를 업데이트합니다.
     * @param stock 업데이트할 주식 데이터
     */
    public void saveOrUpdateStock(SP500Stock stock) {
        SP500Stock existingStock = sp500Repository.findByStockCode(stock.getStockCode());
        if (existingStock != null) {
            // 기존 데이터 업데이트
            existingStock.setCurrentPrice(stock.getCurrentPrice());
            existingStock.setLastUpdated(LocalDateTime.now());
            sp500Repository.save(existingStock);
        } else {
            // 새로운 데이터 삽입
            stock.setLastUpdated(LocalDateTime.now());
            sp500Repository.save(stock);
        }
    }

    /**
     * 대량의 S&P 500 데이터를 저장하거나 업데이트합니다.
     * @param stocks S&P 500 데이터 리스트
     */
    public void saveOrUpdateBulkStocks(List<SP500Stock> stocks) {
        for (SP500Stock stock : stocks) {
            SP500Stock existingStock = sp500Repository.findByStockCode(stock.getStockCode());
            if (existingStock != null) {
                // 기존 데이터 업데이트
                existingStock.setCurrentPrice(stock.getCurrentPrice());
                existingStock.setLastUpdated(LocalDateTime.now());
                sp500Repository.save(existingStock);
            } else {
                // 새로운 데이터 삽입
                stock.setLastUpdated(LocalDateTime.now());
                sp500Repository.save(stock);
            }
        }
    }

    /**
     * S&P 500 주식 데이터를 삭제합니다.
     * @param stockCode 삭제할 주식 코드
     */
    public void deleteStock(String stockCode) {
        SP500Stock stock = sp500Repository.findByStockCode(stockCode);
        if (stock != null) {
            sp500Repository.delete(stock);
        }
    }
}