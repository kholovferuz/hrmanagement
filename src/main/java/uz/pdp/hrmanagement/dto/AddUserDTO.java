package uz.pdp.hrmanagement.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {
    @NotNull(message = "First name should not be empty")
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull(message = "Email should not be empty")
    @Email
    private String email;

    @NotNull(message = "Password should not be empty")
    private String password;

    @NotNull(message = "Role id should not be empty")
    private Set<Integer> rolesId;
}
