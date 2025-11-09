package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;

import java.util.List;

public interface AdviceService {

    AdviceDto createAdvice(AdviceDto adviceDto);

    List<AdviceDto> getAllAdvices();

    AdviceDto getAdviceById(Long adviceId);

    AdviceDto updateAdvice(Long adviceId, AdviceDto adviceDto);

    void deleteAdvice(Long adviceId);

}
