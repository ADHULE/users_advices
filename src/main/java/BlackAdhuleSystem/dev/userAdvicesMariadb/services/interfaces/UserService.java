package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;

import java.util.List;

public interface UserService {
     UserDto createUser(UserDto userDto);
     List<UserDto> getUsers();
     UserDto getUserById(Long userId);
     UserDto updateUser(Long userId, UserDto userDto);
     void deleteUser(Long userId);

}
