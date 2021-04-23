package uz.pdp.hrmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurniketHistoryDTO {
    @NotNull
    private boolean enterOrExit;

    @NotNull
    private Integer turniketId;
}
