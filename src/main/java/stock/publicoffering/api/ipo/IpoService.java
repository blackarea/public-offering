package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stock.publicoffering.domain.ipo.Ipo;
import stock.publicoffering.domain.ipo.IpoRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class IpoService {

    @Value("${ipo.ipo_url}")
    private String IPO_URL;
    private final IpoRepository ipoRepository;

    @Transactional(readOnly = true)
    public List<Ipo> getIpoFirstPage() throws IOException {
        Document doc = Jsoup.connect(IPO_URL).get();
        Element tbody = doc.select("table[summary=공모주 청약일정] tbody").first();

        Elements rows = tbody.select("tr");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        List<Ipo> ipos = new ArrayList<>();

        for (Element row : rows) {
            Elements cols = row.select("td");

            String[] offeringPeriod = cols.get(1).text().split("~");
            String year = offeringPeriod[0].substring(0, 5);
            LocalDate offeringStartDate = LocalDate.parse(offeringPeriod[0], dateFormatter);
            LocalDate offeringEndDate = LocalDate.parse(year + offeringPeriod[1], dateFormatter);

            Ipo ipo = Ipo.builder()
                    .ipoName(cols.get(0).text())
                    .offeringStartDate(offeringStartDate)
                    .offeringEndDate(offeringEndDate)
                    .finalPrice(cols.get(2).text())
                    .expectedPrice(cols.get(3).text())
                    .competitionRate(cols.get(4).text())
                    .company(cols.get(5).text())
                    .link(cols.get(6).select("a").attr("href"))
                    .build();
            ipos.add(ipo);
        }
        return ipos;
    }

    public void saveIpo() throws IOException {
        List<Ipo> ipoFirstPage = getIpoFirstPage();
        ipoRepository.saveAll(ipoFirstPage);
    }

}
