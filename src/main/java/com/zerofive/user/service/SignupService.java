package com.zerofive.user.service;

import com.zerofive.user.api.request.SignupRequest;
import com.zerofive.user.common.exception.ErrorCode;
import com.zerofive.user.common.exception.UserException;
import com.zerofive.user.domain.User;
import com.zerofive.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .gender(request.getGender())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        
        userRepository.save(user);
        log.info("Signup completed successfully for email: {}", request.getEmail());
    }
}
