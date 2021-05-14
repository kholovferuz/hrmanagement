package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.TaskDTO;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.TaskService;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class TaskController {

    final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // director or managers send the task
    @PostMapping("/attachTask")
    public HttpEntity<?> attachTasks(@Valid @RequestBody TaskDTO taskDTO) {
        Response attachTask = taskService.attachTask(taskDTO);
        return ResponseEntity.status(attachTask.isSuccess() ? 201 : 409).body(attachTask);
    }

    @GetMapping("/doTheTask")
    public HttpEntity<?> doTheTask(@RequestParam String email, @RequestParam Integer taskId) {
        Response verifyEmail = taskService.doTheTask(taskId, email);
        return ResponseEntity.status(verifyEmail.isSuccess() ? 200 : 409).body(verifyEmail);
    }

    // completing the task
    @PostMapping("/completeTheTask/{id}")
    public HttpEntity<?> completeTask(@PathVariable Integer id) {
        Response taskCompleted = taskService.taskCompleted(id);
        return ResponseEntity.status(taskCompleted.isSuccess() ? 200 : 409).body(taskCompleted);
    }
}
