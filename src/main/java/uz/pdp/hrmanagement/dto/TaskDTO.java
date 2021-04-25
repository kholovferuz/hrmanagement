package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Timestamp deadline;
    @NotNull
    private UUID toUserId;
}
