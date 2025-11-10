package BlackAdhuleSystem.dev.userAdvicesMariadb.services.implementations;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Role;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.RoleRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.RoleType;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.User;
import BlackAdhuleSystem.dev.userAdvicesMariadb.mapper.UserMapper;

import BlackAdhuleSystem.dev.userAdvicesMariadb.repository.UserRepository;
import BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
   private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        // Vérification de l'adresse email
        if (!userDto.getEmail().contains("@") || !userDto.getEmail().contains(".")) {
            throw new RuntimeException("Your email address is not valid");
        }

        // Vérifier si l'utilisateur existe déjà
        Optional<User> existingUser = this.userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Your email address is already used");
        }

        // Crypter le mot de passe
        String cryptedPassword = this.passwordEncoder.encode(userDto.getPassword());

        // Mapper DTO -> Entité
        User user = UserMapper.mapToUser(userDto);
        user.setPassword(cryptedPassword);

        // 🔹 Assigner un rôle par défaut
        Role userRole = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.USER);
                    return roleRepository.save(newRole);
                });

        user.setRole(userRole);

        // 🔹 Sauvegarder l’utilisateur
        User userSaved = userRepository.save(user);

        // 🔹 Retourner DTO
        return UserMapper.mapToUserDto(userSaved);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setActif(userDto.isActif());

        User updated = userRepository.save(existingUser);
        return UserMapper.mapToUserDto(updated);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
