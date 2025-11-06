package BlackAdhuleSystem.dev.userAdvicesMariadb.repository;

import BlackAdhuleSystem.dev.userAdvicesMariadb.entity.Advice;
import jakarta.persistence.Id;
import org.springframework.data.repository.CrudRepository;

public interface AdviceRepository extends CrudRepository <Advice, Id>{
}
