package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hrmanagement.entity.TurniketHistory;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TurniketHistoryRepository extends JpaRepository<TurniketHistory, Integer> {

    List<TurniketHistory> findAllByTurniket_UserId(UUID turniket_user_id);

    @Query(value = "select * from turniket_history join turniket t on t.id = turniket_history.turniket_id where (t.user_id= :userId and turniket_history.date= :date)", nativeQuery = true)
    List<TurniketHistory> findByDateAndUserId(LocalDate date, UUID userId);

    boolean existsByDate(LocalDate date);
}
