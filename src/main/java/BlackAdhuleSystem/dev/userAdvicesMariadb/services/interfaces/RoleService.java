package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.RoleDto;
import java.util.List;

/**
 * Interface du service rôle.
 * Définit les opérations sur les rôles.
 */
public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    List<RoleDto> getRoles();
    RoleDto getRoleById(Long id);
}
