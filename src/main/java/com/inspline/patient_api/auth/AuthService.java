package com.inspline.patient_api.auth;

import com.inspline.patient_api.auth.dto.LoginRequest;
import com.inspline.patient_api.auth.dto.LoginResponse;
import com.inspline.patient_api.auth.exception.AuthErrorCode;
import com.inspline.patient_api.global.exception.ApplicationException;
import com.inspline.patient_api.user.UserRepository;
import com.inspline.patient_api.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ApplicationException(AuthErrorCode.INVALID_CREDENTIALS));

        if (!user.getPassword().equals(request.password())) {
            throw new ApplicationException(AuthErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtProvider.generateToken(request.username());
        return new LoginResponse(token);
    }
}