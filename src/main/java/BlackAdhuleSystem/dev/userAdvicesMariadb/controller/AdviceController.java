package BlackAdhuleSystem.dev.userAdvicesMariadb.controller;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.AdviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/advices")
public class AdviceController {
    private AdviceService adviceService;

    @PostMapping
    public ResponseEntity<AdviceDto> serviceCreateApi(@RequestBody AdviceDto adviceDto) {
        AdviceDto saveAdvice = adviceService.createAdvice(adviceDto);
        return new ResponseEntity<>(saveAdvice, HttpStatus.CREATED);
    }


}
