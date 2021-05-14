package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDTO {
    @NotNull(message = "Username should not be empty")
    @Email
    private String username;

    @NotNull(message = "Password should not be empty")
    private String password;
}
