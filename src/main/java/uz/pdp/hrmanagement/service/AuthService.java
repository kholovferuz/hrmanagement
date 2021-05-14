package uz.pdp.hrmanagement.service;

import org.springframework.stereotype.Service;
import uz.pdp.hrmanagement.dto.AddUserDTO;
import uz.pdp.hrmanagement.dto.LoginDTO;
@Service
public interface AuthService {

    Response loginToTheAccount(LoginDTO loginDTO);

    Response addUser(AddUserDTO addUserDTO);

    Response verifyEmail(String email, String emailCode);

}
