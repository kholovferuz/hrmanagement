package uz.pdp.hrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.SalaryDTO;
import uz.pdp.hrmanagement.entity.TurniketHistory;

import java.util.UUID;
@Service
public interface StaffService {
    Response getAllEmployees();

    Response getOneEmployeeByTurniket(UUID id);

    Response getAllTurniketByUsers();

    Response getEmployeesByCompletedTask(UUID toUserId);

    Response paySalaryToTheStaff(SalaryDTO salaryDTO);

    Response getSalariesByUserIdAndMonth(UUID id, SalaryDTO salaryDTO);

    Response getTasksAndUsers();

    Response getTurniketHistoryByDate(TurniketHistory turniketHistory, UUID userId);
}
