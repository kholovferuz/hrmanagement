package uz.pdp.hrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.dto.AddUserDTO;
import uz.pdp.hrmanagement.dto.LoginDTO;
import uz.pdp.hrmanagement.service.AuthService;
import uz.pdp.hrmanagement.service.Response;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Response login = authService.loginToTheAccount(loginDTO);
        return ResponseEntity.status(login.isSuccess() ? 200 : 401).body(login);
    }

    @PostMapping("/addUser")
    public HttpEntity<?> addUser(@RequestBody AddUserDTO addUserDTO) {
        Response addManager = authService.addUser(addUserDTO);
        return ResponseEntity.status(addManager.isSuccess() ? 201 : 409).body(addManager);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> emailVerification(@RequestParam String email, @RequestParam String emailCode) {
        Response verifyEmail = authService.verifyEmail(email, emailCode);
        return ResponseEntity.status(verifyEmail.isSuccess() ? 200 : 409).body(verifyEmail);
    }


}
