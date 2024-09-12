package template.framework.api.model;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private Integer expiresIn;
}
