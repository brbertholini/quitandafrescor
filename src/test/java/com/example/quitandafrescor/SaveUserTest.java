package com.example.quitandafrescor;

import com.example.quitandafrescor.dto.UserRequestDTO;
import com.example.quitandafrescor.model.User;
import com.example.quitandafrescor.repository.UserRepository;
import com.example.quitandafrescor.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void testSaveUser() {
        // Mock data
        UserRequestDTO userData = new UserRequestDTO("Test User", "123456789", "test@example.com", "password");
        User savedUser = new User(userData);

        // Mock repository behavior
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Call the method to be tested
        userService.saveUser(userData);

        // Verify that the repository's save method was called
        verify(userRepository, times(1)).save(any(User.class));
    }
}
