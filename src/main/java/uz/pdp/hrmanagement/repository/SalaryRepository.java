package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.entity.Salary;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary,Integer> {

    // seeing the payment of the staff by userID and Month
    List<Salary> findByToUserIdAndMonthNumber(UUID toUser_id, Integer month);
}
