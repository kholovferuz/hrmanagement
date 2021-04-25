package uz.pdp.hrmanagement.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import uz.pdp.hrmanagement.entity.enums.RoleName;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
