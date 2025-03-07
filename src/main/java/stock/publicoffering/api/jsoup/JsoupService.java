package stock.publicoffering.api.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupService {

    public Document getIpoDocument(String detailLink) {
        Document doc;
        try {
            doc = Jsoup.connect(detailLink).get();
        } catch (IOException e) {
            throw new RuntimeException("공모주 document를 가져오는데 실패했습니다.");
        }
        return doc;
    }

    /**
     * 청약일정결과에서 "수요예측결과"를 포함한 tr을 찾아 반환한다.
     */
    public Element getDemandForeCastResultRow(String detailLink) {
        Document doc = getIpoDocument(detailLink);
        Element tbody = doc.select("table[summary=공모청약일정] tbody").first();
        return tbody.select("tr").stream()
                .filter(row -> row.select("td").stream().anyMatch(td -> td.text().contains("수요예측결과")))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("청약일정에서 수요예측결과를 포함한 tr을 찾을 수 없습니다."));
    }
}
