package uz.pdp.hrmanagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class TurniketDTO {
    @NotNull
    private UUID userId;
}
