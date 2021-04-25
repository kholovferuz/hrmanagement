package uz.pdp.hrmanagement.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Response {
    private String message;
    private boolean success;
    private Object token;

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
