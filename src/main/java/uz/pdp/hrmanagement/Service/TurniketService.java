package uz.pdp.hrmanagement.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.DTO.TurniketDTO;
import uz.pdp.hrmanagement.Entity.Turniket;
import uz.pdp.hrmanagement.Entity.User;
import uz.pdp.hrmanagement.Repository.TurniketRepository;
import uz.pdp.hrmanagement.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TurniketService {
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    UserRepository userRepository;

    // kirdi yoki chiqdi
    public Response enterOrExit(TurniketDTO turniketDTO) {
        Optional<User> optionalUser = userRepository.findById(turniketDTO.getUserId());
        if (optionalUser.isEmpty()) {
            return new Response("User with this id is not found!", false);
        }
        Turniket turniket = new Turniket();
        turniket.setEnterOrExit(turniketDTO.isEnterOrExit()); // true bo'lsa kiradi false bo'lsa chiqadi
        turniket.setUser(optionalUser.get());

        if (turniketDTO.isEnterOrExit()) {
            turniket.setEnterTime(LocalDateTime.now());
            turniketRepository.save(turniket);
            return new Response("The employee " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName() + " entered at " + turniket.getEnterTime(), true);
        } else {
            turniket.setExitTime(LocalDateTime.now());
            turniketRepository.save(turniket);
            return new Response("The employee " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName() + " exited at " + turniket.getExitTime(), true);
        }
    }
}
