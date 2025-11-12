package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.UserDto;
import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.ValidationDto;

public interface ValidationService {
    ValidationDto saveValidation(UserDto userDto);
    String generateCode();
    ValidationDto findValidationByCode(String code);
}
