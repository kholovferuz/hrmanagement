package uz.pdp.hrmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.enums.TaskStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByIdAndToUserEmail(Integer id, String user_email);

    List<Task> findAllByTaskStatusAndToUserId(TaskStatus taskStatus, UUID toUser_id);

}
