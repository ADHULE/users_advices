package BlackAdhuleSystem.dev.userAdvicesMariadb.mapper;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;

public class AdviceMapper {

    public static AdviceDto mapToAdviceDto(Advice advice) {
        return new AdviceDto(
                advice.getId(),
                advice.getMessage(),
                advice.getStatus()
        );
    }

    public static Advice mapToAdvice(AdviceDto adviceDto) {
        Advice advice = new Advice();
        advice.setId(adviceDto.getId());
        advice.setMessage(adviceDto.getMessage());
        advice.setStatus(adviceDto.getStatus());
        return advice;
    }
}
