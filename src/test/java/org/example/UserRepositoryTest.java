package org.example;



import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {


    @Test
    public void whenThereAreNoUsersAddedThenEmptyListShouldBeReturned() {
        UserRepository userRepository = new UserRepository();
        List<User> actual = userRepository.getAllUsers();
        assertEquals(0, actual.size());
    }

    @Test
    public void whenThereAreUsersAddedThenUsersListShouldBeReturned() {
        List<User> expected = createUsers();
        UserRepository userRepository = new UserRepository(expected);
        List<User> actual = userRepository.getAllUsers();
        assertEquals(expected, actual);
    }

    @Test
    public void whenUserWithLoginExistsThenReturnTrue() {
        UserRepository userRepository = new UserRepository(createUsers());
        Optional<User> user = userRepository.searchUserByLogin("MariaKorshunova");
        assertTrue(user.isPresent());
    }

    @Test
    public void whenUserWithLoginDoesNotExistThenReturnFalse() {
        UserRepository userRepository = new UserRepository(createUsers());
        Optional<User> user = userRepository.searchUserByLogin("nonExistentLogin");
        assertFalse(user.isPresent());
    }

    @Test
    public void whenUserWithLoginAndPasswordExistsThenReturnTrue() {
        UserRepository userRepository = new UserRepository(createUsers());
        Optional<User> user = userRepository.searchUserByLoginAndPassword("MariaKorshunova", "Maria11407");
        assertTrue(user.isPresent());
    }

    @Test
    public void whenLoginDoesNotExistButPasswordDoesThenReturnFalse() {
        UserRepository userRepository = new UserRepository(createUsers());
        Optional<User> user = userRepository.searchUserByLoginAndPassword("nonExistentLogin", "Maria11407");
        assertFalse(user.isPresent());
    }

    @Test
    public void whenPasswordDoesNotExistButLoginDoesThenReturnFalse() {
        UserRepository userRepository = new UserRepository(createUsers());
        Optional<User> user = userRepository.searchUserByLoginAndPassword("MariaKorshunova", "nonExistentPassword");
        assertFalse(user.isPresent());
    }

    private List<User> createUsers() {
        return List.of(
                new User("MariaKorshunova", "Maria11407"),
                new User("VladimirKorostylev", "Vladimir1978"),
                new User("OlegMalyshev", "Oleg0709"));
    }
}
