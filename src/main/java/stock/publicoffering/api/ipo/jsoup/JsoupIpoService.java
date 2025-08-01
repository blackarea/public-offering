package stock.publicoffering.api.ipo.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JsoupIpoService {

    public Document getIpoDocument(String link) {
        Document doc;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            throw new RuntimeException("공모주 document를 가져오는데 실패했습니다.");
        }
        return doc;
    }

    public Element getIpoTbody(String link) {
        return getIpoDocument(link).select("table[summary=공모주 청약일정] tbody").first();
    }

    /**
     * 청약 일정에서 category를 포함한 tr을 찾아 반환한다.
     */
    public Element extractCategoryTr(String detailLink, String category) {
        Document doc = getIpoDocument(detailLink);
        Element tbody = doc.select("table[summary=공모청약일정] tbody").first();
        return tbody.select("tr").stream()
                .filter(row -> row.select("td").stream().anyMatch(td -> td.text().contains(category)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("청약일정에서 "+ category +"를 포함한 tr을 찾을 수 없습니다."));
    }
}
