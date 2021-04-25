package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
    @NotNull
    @Email
    private String username;

    @NotNull
    private String password;
}
