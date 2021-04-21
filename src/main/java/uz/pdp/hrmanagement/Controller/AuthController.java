package uz.pdp.hrmanagement.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagement.DTO.AddUserDTO;
import uz.pdp.hrmanagement.DTO.LoginDTO;
import uz.pdp.hrmanagement.Service.AuthService;
import uz.pdp.hrmanagement.Service.Response;

@RestController
@RequestMapping("/api/auth/")
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
