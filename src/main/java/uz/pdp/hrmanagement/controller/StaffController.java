package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.SalaryDTO;
import uz.pdp.hrmanagement.entity.TurniketHistory;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.StaffService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class StaffController {

    final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/allEmployees")
    public HttpEntity<?> readAllEmployees() {
        Response allEmployees = staffService.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/byTurniket/{id}")
    public HttpEntity<?> employeesByTurniket(@PathVariable UUID id) {
        Response byTurniket = staffService.getOneEmployeeByTurniket(id);
        return ResponseEntity.status(byTurniket.isSuccess() ? 200 : 409).body(byTurniket);
    }

    @GetMapping("/allTurniket")
    public HttpEntity<?> getAllTurniket() {
        Response allEmployees = staffService.getAllTurniketByUsers();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/byTaskStatus/{id}")
    public HttpEntity<?> employeesByTaskStatus(@PathVariable UUID id) {
        Response completedTask = staffService.getEmployeesByCompletedTask(id);
        return ResponseEntity.status(completedTask.isSuccess() ? 200 : 409).body(completedTask);
    }

    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@Valid @RequestBody SalaryDTO salaryDTO) {
        Response salary = staffService.paySalaryToTheStaff(salaryDTO);
        return ResponseEntity.status(salary.isSuccess() ? 200 : 409).body(salary);
    }

    @GetMapping("/bySalaryAndMonth/{id}")
    public HttpEntity<?> bySalaryAndMonth(@PathVariable UUID id, @Valid @RequestBody SalaryDTO salaryDTO) {
        Response userIdAndMonth = staffService.getSalariesByUserIdAndMonth(id, salaryDTO);
        return ResponseEntity.status(userIdAndMonth.isSuccess() ? 200 : 409).body(userIdAndMonth);
    }

    @GetMapping("/tasksAndUsers")
    public HttpEntity<?> tasksAndUsers() {
        Response tasksAndUsers = staffService.getTasksAndUsers();
        return ResponseEntity.ok(tasksAndUsers);
    }

    @GetMapping("/getTurniketHistoryByDate/{id}")
    public HttpEntity<?> getTurniketHistoryByDate(@Valid @RequestBody TurniketHistory turniketHistory, @PathVariable UUID id) {
        Response turniketHistoryByDate = staffService.getTurniketHistoryByDate(turniketHistory, id);
        return ResponseEntity.status(turniketHistoryByDate.isSuccess() ? 201 : 409).body(turniketHistoryByDate);
    }

    // EXEPTION HANDLER
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> mistakes = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            mistakes.put(fieldName, errorMessage);
        });
        return mistakes;
    }
}
