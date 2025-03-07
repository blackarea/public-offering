package stock.publicoffering.api.ipo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import stock.publicoffering.domain.ipo.Ipo;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class IpoController {

    private final IpoService ipoService;

    @GetMapping("/ipo")
    public List<Ipo> getIpoFirstPage() throws IOException {
        return ipoService.getIpoPage(1);
    }

    @GetMapping("/ipo-save")
    public void saveIpo(@RequestParam(name = "page", defaultValue = "1") int page) throws IOException {
        ipoService.saveIpoPage(page);
    }

}
