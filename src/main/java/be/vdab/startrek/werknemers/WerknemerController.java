package be.vdab.startrek.werknemers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
public class WerknemerController {
    private final WerknemerService werknemerService;

    public WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }
//    private record VoornaamFamilienaam(String voornaam,String familienaam){
//        VoornaamFamilienaam(Werknemer werknemer){
//            this(werknemer.getVoornaam(), werknemer.getFamilienaam());
//        }
//    }
    @GetMapping("werknemers")
    List<Werknemer> toonAlleWerknemers(){
        return werknemerService.toonAlleWerknemers();
    }
}
