package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.publicoffering.api.ipo.jsoup.JsoupIpoService;
import stock.publicoffering.domain.ipo.Ipo;
import stock.publicoffering.domain.ipo.IpoRepository;
import stock.publicoffering.domain.ipo.MyIpoCompany;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IpoService {

    @Value("${ipo.list_page_url}")
    private String LIST_PAGE_URL;
    @Value("${ipo.site_url}")
    private String SITE_URL;
    private static final int MANDATORY_HOLDING_RATIO = 5;
    private static final String DEMAND_FORECAST_RESULT = "수요예측결과";
    private static final String LISTING_DATE = "상장일";

    private final IpoRepository ipoRepository;
    private final JsoupIpoService jsoupIpoService;

    @Transactional
    public void saveIpos(List<Ipo> ipos) {
        ipoRepository.saveAll(ipos);
    }

    @Transactional(readOnly = true)
    public List<Ipo> getMatchedIposFromFirstPage(LocalDate localDate) {
        List<Ipo> matchedIpos = getIposFromSite(1);

        return matchedIpos.stream()
                .filter(ipo -> ipo.getOfferingStartDate().equals(localDate) &&
                        MyIpoCompany.contains(ipo.getCompany()) && isSatisfiedMandatoryHoldingRatio(ipo.getDetailLink()))
                .map(this::updateRatio)
                .map(this::updateListingDate)
                .toList();
    }

    public boolean isSatisfiedMandatoryHoldingRatio(String detailLink) {
        Element targetRow = jsoupIpoService.extractCategoryTr(SITE_URL + detailLink, DEMAND_FORECAST_RESULT);
        float ipoHoldingRatio = Float.parseFloat(targetRow.select("td:nth-child(2) table tr td:nth-child(4)").text().replace("%", ""));
        return ipoHoldingRatio > MANDATORY_HOLDING_RATIO;
    }

    private Ipo updateListingDate(Ipo ipo) {
        ipo.setListingDate(getListingDate(ipo.getDetailLink()));
        return ipo;
    }

    public String getListingDate(String detailLink) {
        Element targetRow = jsoupIpoService.extractCategoryTr(SITE_URL + detailLink, LISTING_DATE);
        Element dateCell = targetRow.select("td").get(1);
        return dateCell.text().trim();
    }

    public List<Ipo> getIposFromSite(int page) {
        Element ipoTbody = jsoupIpoService.getIpoTbody(LIST_PAGE_URL + "&page=" + Math.max(page, 1));
        Elements rows = ipoTbody.select("tr");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        List<Ipo> ipos = new ArrayList<>();

        rows.stream().map(row -> {
            Elements cols = row.select("td");

            String[] offeringPeriod = cols.get(1).text().split("~");
            String year = offeringPeriod[0].substring(0, 5);

            return Ipo.builder()
                    .ipoName(cols.get(0).text())
                    .offeringStartDate(LocalDate.parse(offeringPeriod[0], dateFormatter))
                    .offeringEndDate(LocalDate.parse(year + offeringPeriod[1], dateFormatter))
                    .finalPrice(cols.get(2).text())
                    .expectedPrice(cols.get(3).text())
                    .competitionRate(cols.get(4).text())
                    .company(cols.get(5).text())
                    .detailLink(cols.get(6).select("a").attr("href"))
                    .build();
        }).forEach(ipos::add);

        return ipos;
    }

    private Ipo updateRatio(Ipo ipo) {
        Element targetRow = jsoupIpoService.extractCategoryTr(SITE_URL + ipo.getDetailLink(), DEMAND_FORECAST_RESULT);
        String competitionRatio = targetRow.select("td:nth-child(2) table tr td:nth-child(2)").text();
        float ipoHoldingRatio = Float.parseFloat(targetRow.select("td:nth-child(2) table tr td:nth-child(4)").text().replace("%", ""));
        ipo.setCompetitionRateAndHoldingRatio(competitionRatio, ipoHoldingRatio);
        return ipo;
    }

}
