package uz.pdp.hrmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.DTO.AddUserDTO;
import uz.pdp.hrmanagement.DTO.LoginDTO;
import uz.pdp.hrmanagement.Entity.Role;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Entity.enums.RoleName;
import uz.pdp.hrmanagement.JWT.JWTProvider;
import uz.pdp.hrmanagement.Repository.RoleRepository;
import uz.pdp.hrmanagement.Repository.TaskRepository;
import uz.pdp.hrmanagement.Repository.UserRepository;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTProvider jwtProvider;

    // account ga login qilish
    public Response loginToTheAccount(LoginDTO loginDTO) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(loginDTO.getUsername(), user.getRoles());

            return new Response("You are logged in", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new Response("Password or username is not correct", false);
        }
    }

    // yangi user qo'shish
    public Response addUser(AddUserDTO addUserDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User userContext = (User) authentication.getPrincipal();
        Set<Role> roles = userContext.getRoles();
        int roleId = 0;
        for (Role role : roles){

            if (role.getRoleName().name().equals("DIRECTOR")) {
                roleId = roleRepository.findByRoleName(RoleName.DIRECTOR).getId();
            } else if (role.getRoleName().name().equals("HR_MANAGER")) {
                roleId = roleRepository.findByRoleName(RoleName.HR_MANAGER).getId();
            }


        User addNewUser = userData(addUserDTO);
        Set<Role> roleSet = addNewUser.getRoles();


        for (Role role2 : roleSet) {
            if (roleId == roleRepository.findByRoleName(RoleName.HR_MANAGER).getId() &&
                    !role2.getRoleName().name().equals("WORKER")) {
                return new Response("HR MANAGER can add only workers!", false);


            } else if (roleId == roleRepository.findByRoleName(RoleName.DIRECTOR).getId() &&
                    role2.getRoleName().name().equals("WORKER")) {
                return new Response("DIRECTOR can add only managers, not workers!", false);
            }
        }


        Set<Integer> rolesId = addUserDTO.getRolesId();
        for (Integer integer : rolesId) {
            boolean exists = roleRepository.existsById(integer);
            if (exists && integer == 3) {
                return new Response("HR MANAGER already exists. The company can have only one HR_MANAGER", false);
            } else if (exists && integer == 1) {
                return new Response("DIRECTOR already exists. The company can have only one DIRECTOR", false);
            }
        }


        boolean existsByEmail = userRepository.existsByEmail(addUserDTO.getEmail());
        if (existsByEmail) {
            return new Response("User with this email already exists", false);
        }

        User addedUser = userRepository.save(addNewUser);

        // emailga yuborish
        sendEmail(addedUser.getEmail(), addedUser.getEmailCode());
    }
        return new Response("Successfully added. Now, the user should set password and verify!", true);

    }

    // user ma'lumotlarini saqlash
    public User userData(AddUserDTO addUserDTO) {

        User user = new User();
        user.setFirstName(addUserDTO.getFirstName());
        user.setLastName(addUserDTO.getLastName());
        user.setEmail(addUserDTO.getEmail());
        user.setPassword(passwordEncoder.encode(addUserDTO.getPassword()));
        Set<Integer> rolesIdList = addUserDTO.getRolesId();
        Set<Role> roleSet = new HashSet<>();

        for (Integer roleId : rolesIdList) {
            Optional<Role> optionalRole = roleRepository.findById(roleId);
            optionalRole.ifPresent(roleSet::add);
        }
        user.setRoles(roleSet);
        user.setEmailCode(UUID.randomUUID().toString());
        return user;
    }

    // email jo'natish
    public void sendEmail(String email, String emailCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@gmail.com");
            message.setTo(email);
            message.setSubject("Email verification");
            message.setText("http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + email);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // emailni tasdiqlash
    public Response verifyEmail(String email, String emailCode) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new Response("Your account has been verified. Now, you can enter the system", true);
        }
        return new Response("This account has been already verified!", false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
    }
}
