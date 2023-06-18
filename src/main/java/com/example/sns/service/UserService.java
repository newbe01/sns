package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token-expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not foundedasf", userName))
        );
    }

    @Transactional
    public User join(String userName, String password) {

        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName))
        );

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);

        return token;
    }

    public Page<Void> alarmList(String userName, Pageable pageable) {
        return Page.empty();
    }
}
