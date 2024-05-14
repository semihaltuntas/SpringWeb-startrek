package be.vdab.startrek.werknemers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WerknemerService {
    private final WerknemerRepository werknemerRepository;

    public WerknemerService(WerknemerRepository werknemerRepository) {
        this.werknemerRepository = werknemerRepository;
    }
    public List<Werknemer> toonAlleWerknemers(){
        return werknemerRepository.toonAlleWerknemers();
    }
}
