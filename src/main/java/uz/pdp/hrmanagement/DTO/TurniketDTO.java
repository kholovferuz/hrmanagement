package uz.pdp.hrmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurniketDTO {
    @NotNull
    private boolean enterOrExit;

    @NotNull
    private UUID userId;
}
