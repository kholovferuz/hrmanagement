package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.TurniketHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, Integer> {

    List<TurniketHistory> findAllByTurniket_UserId(UUID turniket_user_id);

    TurniketHistory findByDateAndTurniketId(LocalDate date, Integer turniket_id);


    boolean existsByDate(LocalDate date);
}
