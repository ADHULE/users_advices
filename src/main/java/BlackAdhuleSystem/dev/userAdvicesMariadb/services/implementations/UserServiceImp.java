package BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.ValidationDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Role;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.RoleType;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.User;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Validation;
import BlackAdhuleSystem.dev.userAdvicesMariadb.mapper.UserMapper;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.RoleRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.UserRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.ValidationRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.UserService;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.ValidationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService ,UserDetailsService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;
    private final ValidationRepository validationRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        // 1. Vérifier si l'email existe déjà
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new RuntimeException("Votre adresse email est déjà utilisée !");
        }

        // 2. Encoder le mot de passe
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        // 3. Assigner le rôle USER par défaut
        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.USER);
                    return roleRepository.save(newRole);
                });

        // 4. Créer et sauvegarder l'utilisateur
        User user = UserMapper.mapToUser(userDto);
        user.setRole(role);
        user.setActif(false); // l'utilisateur n’est pas encore activé
        User savedUser = userRepository.save(user);

        logger.info("Nouvel utilisateur créé : {}", savedUser.getEmail());

        // 5. Convertir en DTO pour la validation
        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);

        try {
            // 6. Créer et envoyer le code d’activation
            this.validationService.saveValidation(savedUserDto);
            logger.info("Code d'activation envoyé à {}", savedUser.getEmail());
        } catch (Exception e) {
            logger.error("Erreur lors de l’envoi du code de validation à {} : {}", savedUser.getEmail(), e.getMessage());
        }

        return savedUserDto;
    }

    /**
     * @param activation
     */
    @Override
    public void activation(Map<String, String> activation) {
        String code = activation.get("code");

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Le code de validation est obligatoire !");
        }

        // Rechercher la validation par code
        Validation validation = (Validation) validationRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Code de validation invalide !"));

        // Vérifier si le code a expiré
        if (validation.getExpireTime().isBefore(Instant.now())) {
            throw new RuntimeException("Le code de validation a expiré !");
        }

        // Récupérer l'utilisateur lié à cette validation
        User user = validation.getUser();

        // Activer le compte utilisateur
        user.setActif(true);
        userRepository.save(user);
        logger.info("Compte activé avec succès pour l'utilisateur : {}", user.getEmail());
    }



    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setActif(dto.isActif());

        return UserMapper.mapToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * @param username 
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec email : " + username));

        // Ici on convertit ton User en UserDetails
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getRoleType().name()) // si tu utilises RoleType enum
                .disabled(!user.isActif())
                .build();
    }

}
