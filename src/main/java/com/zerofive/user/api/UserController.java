package com.zerofive.user.api;

import com.zerofive.user.api.request.EmailVerificationRequest;
import com.zerofive.user.api.request.EmailVerifyCodeRequest;
import com.zerofive.user.api.request.LoginRequest;
import com.zerofive.user.api.request.SignupRequest;
import com.zerofive.user.api.request.UpdateProfileRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    /**
     * 이메일 인증 코드 발송
     */
    @PostMapping("/email/send-verification")
    public ResponseEntity<?> sendEmailVerification(@RequestBody EmailVerificationRequest request) {
        // TODO: 이메일 인증 코드 발송 로직 구현
        return ResponseEntity.ok().build();
    }
    
    /**
     * 이메일 인증 코드 확인
     */
    @PostMapping("/email/verify-code")
    public ResponseEntity<?> verifyEmailCode(@RequestBody EmailVerifyCodeRequest request) {
        // TODO: 이메일 인증 코드 확인 로직 구현
        return ResponseEntity.ok().build();
    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // TODO: 회원가입 로직 구현
        return ResponseEntity.ok().build();
    }
    
    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // TODO: 로그인 로직 구현
        return ResponseEntity.ok().build();
    }
    
    /**
     * 회원정보 수정
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request) {
        // TODO: 회원정보 수정 로직 구현
        return ResponseEntity.ok().build();
    }
    
    /**
     * 회원탈퇴
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam Long userId) {
        // TODO: 회원탈퇴 로직 구현
        return ResponseEntity.ok().build();
    }

    /**
     * 소셜 로그인 리다이렉트 처리 (Kakao)
     */
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code, @RequestParam(required = false) String state) {
        // TODO: Kakao 소셜 로그인 콜백 처리
        return ResponseEntity.ok().build();
    }
    
    /**
     * 소셜 로그인 리다이렉트 처리 (Naver)
     */
    @GetMapping("/auth/naver/callback")
    public ResponseEntity<?> naverCallback(@RequestParam String code, @RequestParam(required = false) String state) {
        // TODO: Naver 소셜 로그인 콜백 처리
        return ResponseEntity.ok().build();
    }
    
    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // TODO: 로그아웃 로직 구현
        return ResponseEntity.ok().build();
    }
}
