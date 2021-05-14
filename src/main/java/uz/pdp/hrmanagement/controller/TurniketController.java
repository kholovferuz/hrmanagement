package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.dto.TurniketDTO;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.TurniketService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class TurniketController {

   final TurniketService turniketService;

    public TurniketController(TurniketService turniketService) {
        this.turniketService = turniketService;
    }

    @PostMapping("/addTurniket")
    public HttpEntity<?> addTurniket(@Valid  @RequestBody TurniketDTO turniketDTO) {
        Response addTurniket = turniketService.addTurniket(turniketDTO);
        return ResponseEntity.status(addTurniket.isSuccess() ? 200 : 409).body(addTurniket);
    }
}
