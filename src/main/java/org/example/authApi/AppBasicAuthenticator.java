package org.example.authApi;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class AppBasicAuthenticator implements Authenticator<BasicCredentials, User> {
    private final UserService userService;

    public AppBasicAuthenticator(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        String email = credentials.getUsername();
        String password = credentials.getPassword();
        User user = userService.getUser(email);

        if (!user.equals(null) && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
