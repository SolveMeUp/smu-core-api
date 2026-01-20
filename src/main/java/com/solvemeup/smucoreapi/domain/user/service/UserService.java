package com.solvemeup.smucoreapi.domain.user.service;

import com.solvemeup.smucoreapi.domain.user.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public MyProfileResponseDTO getMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return MyProfileResponseDTO.from(user, userRepository.calcCompetitionRankByRating(user.getRating()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        userRepository.delete(user);
    }

    public UserProfileResponseDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return UserProfileResponseDTO.from(user, userRepository.calcCompetitionRankByRating(user.getRating()));
    }

    public Page<UserProfileResponseDTO> getRanking(Pageable pageable) {
        return userRepository.findRankingWithRank(pageable)
                .map(UserProfileResponseDTO::from);
    }
}
