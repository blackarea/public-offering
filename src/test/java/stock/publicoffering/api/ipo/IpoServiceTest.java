package stock.publicoffering.api.ipo;

import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import stock.publicoffering.api.jsoup.JsoupService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class IpoServiceTest {

    @MockitoBean
    protected JsoupService jsoupService;

    @Autowired
    private IpoService ipoService;

    @Test
    @DisplayName("오늘 상장되는 공모주 중 내 조건과 맞는 공모주들을 조회한다.")
    void getTodayMatchedIpos() {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("공모주가 내 조건과 맞는지 확인한다.")
    void isSatisfiedMandatoryHoldingRatio() {
        // given
        String html = "<tr>\n" +
                "         <td align=\"center\" bgcolor=\"#F1F4F7\">수요예측결과</td>\n" +
                "         <td colspan=\"4\" bgcolor=\"#FFFFFF\" align=\"left\">\n" +
                "          <table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\" bgcolor=\"#C2D2DF\">\n" +
                "           <tbody>\n" +
                "            <tr>\n" +
                "             <td width=\"22%\" bgcolor=\"#F5F5F2\" align=\"center\"><font color=\"#000000\">기관경쟁률</font>&nbsp;</td>\n" +
                "             <td width=\"20%\" bgcolor=\"FFFFFF\" height=\"27\" align=\"center\">951.52:1</td>\n" +
                "             <td width=\"20%\" bgcolor=\"#F5F5F2\" align=\"center\"><font color=\"#000000\">의무보유확약</font>&nbsp;</td>\n" +
                "             <td width=\"38%\" bgcolor=\"FFFFFF\" align=\"center\">19.74%</td>\n" +
                "            </tr>\n" +
                "           </tbody>\n" +
                "          </table></td>\n" +
                "        </tr>";

        Element successElement = new Element(Tag.valueOf("tr"), "").html(html);
        Element failElement = new Element(Tag.valueOf("tr"), "").html(html.replace("19.74%", "4.9"));

        // when, then
        // success
        when(jsoupService.getDemandForeCastResultRow(any(String.class))).thenReturn(successElement);
        boolean satisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(satisfiedMandatoryHoldingRatio).isTrue();

        // fail
        when(jsoupService.getDemandForeCastResultRow(any(String.class))).thenReturn(failElement);
        boolean unsatisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(unsatisfiedMandatoryHoldingRatio).isFalse();
    }

}