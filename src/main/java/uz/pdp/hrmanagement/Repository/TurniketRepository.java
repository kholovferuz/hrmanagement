package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.Turniket;

import java.util.UUID;


public interface TurniketRepository extends JpaRepository<Turniket, Integer> {
    boolean existsByUserId(UUID user_id);
}
