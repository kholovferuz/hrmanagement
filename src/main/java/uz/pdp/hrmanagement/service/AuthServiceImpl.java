package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import uz.pdp.hrmanagement.dto.AddUserDTO;
import uz.pdp.hrmanagement.dto.LoginDTO;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.entity.enums.RoleName;
import uz.pdp.hrmanagement.jwt.JWTProvider;
import uz.pdp.hrmanagement.repository.RoleRepository;
import uz.pdp.hrmanagement.repository.TaskRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    final UserRepository userRepository;
    final JavaMailSender javaMailSender;
    final PasswordEncoder passwordEncoder;
    final RoleRepository roleRepository;
    final TaskRepository taskRepository;
    final AuthenticationManager authenticationManager;
    final JWTProvider jwtProvider;


    public AuthServiceImpl(@Lazy UserRepository userRepository,
                           JavaMailSender javaMailSender,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           TaskRepository taskRepository,
                           AuthenticationManager authenticationManager,
                           JWTProvider jwtProvider) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    // log in the account
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

    // adding new user
    public Response addUser(AddUserDTO addUserDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User userContext = (User) authentication.getPrincipal();
        Set<Role> roles = userContext.getRoles();
        int roleId = 0;
        for (Role role : roles) {

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


            boolean existsByEmail = userRepository.existsByEmail(addUserDTO.getEmail());
            if (existsByEmail) {
                return new Response("User with this email already exists", false);
            }

            User addedUser = userRepository.save(addNewUser);

            // sending an email
            sendEmail(addedUser.getEmail(), addedUser.getEmailCode());
        }
        return new Response("Successfully added. Now, the user should verify the account!", true);

    }

    // saving user data
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

    // sending email
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

    // verifying email
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
