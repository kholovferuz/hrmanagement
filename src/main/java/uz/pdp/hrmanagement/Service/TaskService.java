package uz.pdp.hrmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.DTO.TaskDTO;
import uz.pdp.hrmanagement.Entity.Role;
import uz.pdp.hrmanagement.Entity.Task;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Entity.enums.RoleName;
import uz.pdp.hrmanagement.Entity.enums.TaskStatus;
import uz.pdp.hrmanagement.Repository.RoleRepository;
import uz.pdp.hrmanagement.Repository.TaskRepository;
import uz.pdp.hrmanagement.Repository.UserRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    RoleRepository roleRepository;

    // xodim yoki menejerlarga topshiriqlarni junatish uchun email
    public void sendTaskToWorker(Integer taskId, String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(email);
            message.setSubject("Task for Staff");
            message.setText("Follow the link and do the attached task! " +
                    "\nhttp://localhost:8080/api/auth/doTheTask?email=" + email + "&taskId=" + taskId);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // topshiriq uchun linkni bosganda amalga oshadigan method
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

    // topshiriqni tayinlash
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

    // bajarilgan taskni bajarilganligini task bergan menejer yoki director ga xabar junatish
    public void sendEmailToTheManagers(String email, UUID id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(email);
            message.setSubject("Task completed");
            message.setText("The worker " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName() + " has finished his/her task!");
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // topshiriqni tugatish
    public Response taskCompleted(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userContext = (User) authentication.getPrincipal();

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return new Response("Task with this id is not found", false);
        }
        if (optionalTask.get().getToUser().equals(userContext)) {
            Task task = optionalTask.get();
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
