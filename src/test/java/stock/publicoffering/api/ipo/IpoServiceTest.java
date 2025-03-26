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

        String iposHtml = getIposHtml();
        String ipoDetailHtml = getDemandForCastHtml();

        Element element = new Element(Tag.valueOf("tbody"), "").html(iposHtml);
        Element successElement = new Element(Tag.valueOf("tr"), "").html(ipoDetailHtml);
        // when
        when(jsoupIpoService.getIpoTbody(any(String.class))).thenReturn(element);
        when(jsoupIpoService.getDemandForeCastResultRow(any(String.class))).thenReturn(successElement);
        List<Ipo> matchedIpos = ipoService.getMatchedIposFromFirstPage(now);
        // then

        assertThat(matchedIpos).hasSize(1).extracting("ipoName").contains("더즌");
    }

    @Test
    @DisplayName("공모주가 내 조건과 맞는지 확인한다.")
    void isSatisfiedMandatoryHoldingRatio() {
        // given
        String html = getDemandForCastHtml();

        Element successElement = new Element(Tag.valueOf("tr"), "").html(html);
        Element failElement = new Element(Tag.valueOf("tr"), "").html(html.replace("19.74%", "4.9"));

        // when, then
        // success
        when(jsoupIpoService.getDemandForeCastResultRow(any(String.class))).thenReturn(successElement);
        boolean satisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(satisfiedMandatoryHoldingRatio).isTrue();

        // fail
        when(jsoupIpoService.getDemandForeCastResultRow(any(String.class))).thenReturn(failElement);
        boolean unsatisfiedMandatoryHoldingRatio = ipoService.isSatisfiedMandatoryHoldingRatio("");
        assertThat(unsatisfiedMandatoryHoldingRatio).isFalse();
    }

    private static String getIposHtml() {
        String html = " <tr bgcolor=\"#F8F8F8\">\n" +
                "  <td height=\"30\">&nbsp;<a href=\"/html/fund/?o=v&amp;no=2175&amp;l=&amp;page=1\"><font color=\"#0066CC\">나우로보틱스(구.나우테크닉스)</font></a></td>\n" +
                "  <td>2025.03.21~03.24</td>\n" +
                "  <td align=\"center\">-</td>\n" +
                "  <td align=\"center\">5,900~6,800</td>\n" +
                "  <td align=\"center\"></td>\n" +
                "  <td>대신증권,아이엠증권</td>\n" +
                "  <td align=\"center\"><a href=\"/html/fund/index.htm?o=v&amp;no=2175&amp;l=&amp;page=1\"><img alt=\"분석보기\" src=\"/images/2008/ipo/btn_bunsuk.gif\" width=\"23\" height=\"14\" border=\"0\"></a></td>\n" +
                " </tr>\n" +
                " <tr bgcolor=\"#FFFFFF\">\n" +
                "  <td height=\"30\">&nbsp;<a href=\"/html/fund/?o=v&amp;no=2165&amp;l=&amp;page=1\"><font color=\"#E3231E\">더즌</font></a></td>\n" +
                "  <td>2025.03.12~03.13</td>\n" +
                "  <td align=\"center\">9,000</td>\n" +
                "  <td align=\"center\">10,500~12,500</td>\n" +
                "  <td align=\"center\"></td>\n" +
                "  <td>한국투자증권</td>\n" +
                "  <td align=\"center\"><a href=\"/html/fund/index.htm?o=v&amp;no=2165&amp;l=&amp;page=1\"><img alt=\"분석보기\" src=\"/images/2008/ipo/btn_bunsuk.gif\" width=\"23\" height=\"14\" border=\"0\"></a></td>\n" +
                " </tr>\n" +
                " <tr bgcolor=\"#F8F8F8\">\n" +
                "  <td height=\"30\">&nbsp;<a href=\"/html/fund/?o=v&amp;no=2164&amp;l=&amp;page=1\"><font color=\"#333333\">티엑스알로보틱스</font></a></td>\n" +
                "  <td>2025.03.10~03.11</td>\n" +
                "  <td align=\"center\">13,500</td>\n" +
                "  <td align=\"center\">11,500~13,500</td>\n" +
                "  <td align=\"center\"></td>\n" +
                "  <td>NH투자증권,신한투자증권,유진투자증권</td>\n" +
                "  <td align=\"center\"><a href=\"/html/fund/index.htm?o=v&amp;no=2164&amp;l=&amp;page=1\"><img alt=\"분석보기\" src=\"/images/2008/ipo/btn_bunsuk.gif\" width=\"23\" height=\"14\" border=\"0\"></a></td>\n" +
                " </tr>";
        return html;
    }

    private static String getDemandForCastHtml() {
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
        return html;
    }

}