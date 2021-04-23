package uz.pdp.hrmanagement.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
private String message;
private boolean success;
private Object object;

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
