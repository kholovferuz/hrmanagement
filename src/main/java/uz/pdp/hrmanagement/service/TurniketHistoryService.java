package uz.pdp.hrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.TurniketHistoryDTO;

@Service
public interface TurniketHistoryService {
    Response enterOrExit(TurniketHistoryDTO turniketHistoryDTO);
}
