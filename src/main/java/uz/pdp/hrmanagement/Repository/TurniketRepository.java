package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.Turniket;

import java.util.Optional;

public interface TurniketRepository extends JpaRepository<Turniket, Integer> {
}
