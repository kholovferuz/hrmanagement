package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.SalaryDTO;
import uz.pdp.hrmanagement.entity.TurniketHistory;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.StaffServiceImpl;

import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class StaffController {
    @Autowired
    StaffServiceImpl staffServiceImpl;

    @GetMapping("/allEmployees")
    public HttpEntity<?> readAllEmployees() {
        Response allEmployees = staffServiceImpl.getAllEmployees();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/byTurniket/{id}")
    public HttpEntity<?> employeesByTurniket(@PathVariable UUID id) {
        Response byTurniket = staffServiceImpl.getOneEmployeeByTurniket(id);
        return ResponseEntity.status(byTurniket.isSuccess() ? 200 : 409).body(byTurniket);
    }

    @GetMapping("/allTurniket")
    public HttpEntity<?> getAllTurniket() {
        Response allEmployees = staffServiceImpl.getAllTurniketByUsers();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/byTaskStatus/{id}")
    public HttpEntity<?> employeesByTaskStatus(@PathVariable UUID id) {
        Response completedTask = staffServiceImpl.getEmployeesByCompletedTask(id);
        return ResponseEntity.status(completedTask.isSuccess() ? 200 : 409).body(completedTask);
    }

    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@RequestBody SalaryDTO salaryDTO) {
        Response salary = staffServiceImpl.paySalaryToTheStaff(salaryDTO);
        return ResponseEntity.status(salary.isSuccess() ? 200 : 409).body(salary);
    }

    @GetMapping("/bySalaryAndMonth/{id}")
    public HttpEntity<?> bySalaryAndMonth(@PathVariable UUID id, @RequestBody SalaryDTO salaryDTO) {
        Response userIdAndMonth = staffServiceImpl.getSalariesByUserIdAndMonth(id, salaryDTO);
        return ResponseEntity.status(userIdAndMonth.isSuccess() ? 200 : 409).body(userIdAndMonth);
    }

    @GetMapping("/tasksAndUsers")
    public HttpEntity<?> tasksAndUsers() {
        Response tasksAndUsers = staffServiceImpl.getTasksAndUsers();
        return ResponseEntity.ok(tasksAndUsers);
    }

    @GetMapping("/getTurniketHistoryByDate/{id}")
    public HttpEntity<?> getTurniketHistoryByDate(@RequestBody TurniketHistory turniketHistory, @PathVariable UUID id) {
        Response turniketHistoryByDate = staffServiceImpl.getTurniketHistoryByDate(turniketHistory, id);
        return ResponseEntity.status(turniketHistoryByDate.isSuccess() ? 201 : 409).body(turniketHistoryByDate);
    }
}
