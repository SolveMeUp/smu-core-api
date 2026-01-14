package com.solvemeup.smucoreapi.domain.user.service;

import com.solvemeup.smucoreapi.domain.exception.UserNotFoundException;
import com.solvemeup.smucoreapi.domain.user.dto.response.MyProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.dto.response.UserProfileResponseDTO;
import com.solvemeup.smucoreapi.domain.user.entity.User;
import com.solvemeup.smucoreapi.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public MyProfileResponseDTO getMyProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return MyProfileResponseDTO.from(user);
    }

    public UserProfileResponseDTO getUserProfile(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException(nickname));
        return UserProfileResponseDTO.from(user);
    }
}
