package be.vdab.startrek.bestellingen;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class BestellingenController {
    private final BestellingService bestellingService;

    public BestellingenController(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }
    @GetMapping("werknemers/{id}/bestellingen")
    List<Bestelling> findByWerknemerId(@PathVariable long id){
        return bestellingService.findByWerknemerId(id);
    }

    @PostMapping ("werknemers/{id}/bestellingen")
    void bestel(@PathVariable long id, @RequestBody @Valid NieuweBestelling nieuweBestelling){
        bestellingService.create(id,nieuweBestelling);
    }
}

