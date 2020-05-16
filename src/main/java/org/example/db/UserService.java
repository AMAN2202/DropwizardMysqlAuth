package org.example.db;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;

    }

    public User getUser(String email) {
       User user=userDAO.getUser(email);
       user.setRoles(userDAO.getRoles(email));
       System.out.println(user);
       return user;

    }

    public void addUser(User user) {
        userDAO.addUser(user.getEmail(), user.getPassword());
    }
}
