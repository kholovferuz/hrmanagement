package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryDTO {
    @NotNull(message = "ToUserId should not be empty")
    private UUID toUserId;
    @NotNull(message = "Monthly amount should not be empty")
    private Double monthlyAmount;
    @NotNull(message = "Month number should not be empty")
    private Integer monthNumber;
    @NotNull(message = "Data where the salary paid or not should not be empty")
    private boolean isPaid;


}
