package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.Salary;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {

    // oy va xodim buyicha xodimlarning ish haqi olganligini ko'rish
    List<Salary> findByToUserIdAndMonthNumber(UUID toUser_id, Integer month);
}
