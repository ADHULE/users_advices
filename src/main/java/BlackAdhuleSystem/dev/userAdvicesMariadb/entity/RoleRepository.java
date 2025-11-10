package BlackAdhuleSystem.dev.userAdvicesMariadb.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository <Role,Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
