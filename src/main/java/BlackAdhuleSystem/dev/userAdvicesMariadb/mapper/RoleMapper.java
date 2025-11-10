package BlackAdhuleSystem.dev.userAdvicesMariadb.mapper;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.RoleDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Role;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoleMapper {
    public static RoleDto mapToRoleDto(Role role) {
        if (role == null) return null;
        return new RoleDto(role.getId(), role.getRoleType());
    }

    public static Role mapToRole(RoleDto roleDto) {
        if (roleDto == null) return null;
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setRoleType(roleDto.getRoleType());
        return role;
    }
}
