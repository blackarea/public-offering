package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stock.publicoffering.core.webhook.DiscordFeignClient;
import stock.publicoffering.core.webhook.DiscordMessage;
import stock.publicoffering.domain.ipo.Ipo;

import java.time.LocalDate;
import java.util.List;

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
            return;
        }

        StringBuilder discordMessageBuilder = new StringBuilder();
        for (Ipo ipo : matchedIpos) {
            discordMessageBuilder.append(String.format(
                    "공모주: [%s], 가격 : %s, 의무보유확약 : %s%%, 기관경쟁률 : %s, 주간사 : %s\n",
                    ipo.getIpoName(), ipo.getFinalPrice(), ipo.getMandatoryHoldingRatio(), ipo.getCompetitionRatingByInstitution(), ipo.getCompany()
            ));
        }

        //save matched ipos
        ipoService.saveIpos(matchedIpos);

        //alert to me
        discordFeignClient.sendMessage(new DiscordMessage(discordMessageBuilder.toString()));
    }

}
