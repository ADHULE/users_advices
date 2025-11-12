package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService {
     UserDto createUser(UserDto userDto);
     void activation(Map<String,String> activation);
     List<UserDto> getUsers();
     UserDto getUserById(Long userId);
     UserDto updateUser(Long userId, UserDto userDto);
     void deleteUser(Long userId);

}
