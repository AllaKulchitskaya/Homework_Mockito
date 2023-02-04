package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void whenUsersAreInRepositoryThenLoginsShouldBeReturnedCorrectly() {
        when(userRepository.getAllUsers())
                .thenReturn(List.of(new User("MariaKorshunova", "Maria11407"),
                        new User("VladimirKorostylev", "Vladimir1978"),
                        new User("OlegMalyshev", "Oleg0709")));
        List<String> actualLogins = userService.getAllUsersLogins();
        List<String> expectedLogins = List.of("MariaKorshunova", "VladimirKorostylev", "OlegMalyshev");

        assertEquals(expectedLogins, actualLogins);
        verify(userRepository).getAllUsers();
    }

    @Test
    public void whenThereAreNoUsersInRepositoryThenEmptyLoginsListShouldBeReturned() {
        when(userRepository.getAllUsers())
                .thenReturn(emptyList());
        List<String> actualLogins = userService.getAllUsersLogins();
        List<String> expectedLogins = emptyList();

        assertEquals(expectedLogins, actualLogins);
        verify(userRepository).getAllUsers();
    }


    @Test
    public void whenIncorrectParametersArePassedThenServiceThrowsException() {
        assertThatThrownBy(() -> userService.createNewUser("", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The login and password should be inputted correctly");
        verify(userRepository, never()).searchUserByLogin(any());
        verify(userRepository, never()).addUser(any());
    }

    @Test
    public void whenNullParametersArePassedThenServiceThrowsException() {
        assertThatThrownBy(() -> userService.createNewUser(null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The login and password should be inputted correctly");
        verify(userRepository, never()).searchUserByLogin(any());
        verify(userRepository, never()).addUser(any());
    }

    @Test
    public void whenExistingLoginIsPassedThenServiceThrowsException() {
        when(userRepository.searchUserByLogin("MariaKorshunova"))
                .thenReturn(Optional.of(new User("MariaKorshunova", "Maria11407")));
        assertThatThrownBy(() -> userService.createNewUser("MariaKorshunova", "Maria11407"))
                .isInstanceOf(UserNonUniqueException.class)
                .hasMessage("This login is already in use");
        verify(userRepository).searchUserByLogin("MariaKorshunova");
        verify(userRepository, never()).addUser(any());
    }

    @Test
    public void whenCorrectParametersArePassedThenNewUserIsAdded() {
        userService.createNewUser("VladimirKorostylev", "Vladimir1978");
        verify(userRepository).searchUserByLogin("VladimirKorostylev");
        verify(userRepository).addUser(new User("VladimirKorostylev", "Vladimir1978"));
    }

    @Test
    public void whenUserWithLoginAndPasswordIsFoundThenReturnTrue() {
        when(userRepository.searchUserByLoginAndPassword("MariaKorshunova", "Maria11407"))
                .thenReturn(Optional.of(new User("MariaKorshunova", "Maria11407")));
        assertTrue(userService.logIn("MariaKorshunova", "Maria11407"));
    }

    @Test
    public void whenUserWithLoginAndPasswordIsNotFoundThenReturnFalse() {
        when(userRepository.searchUserByLoginAndPassword(any(), any()))
                .thenReturn(Optional.empty());
        assertFalse(userService.logIn("MariaKorshunova", "Maria11407"));
    }
}
