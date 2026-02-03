package com.solvemeup.smucoreapi.domain.user.service;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequest;
import com.solvemeup.smucoreapi.domain.user.exception.NicknameAlreadyExistsException;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponse;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponse;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.reader.UserReader;
import com.solvemeup.smucoreapi.domain.user.repository.UserInternalRepository;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserReader userReader;
    private final UserRepository userRepository;
    private final UserInternalRepository userInternalRepository;

    public MyProfileResponse getMyProfile(Long userId) {
        User user = userReader.getUser(userId);
        return MyProfileResponse.from(user, userRepository.calculateCompetitionRankByRating(user.getRating()));
    }

    /**
     * 사용자를 탈퇴 처리한다.
     *
     * <p>실제 데이터는 삭제되지 않으며
     * 소프트 딜리트 방식으로 처리된다.
     */
    @Transactional
    public void softDeleteUser(Long userId) {
        User user = userReader.getUser(userId);
        userRepository.delete(user);
    }

    @Transactional
    public void updateMyEmail(Long userId, UpdateMyEmailRequest request) {
        User user = userReader.getUser(userId);
        user.updateEmail(request.email());
    }

    @Transactional
    public void deleteMyEmail(Long userId) {
        User user = userReader.getUser(userId);
        user.updateEmail(null);
    }

    @Transactional
    public void updateMyNickname(Long userId, UpdateMyNicknameRequest request) {
        String nickname = request.nickname().trim();
        User user = userReader.getUser(userId);

        if (nickname.equals(user.getNickname())) {
            return;
        }

        user.updateNickname(nickname);

        try {
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new NicknameAlreadyExistsException();
        }
    }

    @Transactional
    public void updateMyGithubUrl(Long userId, UpdateMyGithubUrlRequest request) {
        User user = userReader.getUser(userId);
        user.updateGithubUrl(request.githubUrl());
    }

    @Transactional
    public void deleteMyGithubUrl(Long userId) {
        User user = userReader.getUser(userId);
        user.updateGithubUrl(null);
    }

    @Transactional
    public void updateMyTechblogUrl(Long userId, UpdateMyTechblogUrlRequest request) {
        User user = userReader.getUser(userId);
        user.updateTechblogUrl(request.techblogUrl());
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
        return UserProfileResponse.from(user, userRepository.calculateCompetitionRankByRating(user.getRating()));
    }

    /**
     * 닉네임 중복 여부를 확인한다.
     *
     * <p>삭제되었거나 익명화된 사용자도 포함하여 검사한다.
     */
    public boolean isNicknameDuplicated(String nickname) {
        return userInternalRepository.existsIncludingDeletedByNickname(nickname);
    }
}
