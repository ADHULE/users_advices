package BlackAdhuleSystem.dev.userAdvicesMariadb.controller;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AdviceDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.AdviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/advices")
public class AdviceController {

    private AdviceService adviceService;

    /**
     * Crée un nouveau conseil.
     *
     * @param adviceDto données du conseil à créer.
     * @return le conseil créé avec le statut HTTP 201.
     */
    @PostMapping
    public ResponseEntity<AdviceDto> serviceCreateApi(@RequestBody AdviceDto adviceDto) {
        AdviceDto savedAdvice = adviceService.createAdvice(adviceDto);
        return new ResponseEntity<>(savedAdvice, HttpStatus.CREATED);
    }

    /**
     * Récupère tous les conseils disponibles.
     *
     * @return liste des conseils avec le statut HTTP 200.
     */
    @GetMapping("all")
    public ResponseEntity<List<AdviceDto>> getAllAdvicesApi() {
        List<AdviceDto> advices = adviceService.getAllAdvices();
        return new ResponseEntity<>(advices, HttpStatus.OK);
    }

    /**
     * Récupère un conseil par son identifiant.
     *
     * @param adviceId identifiant du conseil.
     * @return le conseil trouvé ou statut 404 si absent.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdviceDto> getAdviceByIdApi(@PathVariable("id") Long adviceId) {
        AdviceDto advice = adviceService.getAdviceById(adviceId);
        if (advice != null) {
            return new ResponseEntity<>(advice, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Met à jour un conseil existant.
     *
     * @param adviceId  identifiant du conseil à modifier.
     * @param adviceDto nouvelles données du conseil.
     * @return le conseil mis à jour ou statut 404 si absent.
     */
    @PutMapping("update/{id}")
    public ResponseEntity<AdviceDto> updateAdviceApi(@PathVariable("id") Long adviceId,
                                                     @RequestBody AdviceDto adviceDto) {
        AdviceDto updatedAdvice = adviceService.updateAdvice(adviceId, adviceDto);
        if (updatedAdvice != null) {
            return new ResponseEntity<>(updatedAdvice, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Supprime un conseil par son identifiant.
     *
     * @param adviceId identifiant du conseil à supprimer.
     * @return statut HTTP 204 si suppression réussie.
     */
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteAdviceApi(@PathVariable("id") Long adviceId) {
        adviceService.deleteAdvice(adviceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
