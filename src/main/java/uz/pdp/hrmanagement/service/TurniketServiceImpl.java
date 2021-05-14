package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TurniketDTO;
import uz.pdp.hrmanagement.entity.Role;
import uz.pdp.hrmanagement.entity.Turniket;
import uz.pdp.hrmanagement.entity.User;
import uz.pdp.hrmanagement.repository.TurniketRepository;
import uz.pdp.hrmanagement.repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class TurniketServiceImpl implements TurniketService {

    final TurniketRepository turniketRepository;
    final UserRepository userRepository;


    public TurniketServiceImpl(TurniketRepository turniketRepository, UserRepository userRepository) {
        this.turniketRepository = turniketRepository;
        this.userRepository = userRepository;
    }

    // adding turniket card for employees
    public Response addTurniket(TurniketDTO turniketDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new Response("Only director and hr manager can add turniket", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(turniketDTO.getUserId());
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found", false);
        }
        boolean byUserId = turniketRepository.existsByUserId(turniketDTO.getUserId());
        if (byUserId) {
            return new Response("Turniket with this user already exists", false);
        }

        Turniket turniket = new Turniket();
        turniket.setUser(optionalUser.get());
        turniketRepository.save(turniket);
        return new Response("Turniket added successfully for the user " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName(), true);
    }
}
