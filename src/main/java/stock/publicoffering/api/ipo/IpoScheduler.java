package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stock.publicoffering.core.webhook.DiscordFeignClient;
import stock.publicoffering.core.webhook.DiscordMessage;
import stock.publicoffering.domain.ipo.Ipo;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IpoScheduler {

    private final IpoService ipoService;
    private final DiscordFeignClient discordFeignClient;

    /**
     * 설정한 조건과 맞는 공모주가 상장되었을 때 알림을 받는다.
     * cron : 매일 아침 9시
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendIpoAlert() {
        //find matched ipos
        List<Ipo> matchedIpos = ipoService.getMatchedIposFromFirstPage(LocalDate.now());

        //ipo가 없으면 종료
        if (matchedIpos.isEmpty()) {
            log.info("Today Matched ipos: {}", "No matched ipos");
            return;
        }

        StringBuilder discordMessageBuilder = new StringBuilder();
        for (Ipo ipo : matchedIpos) {
            discordMessageBuilder.append(String.format(
                    "공모주: [%s], 가격 : %s, 의무보유확약 : %s%%, 공모 마감 날짜 : %s," +
                            " 기관경쟁률 : %s, 주간사 : %s, 상장일 :%s\n",
                    ipo.getIpoName(), ipo.getFinalPrice(), ipo.getMandatoryHoldingRatio(), ipo.getOfferingEndDate(),
                    ipo.getCompetitionRatingByInstitution(), ipo.getCompany(), ipo.getListingDate()
            ));
        }

        //save matched ipos
        ipoService.saveIpos(matchedIpos);
        log.info("Today Matched ipos: {}", discordMessageBuilder);

        //alert to me
        discordFeignClient.sendMessage(new DiscordMessage(discordMessageBuilder.toString()));
    }

}
