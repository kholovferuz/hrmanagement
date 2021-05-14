package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.TurniketDTO;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.TurniketService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

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

    // EXEPTION HANDLER
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidException(MethodArgumentNotValidException ex) {
        Map<String, String> mistakes = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            mistakes.put(fieldName, errorMessage);
        });
        return mistakes;
    }
}
