package uz.pdp.hrmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.DTO.TurniketDTO;
import uz.pdp.hrmanagement.Entity.Role;
import uz.pdp.hrmanagement.Entity.Turniket;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Repository.TurniketRepository;
import uz.pdp.hrmanagement.Repository.UserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class TurniketService {
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    UserRepository userRepository;

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
        Turniket turniket = new Turniket();
        turniket.setUser(optionalUser.get());
        turniketRepository.save(turniket);
        return new Response("Turniket added successfully for the user " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName(), true);
    }
}
