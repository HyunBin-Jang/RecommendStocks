package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.SP500Stock;
import com.shareportfolio.antplanet.repository.SP500StockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SP500Service {

    private final SP500StockRepository sp500StockRepository;

    public SP500Service(SP500StockRepository sp500StockRepository) {
        this.sp500StockRepository = sp500StockRepository;
    }

    /**
     * 모든 S&P 500 주식 정보를 반환합니다.
     * @return S&P 500 주식 정보 리스트
     */
    public List<SP500Stock> getAllStocks() {
        return sp500StockRepository.findAll();
    }

    /**
     * 특정 주식 코드를 기준으로 주식 정보를 반환합니다.
     * @param stockCode 주식 코드
     * @return SP500Stock 엔터티
     */
    public Optional<SP500Stock> getStockByCode(String stockCode) {
        return sp500StockRepository.findByStockCode(stockCode);
    }

    /**
     * 새로운 S&P 500 주식을 저장하거나 기존 데이터를 업데이트합니다.
     * @param stock 업데이트할 주식 데이터
     */
    public void saveOrUpdateStock(SP500Stock stock) {
        SP500Stock existingStock = sp500StockRepository.findByStockCode(stock.getStockCode()).orElse(null);
        if (existingStock != null) {
            // 기존 데이터 업데이트
            existingStock.setCurrentPrice(stock.getCurrentPrice());
            existingStock.setLastUpdated(LocalDateTime.now());
            sp500StockRepository.save(existingStock);
        } else {
            // 새로운 데이터 삽입
            stock.setLastUpdated(LocalDateTime.now());
            sp500StockRepository.save(stock);
        }
    }

    /**
     * 대량의 S&P 500 데이터를 저장하거나 업데이트합니다.
     * @param stocks S&P 500 데이터 리스트
     */
    public void saveOrUpdateBulkStocks(List<SP500Stock> stocks) {
        for (SP500Stock stock : stocks) {
            SP500Stock existingStock = sp500StockRepository.findByStockCode(stock.getStockCode()).orElse(null);
            if (existingStock != null) {
                // 기존 데이터 업데이트
                existingStock.setCurrentPrice(stock.getCurrentPrice());
                existingStock.setLastUpdated(LocalDateTime.now());
                sp500StockRepository.save(existingStock);
            } else {
                // 새로운 데이터 삽입
                stock.setLastUpdated(LocalDateTime.now());
                sp500StockRepository.save(stock);
            }
        }
    }

    /**
     * S&P 500 주식 데이터를 삭제합니다.
     * @param stockCode 삭제할 주식 코드
     */
    public void deleteStock(String stockCode) {
        SP500Stock stock = sp500StockRepository.findByStockCode(stockCode).orElse(null);
        if (stock != null) {
            sp500StockRepository.delete(stock);
        }
    }
    public boolean isSP500Stock(String stockCode) {
        return sp500StockRepository.existsByStockCode(stockCode);
    }
}