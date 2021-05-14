package uz.pdp.hrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TurniketDTO;

@Service
public interface TurniketService {
    Response addTurniket(TurniketDTO turniketDTO);
}
