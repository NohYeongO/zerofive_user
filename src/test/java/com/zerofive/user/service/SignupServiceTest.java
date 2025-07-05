package com.zerofive.user.service;

import com.zerofive.user.api.request.SignupRequest;
import com.zerofive.user.common.exception.ErrorCode;
import com.zerofive.user.common.exception.UserException;
import com.zerofive.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SignupService signupService;

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest("noh05@exmple.com", "password123", "노영오", "M", "010-1234-5678", "Seoul, South Korea");
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시 예외가 발생")
    void signup_whenEmailExists_throwsUserException() {
        // given
        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        // when
        UserException exception = assertThrows(UserException.class, () -> {
            signupService.signup(signupRequest);
        });

        // then
        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }
}
