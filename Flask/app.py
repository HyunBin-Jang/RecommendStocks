from flask import Flask, jsonify, request
import yfinance as yf
import pandas as pd
import requests
from datetime import datetime
import json
from crawling import fetch_superinvestors, fetch_portfolio

app = Flask(__name__)

# Spring Boot 서버 URL
SPRING_BOOT_API_URL = "http://localhost:8080"


def load_json(file_path):
    """
    JSON 파일을 읽어 딕셔너리로 반환.
    """
    with open(file_path, 'r', encoding='utf-8') as file:
        return json.load(file)

def get_stock_name_by_ticker(ticker, json_data):
    """
    티커를 사용해 주식의 한국 이름을 조회.
    """
    return json_data.get(ticker, "알 수 없음")

def fetch_sp500_tickers():
    """
    S&P 500 종목의 티커 리스트
    """
    url = "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies"
    sp500_table = pd.read_html(url)[0]
    tickers = sp500_table['Symbol'].tolist()
    tickers = [ticker.replace('.', '-') for ticker in tickers]  # Yahoo Finance 형식 변환
    return tickers

def get_sp500_data():
    """
    S&P 500 데이터를 yfinance로 가져오기
    """
    tickers = fetch_sp500_tickers()
    data = yf.download(tickers, group_by='ticker', period='1d', threads=True)

    # JSON 데이터 로드
    json_file_path = 'sp500_stocks.json'
    stock_name = load_json(json_file_path)

    # 데이터 정리
    stock_data = []
    for ticker in tickers:
        try:
            if ticker in data.columns.levels[0]: # 유효한 데이터만 처리
                stock_data.append({
                    "stockCode": ticker,
                    "stockName": get_stock_name_by_ticker(ticker, stock_name),
                    "currentPrice": data[ticker]['Close'][-1],
                    "lastUpdated": datetime.now().isoformat()  # ISO 8601 형식
                })
        except KeyError:
            print(f"데이터 없음: {ticker}")
        except Exception as e:
            print(f"오류 발생: {ticker}, {e}")

    return stock_data

@app.route('/update-sp500', methods=['POST'])
def update_sp500():
    """
    Spring Boot의 요청으로 S&P 500 데이터를 업데이트하고 전달
    """
    try:
        # S&P 500 데이터 가져오기
        stock_data = get_sp500_data()

        # 데이터를 Spring Boot로 전송
        response = requests.post(SPRING_BOOT_API_URL + "/api/sp500/bulk-update", json=stock_data)

        if response.status_code == 200:
            return jsonify({"message": "S&P 500 데이터 업데이트 완료"})
        else:
            return jsonify({"error": "Spring Boot 업데이트 실패", "details": response.text}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/update-superinvestor', methods=['POST'])
def update_superinvestor():
    try:
        superinvestors = fetch_superinvestors()
        portfolios = []
        for investor in superinvestors:
            portfolio = fetch_portfolio(investor["link"])
            portfolios.append({
                "name" : investor["name"],
                "stocks" : portfolio
            })
        response = requests.post(SPRING_BOOT_API_URL + "/api/superinvestor", json=portfolios)

        if response.status_code == 200:
            return jsonify({"message": "데이터 업데이트 완료"})
        else:
            return jsonify({"error": "업데이트 실패", "details": response.text}), 500
    except Exception as e:
        return jsonify({"error": str(e)}), 500
if __name__ == "__main__":
    app.run(port=5000, debug=True)