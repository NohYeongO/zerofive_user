package com.zerofive.user.api;

import com.zerofive.user.TestContainerConfig;
import com.zerofive.user.api.request.SignupRequest;
import com.zerofive.user.api.response.ErrorResponse;
import com.zerofive.user.common.exception.ErrorCode;
import com.zerofive.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SignupApiTest extends TestContainerConfig {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    private String signupUrl;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        signupUrl = "http://localhost:" + port + "/api/users/signup";
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 성공 케이스")
    void testSignupSuccess() {
        // given
        SignupRequest request = createValidSignupRequest("test@example.com");
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<String> response = restTemplate.postForEntity(signupUrl, entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 이메일 중복 케이스")
    void testSignupEmailDuplicate() {
        // given
        String duplicateEmail = "duplicate@example.com";
        
        // 첫 번째 사용자 회원가입
        SignupRequest firstRequest = createValidSignupRequest(duplicateEmail);
        HttpEntity<SignupRequest> firstEntity = new HttpEntity<>(firstRequest, headers);
        restTemplate.postForEntity(signupUrl, firstEntity, String.class);

        // 동일한 이메일로 두 번째 사용자 회원가입 시도
        SignupRequest secondRequest = new SignupRequest(
                duplicateEmail,
                "password456!",
                "두번째사용자",
                "F",
                "010-3333-4444",
                "서울시 서초구"
        );
        HttpEntity<SignupRequest> secondEntity = new HttpEntity<>(secondRequest, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, secondEntity, ErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS.getCode());
        assertThat(response.getBody().getMessage()).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
        assertThat(response.getBody().getPath()).isEqualTo("/api/users/signup");
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 이메일 양식 오류 케이스")
    void testSignupEmailFormatError() {
        // given
        SignupRequest request = new SignupRequest(
                "invalid-email-format",
                "password123!",
                "테스트사용자",
                "M",
                "010-1234-5678",
                "서울시 강남구"
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertValidationError(response, "이메일 형식이 올바르지 않습니다");
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 비밀번호 양식 오류 케이스")
    void testSignupPasswordFormatError() {
        // given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "123",
                "테스트사용자",
                "M",
                "010-1234-5678",
                "서울시 강남구"
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.INVALID_REQUEST.getCode());
        // 비밀번호 관련 검증 메시지 확인
        assertThat(response.getBody().getMessage()).satisfiesAnyOf(
                message -> assertThat(message).contains("비밀번호는 8자 이상"),
                message -> assertThat(message).contains("비밀번호는 영문, 숫자, 특수문자")
        );
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 전화번호 양식 오류 케이스")
    void testSignupPhoneFormatError() {
        // given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password123!",
                "테스트사용자",
                "M",
                "123-456-789",
                "서울시 강남구"
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertValidationError(response, "휴대폰 번호 형식이 올바르지 않습니다");
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 빈 이름 케이스")
    void testSignupEmptyNameError() {
        // given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password123!",
                "",
                "M",
                "010-1234-5678",
                "서울시 강남구"
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertValidationError(response, "이름은 필수 입력 값입니다");
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 빈 성별 케이스")
    void testSignupEmptyGenderError() {
        // given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password123!",
                "테스트사용자",
                "",
                "010-1234-5678",
                "서울시 강남구"
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertValidationError(response, "성별은 필수 입력 값입니다");
    }

    @Test
    @DisplayName("회원가입 API 테스트 - 빈 주소 케이스")
    void testSignupEmptyAddressError() {
        // given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password123!",
                "테스트사용자",
                "M",
                "010-1234-5678",
                ""
        );
        HttpEntity<SignupRequest> entity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                signupUrl, entity, ErrorResponse.class);

        // then
        assertValidationError(response, "주소는 필수 입력 값입니다");
    }

    // === 헬퍼 메서드 ===

    private SignupRequest createValidSignupRequest(String email) {
        return new SignupRequest(
                email,
                "password123!",
                "테스트사용자",
                "M",
                "010-1234-5678",
                "서울시 강남구"
        );
    }

    private void assertValidationError(ResponseEntity<ErrorResponse> response, String expectedMessage) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(ErrorCode.INVALID_REQUEST.getCode());
        assertThat(response.getBody().getMessage()).contains(expectedMessage);
        assertThat(response.getBody().getPath()).isEqualTo("/api/users/signup");
    }
}
