package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.dto.TurniketHistoryDTO;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.TurniketHistoryService;

@RestController
@RequestMapping("/api")
public class TurniketHistoryController {
    @Autowired
    TurniketHistoryService turniketHistoryService;

    @PostMapping("/enterOrExit")
    public HttpEntity<?> enterOrExit(@RequestBody TurniketHistoryDTO turniketHistoryDTO) {
        Response enterOrExit = turniketHistoryService.enterOrExit(turniketHistoryDTO);
        return ResponseEntity.status(enterOrExit.isSuccess() ? 201 : 409).body(enterOrExit);
    }
}
