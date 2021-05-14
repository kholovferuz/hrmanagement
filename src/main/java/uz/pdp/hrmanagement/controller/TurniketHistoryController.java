package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.TurniketHistoryDTO;
import uz.pdp.hrmanagement.service.Response;
import uz.pdp.hrmanagement.service.TurniketHistoryService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TurniketHistoryController {

    final TurniketHistoryService turniketHistoryService;

    public TurniketHistoryController(TurniketHistoryService turniketHistoryService) {
        this.turniketHistoryService = turniketHistoryService;
    }

    @PostMapping("/enterOrExit")
    public HttpEntity<?> enterOrExit(@Valid @RequestBody TurniketHistoryDTO turniketHistoryDTO) {
        Response enterOrExit = turniketHistoryService.enterOrExit(turniketHistoryDTO);
        return ResponseEntity.status(enterOrExit.isSuccess() ? 201 : 409).body(enterOrExit);
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
