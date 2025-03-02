package org.idtaleb.skylin.security;

public class SecurityConstants {

    public static final long EXPIRATION_TIME = 864000000; // 10 Days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users/signup";
    public static final String SIGN_IN_URL = "/api/users/login";
    public static final String MONITOR_URL = "/api/tasks/monitor";
    public static final String TOKEN_SECRET = "6ZFIITH2nEKvCSsj5yITSeKGejM3ODNqctA2f9DWyw4=";
}