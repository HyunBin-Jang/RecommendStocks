package com.shareportfolio.antplanet.controller;

import com.shareportfolio.antplanet.domain.SP500Stock;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shareportfolio.antplanet.service.SP500Service;

import java.util.List;

@RestController
@RequestMapping("/api/sp500")
public class SP500Controller {

    private final SP500Service sp500Service;

    public SP500Controller(SP500Service sp500Service) {
        this.sp500Service = sp500Service;
    }

    /**
     * 모든 S&P 500 주식 정보를 반환합니다.
     * @return S&P 500 주식 정보 리스트
     */
    @GetMapping
    public List<SP500Stock> getAllStocks() {
        return sp500Service.getAllStocks();
    }

    @PostMapping("/bulk-update")
    public ResponseEntity<String> bulkUpdateStocks(@RequestBody List<SP500Stock> stocks) {
        sp500Service.saveOrUpdateBulkStocks(stocks);
        return ResponseEntity.ok("Bulk stock data updated successfully.");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStock(@RequestBody SP500Stock stockData) {
        try {
            sp500Service.saveOrUpdateStock(stockData);
            return ResponseEntity.ok("Stock data updated successfully for: " + stockData.getStockCode());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update stock data: " + e.getMessage());
        }
    }
    /**
     * 특정 주식 코드를 기준으로 주식 정보를 반환합니다.
     * @param stockCode 주식 코드
     * @return 주식 정보
     */
    @GetMapping("/{stockCode}")
    public ResponseEntity<SP500Stock> getStockByCode(@PathVariable String stockCode) {
        SP500Stock stock = sp500Service.getStockByCode(stockCode);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * S&P 500 주식 데이터를 저장하거나 업데이트합니다.
     * @param stock 주식 데이터
     * @return 상태 메시지
     */
    @PostMapping
    public ResponseEntity<String> saveOrUpdateStock(@RequestBody SP500Stock stock) {
        sp500Service.saveOrUpdateStock(stock);
        return ResponseEntity.ok("Stock data saved or updated successfully.");
    }

    /**
     * 특정 주식 코드를 기준으로 주식 데이터를 삭제합니다.
     * @param stockCode 주식 코드
     * @return 상태 메시지
     */
    @DeleteMapping("/{stockCode}")
    public ResponseEntity<String> deleteStock(@PathVariable String stockCode) {
        sp500Service.deleteStock(stockCode);
        return ResponseEntity.ok("Stock data deleted successfully.");
    }
}