package BlackAdhuleSystem.dev.userAdvicesMariadb.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationDto {
    private Long id;
    private String code;
    private Instant creationTime;
    private Instant activationTime;
    private Instant expireTime;
    private UserDto userDto;
}
