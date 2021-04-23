package uz.pdp.hrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TurniketHistoryDTO;
import uz.pdp.hrmanagement.entity.Turniket;
import uz.pdp.hrmanagement.entity.TurniketHistory;
import uz.pdp.hrmanagement.repository.TurniketHistoryRepository;
import uz.pdp.hrmanagement.repository.TurniketRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class TurniketHistoryService {
    @Autowired
    TurniketHistoryRepository turniketHistoryRepository;
    @Autowired
    TurniketRepository turniketRepository;

    // entered or left
    public Response enterOrExit(TurniketHistoryDTO turniketHistoryDTO) {
        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketHistoryDTO.getTurniketId());
        if (optionalTurniket.isEmpty()) {
            return new Response("Turniket with this id is not found!", false);
        }
        TurniketHistory turniketHistory = new TurniketHistory();
        turniketHistory.setEnterOrExit(turniketHistoryDTO.isEnterOrExit()); // true bo'lsa kiradi false bo'lsa chiqadi
        turniketHistory.setTurniket(optionalTurniket.get());

        if (turniketHistoryDTO.isEnterOrExit()) {
            turniketHistory.setDate(LocalDate.now());
            turniketHistory.setTime(LocalTime.now());
            turniketHistoryRepository.save(turniketHistory);
            return new Response("The employee " + optionalTurniket.get().getUser().getFirstName() + " " + optionalTurniket.get().getUser().getLastName() + " entered at " + turniketHistory.getDate() + " " + turniketHistory.getTime(), true);
        } else {
            turniketHistory.setDate(LocalDate.now());
            turniketHistory.setTime(LocalTime.now());
            turniketHistoryRepository.save(turniketHistory);
            return new Response("The employee " + optionalTurniket.get().getUser().getFirstName() + " " + optionalTurniket.get().getUser().getLastName() + " exited at " + turniketHistory.getDate() + " " + turniketHistory.getTime(), true);
        }
    }
}
