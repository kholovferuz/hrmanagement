package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.DTO.TurniketHistoryDTO;
import uz.pdp.hrmanagement.Service.Response;
import uz.pdp.hrmanagement.Service.TurniketHistoryService;

@RestController
@RequestMapping("/api/auth")
public class TurniketHistoryController {
    @Autowired
    TurniketHistoryService turniketHistoryService;

    @PostMapping("/enterOrExit")
    public HttpEntity<?> enterOrExit(@RequestBody TurniketHistoryDTO turniketHistoryDTO) {
        Response enterOrExit = turniketHistoryService.enterOrExit(turniketHistoryDTO);
        return ResponseEntity.status(enterOrExit.isSuccess() ? 201 : 409).body(enterOrExit);
    }
}
