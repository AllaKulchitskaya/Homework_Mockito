package org.example;

import java.util.*;

public class UserRepository {
    private final List<User> users;

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public List<User> getAllUsers() {
        return Collections.unmodifiableList(users);
    }

    public Optional<User> searchUserByLogin(String login) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    public Optional<User> searchUserByLoginAndPassword(String login, String password) {
        return users.stream()
                .filter(user -> user.getLogin().equals(login) && user.getPassword().equals(password))
                .findFirst();
    }

    public void addUser(User user) {
        users.add(user);
    }
}
