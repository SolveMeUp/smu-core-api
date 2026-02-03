package com.solvemeup.smucoreapi.domain.user.reader;

import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 조회 전용 Reader.
 *
 * <p>사용자 조회 시 도메인 정책(존재 여부, 차단 여부)을
 * 함께 검증한다.
 */
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReader {

    private final UserRepository userRepository;

    /**
     * 사용자 ID로 사용자를 조회한다.
     *
     * @throws UserNotFoundException 사용자가 존재하지 않을 경우
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
