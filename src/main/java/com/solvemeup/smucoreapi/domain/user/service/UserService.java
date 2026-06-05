package com.solvemeup.smucoreapi.domain.user.service;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.response.*;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.exception.NicknameAlreadyExistsException;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.solvemeup.smucoreapi.domain.user.entity.UserStatus.WITHDRAWN;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserRepository userRepository;

    public MyProfileResponse getMyProfile(Long userId) {
        User user = userReader.getUser(userId);
        return MyProfileResponse.from(user, competitionRank(user.getRating()));
    }

    @Transactional
    public void withdrawMyAccount(Long userId) {
        User user = userReader.getUser(userId);
        user.withdraw();
    }

    @Transactional
    public UpdateMyEmailResponse updateMyEmail(Long userId, UpdateMyEmailRequest request) {
        User user = userReader.getUser(userId);
        user.updateEmail(request.email());
        return new UpdateMyEmailResponse(user.getEmail());
    }

    @Transactional
    public void deleteMyEmail(Long userId) {
        User user = userReader.getUser(userId);
        user.updateEmail(null);
    }

    @Transactional
    public UpdateMyNicknameResponse updateMyNickname(Long userId, UpdateMyNicknameRequest request) {
        String nickname = request.nickname().trim();
        User user = userReader.getUser(userId);

        if (nickname.equals(user.getNickname())) {
            return new UpdateMyNicknameResponse(user.getNickname());
        }

        user.updateNickname(nickname);

        try {
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new NicknameAlreadyExistsException();
        }

        return new UpdateMyNicknameResponse(user.getNickname());
    }

    @Transactional
    public UpdateMyGithubUrlResponse updateMyGithubUrl(Long userId, UpdateMyGithubUrlRequest request) {
        User user = userReader.getUser(userId);
        user.updateGithubUrl(request.githubUrl());
        return new UpdateMyGithubUrlResponse(user.getGithubUrl());
    }

    @Transactional
    public void deleteMyGithubUrl(Long userId) {
        User user = userReader.getUser(userId);
        user.updateGithubUrl(null);
    }

    @Transactional
    public UpdateMyTechblogUrlResponse updateMyTechblogUrl(Long userId, UpdateMyTechblogUrlRequest request) {
        User user = userReader.getUser(userId);
        user.updateTechblogUrl(request.techblogUrl());
        return new UpdateMyTechblogUrlResponse(user.getTechblogUrl());
    }

    @Transactional
    public void deleteMyTechblogUrl(Long userId) {
        User user = userReader.getUser(userId);
        user.updateTechblogUrl(null);
    }

    public Page<UserProfileResponse> getRanking(Pageable pageable) {
        return userRepository.findUserRankingPageWithRank(pageable)
                .map(UserProfileResponse::from);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userReader.getUser(userId);
        return UserProfileResponse.from(user, competitionRank(user.getRating()));
    }

    /**
     * 닉네임 중복 여부를 확인한다.
     *
     * <p>탈퇴한 사용자가 점유한 닉네임도 포함하여 검사한다.
     */
    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private long competitionRank(int rating) {
        return userRepository.countByStatusNotAndRatingGreaterThan(WITHDRAWN, rating) + 1;
    }
}
