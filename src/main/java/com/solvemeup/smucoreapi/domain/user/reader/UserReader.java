package com.solvemeup.smucoreapi.domain.user.reader;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.solvemeup.smucoreapi.domain.user.entity.UserStatus.WITHDRAWN;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReader {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return userRepository.findByIdAndStatusNot(userId, WITHDRAWN)
                .orElseThrow(UserNotFoundException::new);
    }
}
