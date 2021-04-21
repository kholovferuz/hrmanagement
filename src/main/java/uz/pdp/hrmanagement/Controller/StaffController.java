package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.DTO.SalaryDTO;
import uz.pdp.hrmanagement.Entity.TurniketHistory;
import uz.pdp.hrmanagement.Service.Response;
import uz.pdp.hrmanagement.Service.StaffService;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class StaffController {
    @Autowired
    StaffService staffService;

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
    public HttpEntity<?> paySalary(@RequestBody SalaryDTO salaryDTO) {
        Response salary = staffService.paySalaryToTheStaff(salaryDTO);
        return ResponseEntity.status(salary.isSuccess() ? 200 : 409).body(salary);
    }

    @GetMapping("/bySalaryAndMonth/{id}")
    public HttpEntity<?> bySalaryAndMonth(@PathVariable UUID id, @RequestBody SalaryDTO salaryDTO) {
        Response userIdAndMonth = staffService.getSalariesByUserIdAndMonth(id, salaryDTO);
        return ResponseEntity.status(userIdAndMonth.isSuccess() ? 200 : 409).body(userIdAndMonth);
    }

    @GetMapping("/tasksAndUsers")
    public HttpEntity<?> tasksAndUsers() {
        Response tasksAndUsers = staffService.getTasksAndUsers();
        return ResponseEntity.ok(tasksAndUsers);
    }

    @GetMapping("/getTurniketHistoryByDate/{id}")
    public HttpEntity<?> getTurniketHistoryByDate(@RequestBody TurniketHistory turniketHistory, @PathVariable Integer id) {
        Response turniketHistoryByDate = staffService.getTurniketHistoryByDate(turniketHistory, id);
        return ResponseEntity.status(turniketHistoryByDate.isSuccess() ? 201 : 409).body(turniketHistoryByDate);
    }
}
