package org.example.auth;

import io.dropwizard.auth.Authorizer;
import org.example.db.User;

public class AppAuthorizer implements Authorizer<User>
{
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
