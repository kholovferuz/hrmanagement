package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.entity.Turniket;

import java.util.UUID;


public interface TurniketRepository extends JpaRepository<Turniket, Integer> {
    boolean existsByUserId(UUID user_id);
}
