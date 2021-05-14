package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.SalaryDTO;
import uz.pdp.hrmanagement.entity.*;
import uz.pdp.hrmanagement.entity.enums.TaskStatus;
import uz.pdp.hrmanagement.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class StaffServiceImpl implements StaffService {

    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final TaskRepository taskRepository;
    final TurniketHistoryRepository turniketHistoryRepository;
    final SalaryRepository salaryRepository;
    final TurniketRepository turniketRepository;


    public StaffServiceImpl(@Lazy UserRepository userRepository,
                            RoleRepository roleRepository,
                            TaskRepository taskRepository,
                            TurniketHistoryRepository turniketHistoryRepository,
                            SalaryRepository salaryRepository,
                            TurniketRepository turniketRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
        this.turniketHistoryRepository = turniketHistoryRepository;
        this.salaryRepository = salaryRepository;
        this.turniketRepository = turniketRepository;
    }

    // hr manager va director can see the list of all employees
    public Response getAllEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")) {
                List<User> employeeList = userRepository.findAll();
                return new Response("Followings are the list of all employees: ", true, employeeList);
            }
            return new Response("Only director and hr manager can see the employee list", false);

        }
        return new Response("Cannot get the list!", false);
    }

    // seeing the attendance of one employee
    public Response getOneEmployeeByTurniket(UUID id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new Response("Only director and hr manager can see this list", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found!", false);
        }
        List<TurniketHistory> turniketHistoryList = turniketHistoryRepository.findAllByTurniket_UserId(id);
        if (turniketHistoryList.isEmpty()) {
            return new Response("No information about user attendance found!", false);
        }

        return new Response("Followings are the attendance rate of the chosen user: ", true, turniketHistoryList);
    }

    // seeing the attendance of employees
    public Response getAllTurniketByUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new Response("Only director and hr manager can see this list", false);
            }
        }
        List<TurniketHistory> turniketHistoryList = turniketHistoryRepository.findAll();
        if (turniketHistoryList.isEmpty()) {
            return new Response("No information about user attendance found!", false);
        }

        return new Response("Followings are the list of users by attendance rate: ", true, turniketHistoryList);
    }

    // seeing only completed tasks
    public Response getEmployeesByCompletedTask(UUID toUserId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (!role.getRoleName().name().equals("DIRECTOR")) {
                return new Response("Only director can see this list", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(toUserId);
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found!", false);
        }
        List<Task> allByTaskStatusAndToUserId = taskRepository.findAllByTaskStatusAndToUserId(TaskStatus.COMPLETED, toUserId);
        if (allByTaskStatusAndToUserId.isEmpty()) {
            return new Response("No information found!", false);
        }
        return new Response("Here are the list of users by their task", true, allByTaskStatusAndToUserId);
    }

    // computing salary to the workers(paid or  not paid)
    public Response paySalaryToTheStaff(SalaryDTO salaryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (!role.getRoleName().name().equals("DIRECTOR")) {
                return new Response("Only director can pay the salary", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(salaryDTO.getToUserId());
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found!", false);
        }

        Salary paySalary = new Salary();
        paySalary.setToUser(optionalUser.get());
        paySalary.setMonthlyAmount(salaryDTO.getMonthlyAmount());
        // oy raqamiga 12 dan katta son qo'ysa ishlamaydi chunki bunday oy yo'q
        if (salaryDTO.getMonthNumber() > 12) {
            return new Response("No month with this number", false);
        }
        paySalary.setMonthNumber(salaryDTO.getMonthNumber());
        paySalary.setPaid(salaryDTO.isPaid());

        salaryRepository.save(paySalary);
        return new Response("Salary paid to " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName(), true);
    }

    // seeing the salaries of employees by month and userId
    public Response getSalariesByUserIdAndMonth(UUID id, SalaryDTO salaryDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (!role.getRoleName().name().equals("DIRECTOR")) {
                return new Response("Only director can the the salaries", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found!", false);
        }
        if (salaryDTO.getMonthNumber() > 12) {
            return new Response("No month with this number!", false);
        }
        List<Salary> salaryList = salaryRepository.findByToUserIdAndMonthNumber(id, salaryDTO.getMonthNumber());
        if (salaryList.isEmpty()) {
            return new Response("This user did not get the salary in this month!", false);
        }
        return new Response("This is the salary of the user " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName(), true, salaryList);
    }

    // the list of completed and not completed tasks
    public Response getTasksAndUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("WORKER")) {
                return new Response("Only director, managers and hr manager can the the salaries", false);
            }
        }
        List<Task> taskList = taskRepository.findAll();
        return new Response("The list of tasks", true, taskList);
    }

    // see attendance rate by date
    public Response getTurniketHistoryByDate(TurniketHistory turniketHistory, UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new Response("Only director and hr manager can see this list", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found", false);
        }
        boolean existsByDate = turniketHistoryRepository.existsByDate(turniketHistory.getDate());
        if (!existsByDate) {
            return new Response("Turniket History in this date does not exist", false);
        }
        List<TurniketHistory> byDateAndUserId = turniketHistoryRepository.findByDateAndUserId(turniketHistory.getDate(), userId);
        return new Response("Followings are the attendance of " + optionalUser.get().getFirstName() + " " +
                optionalUser.get().getLastName(), true, byDateAndUserId);
    }
}

