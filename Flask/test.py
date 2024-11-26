import requests
from bs4 import BeautifulSoup


def fetch_superinvestors():
    """
    Dataroma에서 저명한 투자자 목록과 포트폴리오 링크를 가져옵니다.

    Returns:
        list: 저명한 투자자들의 이름과 포트폴리오 링크 리스트
    """
    url = "https://www.dataroma.com/m/home.php"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    response = requests.get(url, headers=headers)
    response.raise_for_status()

    soup = BeautifulSoup(response.content, "html.parser")
    span = soup.find("span", {"id": "port_body"})

    investors = []
    if span:
        rows = span.find_all("ul")
        for row in rows:
            cols = row.find_all("li")
            for col in cols:
                if len(col) >= 1:
                    # 투자자 이름과 링크 추출
                    a_tag = col.find("a", href=True)  # href 속성이 있는 <a> 태그 찾기
                    if a_tag:
                        href = a_tag["href"]  # href 속성 값
                        full_link = f"https://www.dataroma.com{href}"  # 절대 경로로 변환

                        text_parts = a_tag.contents  # <a> 태그의 자식 요소 리스트
                        main_text = ""
                        for part in text_parts:
                            if part.name == "span":  # <span> 태그에 도달하면 중지
                                break
                            if isinstance(part, str):  # 텍스트 노드만 추가
                                main_text += part.strip()

                        investors.append({"name": main_text, "link": full_link})
    print(investors)
    return investors


def fetch_portfolio(link):
    """
    투자자의 포트폴리오 페이지에서 주식 종목 데이터를 가져옵니다.

    Args:
        link (str): 투자자 포트폴리오 페이지 링크

    Returns:
        list: 투자자가 보유한 주식 종목 데이터 리스트
    """
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    response = requests.get(link, headers=headers)
    response.raise_for_status()

    soup = BeautifulSoup(response.content, "html.parser")
    table = soup.find("table", {"id": "grid"})
    stocks = []
    if table:
        rows = table.find_all("tr")[1:]
        for row in rows:
            cols = row.find_all("td")
            a_tag = cols[1].find("a", href=True)
            text_parts = a_tag.contents  # <a> 태그의 자식 요소 리스트
            stock_code = ""
            for part in text_parts:
                if part.name == "span":  # <span> 태그에 도달하면 중지
                    break
                if isinstance(part, str):  # 텍스트 노드만 추가
                    stock_code += part.strip()
            weight = cols[2].text.strip()
            shares = cols[4].text.strip()
            stocks.append({"stockCode": stock_code, "weight": weight, "shares": shares})
    return stocks


# Main Execution
if __name__ == "__main__":
    try:
        # 1. 저명한 투자자 목록 가져오기
        superinvestors = fetch_superinvestors()
        print(f"Fetched {len(superinvestors)} superinvestors.")

        # 2. 각 투자자의 포트폴리오 크롤링
        for investor in superinvestors[:5]:  # 테스트: 상위 5명만
            print(f"Fetching portfolio for {investor['name']}...")
            portfolio = fetch_portfolio(investor["link"])
            print(f"Found {len(portfolio)} stocks for {investor['name']}.")
            for stock in portfolio[:5]:  # 상위 5개 주식만 출력
                print(stock)
    except Exception as e:
        print(f"An error occurred: {e}")