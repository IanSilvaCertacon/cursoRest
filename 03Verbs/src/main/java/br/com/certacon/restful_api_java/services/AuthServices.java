package br.com.certacon.restful_api_java.services;

import br.com.certacon.restful_api_java.repositories.UserRepository;
import br.com.certacon.restful_api_java.security.jwt.JwtTokenProvider;
import br.com.certacon.restful_api_java.vo.v1.AccountCredentialsVO;
import br.com.certacon.restful_api_java.vo.v1.TokenVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository repository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("Username " + username + " not found!");
            }

            var tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            return ResponseEntity.ok(tokenResponse);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username/password supplied!", e);
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("Invalid username/password supplied!", e);
        }
    }

    public ResponseEntity refreshToken(String username, String refreshToken) {
        var user = repository.findByUsername(username);

        var tokenResponse = new TokenVO();
        if (user != null) {
            tokenResponse = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found!");
        }
        return ResponseEntity.ok(tokenResponse);
    }
}