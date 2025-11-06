package BlackAdhuleSystem.dev.userAdvicesMariadb.services;

import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.AdviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor //gestionnaire de constructeurs avec Lombok
public class AdviceService {
//    injection de la repository dans le service
    private  final AdviceRepository adviceRepository;

//  créer un avis
    public  void adviceCreate(Advice advice){
        this.adviceRepository.save(advice);
    }
}
