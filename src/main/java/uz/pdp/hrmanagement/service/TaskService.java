package uz.pdp.hrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TaskDTO;

import java.util.UUID;

@Service
public interface TaskService {
    void sendTaskToWorker(Integer taskId, String email);

    Response doTheTask(Integer taskId, String email);

    Response attachTask(TaskDTO taskDTO);

    void sendEmailToTheManagers(String email, UUID id);

    Response taskCompleted(Integer id);
}
