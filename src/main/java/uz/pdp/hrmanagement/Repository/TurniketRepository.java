package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hrmanagement.Entity.Turniket;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TurniketRepository extends JpaRepository<Turniket, Integer> {

    List<Turniket> findAllByUserId(UUID user_id);
}
