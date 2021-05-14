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
    @NotNull(message = "Name should not be empty")
    private String name;
    @NotNull(message = "Description should not be empty")
    private String description;
    @NotNull(message = "Deadline should not be empty")
    private Timestamp deadline;
    @NotNull(message = "ToUserId should not be empty")
    private UUID toUserId;
}
