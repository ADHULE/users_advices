package BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;
import BlackAdhuleSystem.dev.userAdvicesMariadb.mapper.AdviceMapper;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.AdviceRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.AdviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdviceServiceImp implements AdviceService {

    private AdviceRepository adviceRepository;

    /**
     * Crée un nouveau conseil (Advice) à partir d'un DTO.
     *
     * @param adviceDto DTO contenant les données du conseil à créer.
     * @return AdviceDto représentant le conseil sauvegardé.
     */
    @Override
    public AdviceDto createAdvice(AdviceDto adviceDto) {
        Advice advice = AdviceMapper.mapToAdvice(adviceDto);
        Advice savedAdvice = adviceRepository.save(advice);
        return AdviceMapper.mapToAdviceDto(savedAdvice);
    }

    /**
     * Récupère tous les conseils enregistrés dans la base de données.
     *
     * @return Liste de AdviceDto représentant tous les conseils.
     */
    @Override
    public List<AdviceDto> getAllAdvices() {
        List<Advice> advices = adviceRepository.findAll();
        return advices.stream()
                .map(AdviceMapper::mapToAdviceDto)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un conseil spécifique par son identifiant.
     *
     * @param adviceId Identifiant du conseil.
     * @return AdviceDto correspondant au conseil trouvé, ou null si absent.
     */
    @Override
    public AdviceDto getAdviceById(Long adviceId) {
        Optional<Advice> adviceOptional = adviceRepository.findById(adviceId);
        return adviceOptional.map(AdviceMapper::mapToAdviceDto).orElse(null);
    }

    /**
     * Met à jour un conseil existant avec les nouvelles données fournies.
     *
     * @param adviceId  Identifiant du conseil à mettre à jour.
     * @param adviceDto Nouvelles données du conseil.
     * @return AdviceDto mis à jour, ou null si le conseil n'existe pas.
     */
    @Override
    public AdviceDto updateAdvice(Long adviceId, AdviceDto adviceDto) {
        Optional<Advice> adviceOptional = adviceRepository.findById(adviceId);
        if (adviceOptional.isPresent()) {
            Advice adviceToUpdate = adviceOptional.get();
            adviceToUpdate.setMessage(adviceDto.getMessage());
            adviceToUpdate.setStatus(adviceDto.getStatus());
            Advice updatedAdvice = adviceRepository.save(adviceToUpdate);
            return AdviceMapper.mapToAdviceDto(updatedAdvice);
        }
        return null;
    }

    /**
     * Supprime un conseil de la base de données par son identifiant.
     *
     * @param adviceId Identifiant du conseil à supprimer.
     */
    @Override
    public void deleteAdvice(Long adviceId) {
        adviceRepository.deleteById(adviceId);
    }
}
