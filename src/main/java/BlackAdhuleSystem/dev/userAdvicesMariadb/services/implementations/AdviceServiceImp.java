package BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;
import BlackAdhuleSystem.dev.userAdvicesMariadb.mapper.AdviceMapper;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.AdviceRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.AdviceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdviceServiceImp implements AdviceService {
    private  AdviceRepository adviceRepository;

    @Override
    public AdviceDto createAdvice(AdviceDto adviceDto) {
        Advice advice= AdviceMapper.mapToAdvice(adviceDto);
        Advice savedAdvice= adviceRepository.save(advice);
        return AdviceMapper.mapToAdviceDto(savedAdvice);
    }
}
