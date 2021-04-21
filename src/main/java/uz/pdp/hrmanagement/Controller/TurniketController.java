package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hrmanagement.DTO.TurniketDTO;
import uz.pdp.hrmanagement.Service.Response;
import uz.pdp.hrmanagement.Service.TurniketService;

@RestController
@RequestMapping("/api/auth")
public class TurniketController {
    @Autowired
    TurniketService turniketService;

    @PostMapping("/addTurniket")
    public HttpEntity<?> addTurniket(@RequestBody TurniketDTO turniketDTO) {
        Response addTurniket = turniketService.addTurniket(turniketDTO);
        return ResponseEntity.status(addTurniket.isSuccess() ? 200 : 409).body(addTurniket);
    }
}
