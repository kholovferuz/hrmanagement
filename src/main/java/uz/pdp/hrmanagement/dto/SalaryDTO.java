package uz.pdp.hrmanagement.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalaryDTO {

    private UUID toUserId;

    private Double monthlyAmount;

    private Integer monthNumber;

    private boolean isPaid;


}
