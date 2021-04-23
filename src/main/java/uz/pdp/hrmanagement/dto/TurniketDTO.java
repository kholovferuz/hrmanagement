package uz.pdp.hrmanagement.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class TurniketDTO {
    @NotNull
    private UUID userId;
}
