package stock.publicoffering.api.ipo;

import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import stock.publicoffering.api.ipo.jsoup.JsoupIpoService;
import stock.publicoffering.domain.ipo.Ipo;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class IpoServiceTest {

    @MockitoBean
    protected JsoupIpoService jsoupIpoService;

    @Autowired
    private IpoService ipoService;

    @Test
    @DisplayName("공모주 첫번째 페이지 중 내 조건과 맞는 공모주들을 조회한다.")
    void getTodayMatchedIpos() {
        // given
        LocalDate now = LocalDate.of(2025, 3, 12);

        String iposHtml = readHtml("ipos.html");
        String ipoDetailHtml = readHtml("demandForCast.html");

        Element element = new Element(Tag.valueOf("tbody"), "").html(iposHtml);
        Element successElement = new Element(Tag.valueOf("tr"), "").html(ipoDetailHtml);
        // when
        when(jsoupIpoService.getIpoTbody(any(String.class))).thenReturn(element);
        when(jsoupIpoService.extractCategoryTr(any(String.class), any(String.class))).thenReturn(successElement);
        List<Ipo> matchedIpos = ipoService.getMatchedIposFromFirstPage(now);
        // then

        assertThat(matchedIpos).hasSize(1).extracting("ipoName").contains("더즌");
    }

    @Test
    @DisplayName("공모주가 내 조건과 맞는지 확인한다.")
    void isSatisfiedMandatoryHoldingRatio() {
        // given
        String html = readHtml("demandForCast.html");

        Element successElement = new Element(Tag.valueOf("tr"), "").html(html);
        Element failElement = new Element(Tag.valueOf("tr"), "").html(html.replace("19.74%", "4.9"));

        // when, then
        // success
        when(jsoupIpoService.extractCategoryTr(any(String.class), any(String.class))).thenReturn(successElement);
        boolean satisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(satisfiedMandatoryHoldingRatio).isTrue();

        // fail
        when(jsoupIpoService.extractCategoryTr(any(String.class), any(String.class))).thenReturn(failElement);
        boolean unsatisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(unsatisfiedMandatoryHoldingRatio).isFalse();
    }

    @Test
    @DisplayName("상장일 정상적으로 불러오는지 테스트")
    void checkListingDate() {
        // given
        String html = readHtml("listingDate.html");
        Element element = new Element(Tag.valueOf("tr"), "").html(html);

        // when
        when(jsoupIpoService.extractCategoryTr(any(String.class), any(String.class))).thenReturn(element);
        String listingDate = ipoService.getListingDate("");

        // then
        assertThat(listingDate).isEqualTo("2025.08.07");
    }

    private String readHtml(String filename) {
        try {
            return new String(getClass()
                    .getClassLoader()
                    .getResourceAsStream("test-data/" + filename)
                    .readAllBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read test HTML file: " + filename, e);
        }
    }

}