package stock.publicoffering.core.webhook;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${discord.name}", url = "${discord.webhook_url}")
public interface DiscordFeignClient {
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    void sendMessage(@RequestBody DiscordMessage discordMessage);

}