package org.idtaleb.skylin.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.idtaleb.skylin.Requests.UserLoginRequest;
import org.idtaleb.skylin.SpringApplicationContext;
import org.idtaleb.skylin.entities.UserApp;
import org.idtaleb.skylin.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            UserLoginRequest creds = new ObjectMapper().readValue(req.getInputStream(), UserLoginRequest.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String userName = ((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername();

        UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");

        UserApp user = userService.getUser(userName);

        String token = Jwts.builder().setSubject(user.getId()).claim("id", user.getId())
                .setExpiration(new Date(System.currentTimeMillis() + org.idtaleb.skylin.security.SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, org.idtaleb.skylin.security.SecurityConstants.TOKEN_SECRET).compact();

        res.addHeader(org.idtaleb.skylin.security.SecurityConstants.HEADER_STRING, org.idtaleb.skylin.security.SecurityConstants.TOKEN_PREFIX + token);
        res.addHeader("user_id", String.valueOf(user.getId()));

        String userId = user.getId();
        res.getWriter().write("{\"token\": \"" + token + "\", \"id\": \"" + userId + "\"}");

    }
}