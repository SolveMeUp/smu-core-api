package com.solvemeup.smucoreapi.domain.user.service;

import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyEmailRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyGithubUrlRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyNicknameRequest;
import com.solvemeup.smucoreapi.domain.user.dto.request.UpdateMyTechblogUrlRequest;
import com.solvemeup.smucoreapi.domain.user.exception.NicknameAlreadyExistsException;
import com.solvemeup.smucoreapi.domain.user.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponse;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponse;
import com.solvemeup.smucoreapi.domain.user.entity.UserEntity;
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

    private final UserRepository userRepository;

    public MyProfileResponse getMyProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return MyProfileResponse.from(user, userRepository.calculateCompetitionRankByRating(user.getRating()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    @Transactional
    public void updateMyEmail(Long userId, UpdateMyEmailRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateEmail(request.email());
    }

    @Transactional
    public void deleteMyEmail(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateEmail(null);
    }

    @Transactional
    public void updateMyNickname(Long userId, UpdateMyNicknameRequest request) {
        String nickname = request.nickname().trim();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (nickname.equals(user.getNickname())) {
            return;
        }

        user.updateNickname(nickname);

        try {
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new NicknameAlreadyExistsException(nickname);
        }
    }

    @Transactional
    public void updateMyGithubUrl(Long userId, UpdateMyGithubUrlRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateGithubUrl(request.githubUrl());
    }

    @Transactional
    public void deleteMyGithubUrl(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateGithubUrl(null);
    }

    @Transactional
    public void updateMyTechblogUrl(Long userId, UpdateMyTechblogUrlRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateTechblogUrl(request.techblogUrl());
    }

    @Transactional
    public void deleteMyTechblogUrl(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.updateTechblogUrl(null);
    }

    public Page<UserProfileResponse> getRanking(Pageable pageable) {
        return userRepository.findUserRankingPageWithRank(pageable)
                .map(UserProfileResponse::from);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return UserProfileResponse.from(user, userRepository.calculateCompetitionRankByRating(user.getRating()));
    }

    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsIncludingDeletedByNickname(nickname) == 1;
    }
}
