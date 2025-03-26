package stock.publicoffering.core.webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.publicoffering.api.ipo.IpoScheduler;

@RequiredArgsConstructor
@RestController
public class DiscordTestController {

    private final DiscordFeignClient discordFeignClient;

    @PostMapping("/discord")
    public void discordTest(@RequestParam("message") String message) {
        discordFeignClient.sendMessage(new DiscordMessage(message));
    }

}
