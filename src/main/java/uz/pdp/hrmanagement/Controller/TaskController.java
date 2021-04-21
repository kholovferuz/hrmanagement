package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.DTO.TaskDTO;
import uz.pdp.hrmanagement.Service.Response;
import uz.pdp.hrmanagement.Service.TaskService;



@RestController
@RequestMapping("/api/auth")
public class TaskController {
    @Autowired
    TaskService taskService;
    // direktor yoki menejerlar topshiriqni junatadi
    @PostMapping("/attachTask")
    public HttpEntity<?> attachTasks(@RequestBody TaskDTO taskDTO) {
        Response attachTask = taskService.attachTask(taskDTO);
        return ResponseEntity.status(attachTask.isSuccess() ? 201 : 409).body(attachTask);
    }

    @GetMapping("/doTheTask")
    public HttpEntity<?> doTheTask(@RequestParam String email, @RequestParam Integer taskId) {
        Response verifyEmail = taskService.doTheTask(taskId, email);
        return ResponseEntity.status(verifyEmail.isSuccess() ? 200 : 409).body(verifyEmail);
    }

    // buni faqat qaysi userga topshiriq berilgan bolsa usha qila oladi
    @PostMapping("/completeTheTask/{id}")
    public HttpEntity<?> completeTask(@PathVariable Integer id) {
        Response taskCompleted = taskService.taskCompleted(id);
        return ResponseEntity.status(taskCompleted.isSuccess() ? 200 : 409).body(taskCompleted);
    }
}
