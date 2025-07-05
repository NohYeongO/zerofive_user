package com.zerofive.user.application;

import com.zerofive.user.api.request.SignupRequest;
import com.zerofive.user.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class UserAppService {
    
    private final SignupService signupService;
    
    public void signup(SignupRequest request) {
        signupService.signup(request);
    }
}
