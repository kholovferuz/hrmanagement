package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TurniketHistoryDTO {
    @NotNull
    private boolean enterOrExit;

    @NotNull
    private Integer turniketId;
}
