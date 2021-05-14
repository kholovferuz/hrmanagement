package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.entity.enums.RoleName;
import uz.pdp.hrmanagement.repository.RoleRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    final RoleRepository roleRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository,
                      UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    // generating users automatically when the system runs
    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            User director = new User();
            director.setFirstName("Feruz");
            director.setLastName("Kholov");
            director.setEmail("xolov.2021@mail.ru");
            director.setPassword(passwordEncoder.encode("1111"));
            director.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.DIRECTOR)));
            director.setEnabled(true);


            User hrManager = new User();
            hrManager.setFirstName("Bekhruz");
            hrManager.setLastName("Kholov");
            hrManager.setEmail("bexruzjonxolov.1996@gmail.com");
            hrManager.setPassword(passwordEncoder.encode("0000"));
            hrManager.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.HR_MANAGER)));
            hrManager.setEnabled(true);

            userRepository.saveAll(Arrays.asList(director, hrManager));
        }

    }
}
