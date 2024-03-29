package org.idtaleb.skyline.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL_USER = "/api/users/signup";
    public static final String SIGN_IN_URL_USER = "/api/users/login";
    public static final String H2_DATA_BASE_URL = "/h2-console/**";
    public static final String SWAGGER_URL1 = "/swagger-ui/**";
    public static final String SWAGGER_URL2 = "/v3/api-docs/**";
    public static final String SWAGGER_URL3 = "/swagger-ui.html";
    public static final String INDEX = "/index";
    public static final String TOKEN_SECRET = "6ZFIITH2nEKvCSsj5yITSeKGejM3ODNqctA2f9DWyw4=";
}