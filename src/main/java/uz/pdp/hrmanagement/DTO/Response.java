package uz.pdp.hrmanagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response {
    private String message;
    private boolean success;
    private Object token;

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
