package BlackAdhuleSystem.dev.userAdvicesMariadb.controller;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.AuthentificationDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.UserRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.security.JwtService;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping()
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;

    @PostMapping(path = "inscription")
    public ResponseEntity<UserDto> inscruption(@RequestBody UserDto userDto) {
        UserDto savedUser = this.userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @PostMapping(path = "activation")
    public ResponseEntity<String> activation(@RequestBody Map<String, String> activation) {
        userService.activation(activation);
        return ResponseEntity.ok("Compte activé avec succès !");
    }

    @PostMapping(path = "login")
    public Map<String, String> connexion(@RequestBody AuthentificationDto authentificationDto) {
        final Authentication authenticate = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authentificationDto.username(),
                        authentificationDto.password())
        );
//        generer le jeton si l'utilisateur est authentifié
        if (authenticate.isAuthenticated()) {
            return this.jwtService.jwtGenerate(authentificationDto.username());
        }
//        log.info("Resultat {}" + authenticate.isAuthenticated());
        return null;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    // Récupérer un utilisateur par son ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    //Mettre à jour un utilisateur
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        UserDto updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    //Supprimer un utilisateur
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Utilisateur supprimé avec succès !");
    }


}
