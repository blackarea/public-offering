package stock.publicoffering.core.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import stock.publicoffering.api.ipo.IpoScheduler;

@RequiredArgsConstructor
@RestController
public class DiscordTestController {

    private final IpoScheduler ipoScheduler;

    @PostMapping("/discord")
    public void dt(){
        ipoScheduler.sendIpoAlert();
    }

}
