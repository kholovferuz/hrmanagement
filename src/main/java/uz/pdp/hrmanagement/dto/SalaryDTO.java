package uz.pdp.hrmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SalaryDTO {

    private UUID toUserId;

    private Double monthlyAmount;

    private Integer monthNumber;

    private boolean isPaid;


}
