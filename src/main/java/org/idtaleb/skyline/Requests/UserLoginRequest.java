package org.idtaleb.skyline.Requests;

import lombok.Data;

@Data
public class UserLoginRequest {

    private String email;
    private String password;

}
