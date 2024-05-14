package be.vdab.startrek.bestellingen;

import be.vdab.startrek.werknemers.WerknemerNietGevondenException;
import be.vdab.startrek.werknemers.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final WerknemerRepository werknemerRepository;

    public BestellingService(BestellingRepository bestellingRepository, WerknemerRepository werknemerRepository) {
        this.bestellingRepository = bestellingRepository;
        this.werknemerRepository = werknemerRepository;
    }
    public List<Bestelling> findByWerknemerId(long werknemerId){
        return bestellingRepository.findByWerknemerId(werknemerId);
    }
    @Transactional
    void create(long werknemerId, NieuweBestelling nieuweBestelling){
        var werknemer = werknemerRepository.findByIdEnLock(werknemerId).orElseThrow(
                ()->new WerknemerNietGevondenException(werknemerId));
        Bestelling bestelling = new Bestelling(0,werknemerId, nieuweBestelling.omschrijving(),
                nieuweBestelling.bedrag(), LocalDateTime.now());
        bestellingRepository.create(bestelling);
        werknemer.bestel(nieuweBestelling.bedrag());
        werknemerRepository.updateBudget(werknemerId,werknemer.getBudget());
    }
}
