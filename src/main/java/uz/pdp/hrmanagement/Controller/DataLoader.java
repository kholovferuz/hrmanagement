package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Entity.enums.RoleName;
import uz.pdp.hrmanagement.Repository.RoleRepository;
import uz.pdp.hrmanagement.Repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")){
            User director=new User();
            director.setFirstName("Feruz");
            director.setLastName("Kholov");
            director.setEmail("xolov.2021@mail.ru");
            director.setPassword(passwordEncoder.encode("Harvard123"));
            director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));
            director.setEnabled(true);
            userRepository.save(director);
        }
    }
}
