package org.example.auth;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.apache.commons.lang3.ObjectUtils;
import org.example.db.User;
import org.example.db.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AppBasicAuthenticator implements Authenticator<BasicCredentials, User>
{
    private final UserService userService;
    public AppBasicAuthenticator(UserService userService)
    {
        this.userService=userService;
    }


    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException
    {
        String email=credentials.getUsername();
        String password=credentials.getPassword();
        User user=userService.getUser(email);

        if (!user.equals(null) && user.getPassword().equals(password))
        {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
