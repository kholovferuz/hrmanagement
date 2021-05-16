package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TaskDTO;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.entity.Task;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.entity.enums.RoleName;
import uz.pdp.hrmanagement.entity.enums.TaskStatus;
import uz.pdp.hrmanagement.repository.RoleRepository;
import uz.pdp.hrmanagement.repository.TaskRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    final TaskRepository taskRepository;
    final UserRepository userRepository;
    final JavaMailSender javaMailSender;
    final RoleRepository roleRepository;


    public TaskServiceImpl(@Lazy TaskRepository taskRepository,
                           UserRepository userRepository,
                           JavaMailSender javaMailSender,
                           RoleRepository roleRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.roleRepository = roleRepository;
    }

    // email method to send the managers and workers their tasks
    public void sendTaskToWorker(Integer taskId, String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(email);
            message.setSubject("Task for Staff");
            message.setText("Follow the link and do the attached task! " +
                    "\nhttp://localhost:8080/api/doTheTask?email=" + email + "&taskId=" + taskId);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method for the task that is activated when clicked the link
    public Response doTheTask(Integer taskId, String email) {
        Optional<Task> optionalTask = taskRepository.findByIdAndToUserEmail(taskId, email);
        if (optionalTask.isEmpty()) {
            return new Response("Task with this user email is not found", false);
        }
        Task currentTask = optionalTask.get();
        currentTask.setTaskStatus(TaskStatus.PROCESSING);

        taskRepository.save(currentTask);

        return new Response("Please do the task on time!", true);
    }

    // attaching the task to the employees
    public Response attachTask(TaskDTO taskDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // user
        Optional<User> optionalUser = userRepository.findById(taskDTO.getToUserId());
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found", false);
        }
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser")) {
            User userContext = (User) authentication.getPrincipal();
            Set<Role> roles = userContext.getRoles();

            for (Role role : roles) {
                int roleId = 0;

                switch (role.getRoleName().name()) {
                    case "DIRECTOR":
                        roleId = roleRepository.findByRoleName(RoleName.DIRECTOR).getId();
                        break;
                    case "HR_MANAGER":
                    case "MANAGER":
                        roleId = 2;
                        break;
                    default:
                        return new Response("Role with this id is not found", false);
                }


                Set<Role> toUserRole = optionalUser.get().getRoles();
                int roleId1 = 0;
                for (Role responsibleRole : toUserRole) {
                    switch (responsibleRole.getRoleName().name()) {
                        case "DIRECTOR":
                            roleId1 = roleRepository.findByRoleName(RoleName.DIRECTOR).getId();
                            break;
                        case "HR_MANAGER":
                        case "MANAGER":
                            roleId1 = 2;
                            break;
                        case "WORKER":
                            roleId1 = roleRepository.findByRoleName(RoleName.WORKER).getId();
                            break;
                        default:
                            return new Response("Role of Responsible with this id is not found", false);
                    }

                }

                boolean taskStatus = false;
                if (roleId == roleRepository.findByRoleName(RoleName.DIRECTOR).getId()
                        && roleId1 != roleRepository.findByRoleName(RoleName.DIRECTOR).getId()) {
                    taskStatus = true;
                }

                if (roleId == 2 && roleId1 != 2 && roleId1 != roleRepository.findByRoleName(RoleName.DIRECTOR).getId()) {
                    taskStatus = true;
                }
                if (!taskStatus) {
                    return new Response("You cannot assign tasks to this worker", false);
                }

            }
            Task task = new Task();
            task.setName(taskDTO.getName());
            task.setDescription(taskDTO.getDescription());
            task.setDeadline(taskDTO.getDeadline());
            task.setTaskStatus(TaskStatus.NEW);
            task.setFromUser(userContext);
            task.setToUser(optionalUser.get());
            taskRepository.save(task);

            // emailga yuborish
            sendTaskToWorker(task.getId(), optionalUser.get().getEmail());
        }
        return new Response("The task has been successfully sent!", true);
    }

    // sending an email about the completed tasks of employees to the managers or director
    public void sendEmailToTheManagers(String email, UUID id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("noreply@gmail.com");
                message.setTo(email);
                message.setSubject("Task completed");
                message.setText("The worker " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName() + " has finished his/her task!");
                javaMailSender.send(message);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    // completing the task
    public Response taskCompleted(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userContext = (User) authentication.getPrincipal();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return new Response("Task with this id is not found", false);
        }
        Task task = optionalTask.get();
        if (task.getToUser().getUsername().equals(userContext.getUsername())) {

            if (task.getTaskStatus().equals(TaskStatus.COMPLETED)) {
                return new Response("The task has already been completed!", false);
            }
            task.setTaskStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);

            sendEmailToTheManagers(optionalTask.get().getFromUser().getEmail(), optionalTask.get().getToUser().getId());

            return new Response("Task completed", true);
        }
        return new Response("It is not your task! Please choose your own task!", false);
    }
}
