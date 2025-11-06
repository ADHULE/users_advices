package BlackAdhuleSystem.dev.userAdvicesMariadb.controller;

import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.AdviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/advice")
public class AdviceController {
//    injection de service
    private  final AdviceService adviceService;

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public void create(@RequestBody Advice advice){
        this.adviceService.adviceCreate(advice);
    }
}
