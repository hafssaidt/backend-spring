package org.idtaleb.skylin.Requests;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String email;
    private String password;

}
