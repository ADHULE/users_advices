package BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.ValidationDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Role;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.RoleType;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.User;
import BlackAdhuleSystem.dev.userAdvicesMariadb.mapper.UserMapper;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.RoleRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.UserRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.UserService;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.ValidationService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ValidationService validationService;

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
}
