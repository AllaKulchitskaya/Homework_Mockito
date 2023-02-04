package org.example;

import java.util.List;

public class  UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAllUsersLogins() {
        return userRepository
                .getAllUsers()
                .stream()
                .map(User::getLogin)
                .toList();
    }

    public void createNewUser(String login, String password) {
        if ((login == null || login.isBlank()) || (password == null || password.isBlank())) {
            throw new IllegalArgumentException("The login and password should be inputted correctly");
        }
        if (userRepository.searchUserByLogin(login).isPresent()) {
            throw new UserNonUniqueException("This login is already in use");
        }
        userRepository.addUser(new User(login, password));
    }

    public boolean logIn(String login, String password) {
        return userRepository.searchUserByLoginAndPassword(login, password).isPresent();
    }
}
