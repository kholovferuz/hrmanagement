package uz.pdp.hrmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
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
