package org.example.authApi;

import io.dropwizard.auth.Authorizer;

import javax.inject.Singleton;

@Singleton
public class AppAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}
