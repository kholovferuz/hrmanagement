package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TurniketHistoryDTO {
    @NotNull(message = "Data about enter or exit should not be empty")
    private boolean enterOrExit;

    @NotNull(message = "TurniketId should not be empty")
    private Integer turniketId;
}
