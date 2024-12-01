package com.shareportfolio.antplanet.service;

import com.shareportfolio.antplanet.domain.SP500Stock;
import com.shareportfolio.antplanet.domain.SuperInvestor;
import com.shareportfolio.antplanet.repository.SuperInvestorRepository;
import com.shareportfolio.antplanet.repository.UserStockRepository;
import com.shareportfolio.antplanet.dto.RecommendedStockDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioRecommendationService {

    private final UserStockRepository userStockRepository;
    private final SuperInvestorRepository superInvestorRepository;

    public PortfolioRecommendationService(UserStockRepository userStockRepository,
                                          SuperInvestorRepository superInvestorRepository) {
        this.userStockRepository = userStockRepository;
        this.superInvestorRepository = superInvestorRepository;
    }

    public List<RecommendedStockDto> recommendStocks(Long portfolioId) {
        // 1. User의 포트폴리오 주식 비중 가져오기
        List<Object[]> userWeightsData = userStockRepository.calculateWeightsByPortfolioId(portfolioId);
        Map<String, Double> userWeightMap = userWeightsData.stream()
                .collect(Collectors.toMap(
                        data -> (String) data[0],  // SP500Stock의 stockCode
                        data -> (Double) data[1]  // 비중
                ));

        // 2. SuperInvestor 데이터 가져오기
        List<SuperInvestor> superInvestors = superInvestorRepository.findAll();

        // SuperInvestor 포트폴리오별로 유사도 계산
        Map<SuperInvestor, Double> similarityScores = new HashMap<>();
        for (SuperInvestor superInvestor : superInvestors) {
            Map<String, Double> investorWeightMap = superInvestor.getStocks().stream()
                    .collect(Collectors.toMap(
                            stock -> stock.getSp500Stock().getStockCode(),
                            stock -> stock.getWeight()
                    ));

            double similarity = calculatePortfolioSimilarity(userWeightMap, investorWeightMap);
            similarityScores.put(superInvestor, similarity);
        }

        // 3. 유사도가 높은 순으로 SuperInvestor 정렬
        List<Map.Entry<SuperInvestor, Double>> sortedInvestors = similarityScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .toList();

        // 4. 추천 주식 추출
        Set<String> userOwnedStockCodes = userWeightMap.keySet();
        List<RecommendedStockDto> recommendedStocks = new ArrayList<>();

        for (Map.Entry<SuperInvestor, Double> entry : sortedInvestors) {
            SuperInvestor investor = entry.getKey();
            double similarity = entry.getValue();

            for (var stock : investor.getStocks()) {
                String stockCode = stock.getSp500Stock().getStockCode();
                if (!userOwnedStockCodes.contains(stockCode)) {
                    recommendedStocks.add(new RecommendedStockDto(
                            stock.getSp500Stock(),
                            investor.getName(),
                            similarity
                    ));
                }
            }
        }
        // 유사도에 따라 추천 리스트 정렬 및 10개로 제한
        return recommendedStocks.stream()
                .sorted(Comparator.comparing(RecommendedStockDto::getSimilarity).reversed())
                .limit(10)
                .toList();
    }
    private double calculatePortfolioSimilarity(Map<String, Double> userWeights, Map<String, Double> investorWeights) {
        double overlapSimilarity = 0.0;
        double weightSimilarity = 0.0;
        double dotProduct = 0.0;
        double userMagnitude = 0.0;
        double investorMagnitude = 0.0;

        // 공통 종목 및 전체 종목
        Set<String> allStockCodes = new HashSet<>();
        allStockCodes.addAll(userWeights.keySet());
        allStockCodes.addAll(investorWeights.keySet());

        Set<String> commonStockCodes = new HashSet<>(userWeights.keySet());
        commonStockCodes.retainAll(investorWeights.keySet());

        // Overlap Similarity
        overlapSimilarity = (double) commonStockCodes.size() / allStockCodes.size();

        // Weight Similarity 계산 (코사인 유사도)
        for (String stockCode : commonStockCodes) {
            double userWeight = userWeights.get(stockCode);
            double investorWeight = investorWeights.get(stockCode);

            // 내적 계산
            dotProduct += userWeight * investorWeight;

            // 벡터 크기 계산
            userMagnitude += Math.pow(userWeight, 2);
            investorMagnitude += Math.pow(investorWeight, 2);
        }

        userMagnitude = Math.sqrt(userMagnitude);
        investorMagnitude = Math.sqrt(investorMagnitude);

        // 코사인 유사도 계산
        if (userMagnitude > 0 && investorMagnitude > 0) {
            weightSimilarity = dotProduct / (userMagnitude * investorMagnitude);
        }

        // 최종 유사도 계산
        double alpha = 0.5; // 주식 종목 구성 유사도 가중치
        double beta = 0.5;  // 비중 유사도 가중치

        return alpha * overlapSimilarity + beta * weightSimilarity;
    }
}
