package BlackAdhuleSystem.dev.userAdvicesMariadb.dto;

import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private RoleType roleType;
}
