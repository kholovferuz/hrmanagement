package uz.pdp.hrmanagement.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.AddUserDTO;
import uz.pdp.hrmanagement.dto.LoginDTO;
import uz.pdp.hrmanagement.service.AuthService;
import uz.pdp.hrmanagement.service.Response;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        Response login = authService.loginToTheAccount(loginDTO);
        return ResponseEntity.status(login.isSuccess() ? 200 : 401).body(login);
    }

    @PostMapping("/addUser")
    public HttpEntity<?> addUser(@Valid @RequestBody AddUserDTO addUserDTO) {
        Response addManager = authService.addUser(addUserDTO);
        return ResponseEntity.status(addManager.isSuccess() ? 201 : 409).body(addManager);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> emailVerification(@RequestParam String email, @RequestParam String emailCode) {
        Response verifyEmail = authService.verifyEmail(email, emailCode);
        return ResponseEntity.status(verifyEmail.isSuccess() ? 200 : 409).body(verifyEmail);
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
