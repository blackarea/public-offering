package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import stock.publicoffering.domain.ipo.Ipo;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class IpoScheduler {

    private final IpoService ipoService;

    /**
     * 설정한 조건과 맞는 공모주가 상장되었을 때 알림을 받는다.
     * cron : 매일 아침 9시
     */
    @Scheduled(cron = "0 0 9 * * *")
    public void sendIpoAlert() {
        //find matched ipos
        List<Ipo> matchedIpos = ipoService.getMatchedIpos(LocalDate.now());

        //alert to me
        //do something

        //save matched ipos
        ipoService.saveMatchedIpos(matchedIpos);
    }

}
