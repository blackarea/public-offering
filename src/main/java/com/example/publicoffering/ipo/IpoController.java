package com.example.publicoffering.ipo;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class IpoController {

    @GetMapping("/test")
    public void getAll() throws IOException {
        String url = "https://www.38.co.kr/html/fund/index.htm?o=k&page=1";

        Document doc = Jsoup.connect(url).get();
        Element tbody = doc.select("table[summary=공모주 청약일정] tbody").first();

        List<Ipo> ipoList = new ArrayList<>();

        Elements rows = tbody.select("tr");

        for (Element row : rows) {
            Elements cols = row.select("td");

            // 각 열의 값을 추출하여 DTO로 변환
            String ipoName = cols.get(0).text();
            String offeringPeriod = cols.get(1).text();
            String finalPrice = cols.get(2).text();
            String expectedPrice = cols.get(3).text();
            String company = cols.get(5).text();
            String link = cols.get(6).select("a").attr("href");

            Ipo ipo = new Ipo(ipoName, offeringPeriod, finalPrice, expectedPrice, company, link);
            ipoList.add(ipo);
        }
        for (Ipo ipo : ipoList) {
            System.out.println(ipo);
        }

    }
}
