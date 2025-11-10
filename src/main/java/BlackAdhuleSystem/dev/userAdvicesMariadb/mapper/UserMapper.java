package BlackAdhuleSystem.dev.userAdvicesMariadb.mapper;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.RoleDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.User;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Role;

public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;

        RoleDto roleDto = RoleMapper.mapToRoleDto(user.getRole());

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.isActif(),
                roleDto
        );
    }

    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setActif(userDto.isActif());

        Role role = RoleMapper.mapToRole(userDto.getRoleDto());
        user.setRole(role);

        return user;
    }
}
