package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.Role;
import uz.pdp.hrmanagement.Entity.enums.RoleName;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);

}
