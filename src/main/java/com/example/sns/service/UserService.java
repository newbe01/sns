package com.example.sns.service;

import com.example.sns.exception.SnsApplicationException;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {

        Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);

        userEntityRepository.save(new UserEntity());

        return new User();
    }

    public String login(String userName, String password) {

        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(SnsApplicationException::new);

        if (!userEntity.getPassword().equals(password)) {
            throw new SnsApplicationException();
        }


        return "";
    }

}
