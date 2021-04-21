package uz.pdp.hrmanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.Entity.Task;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Entity.enums.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByIdAndToUserEmail(Integer id, String user_email);

    List<Task> findAllByTaskStatusAndToUserId(TaskStatus taskStatus, UUID toUser_id);

}
