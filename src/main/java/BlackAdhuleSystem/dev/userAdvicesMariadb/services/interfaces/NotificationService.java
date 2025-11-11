package BlackAdhuleSystem.dev.userAdvicesMariadb.services.interfaces;

import BlackAdhuleSystem.dev.userAdvicesMariadb.dto.ValidationDto;

public interface NotificationService {
    ValidationDto sendNotification(ValidationDto validationDto);
}
