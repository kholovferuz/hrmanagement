package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);


}
