package com.example.SpringBootBackend.service;

import com.example.SpringBootBackend.model.UserProfile;
import com.example.SpringBootBackend.repo.UserProfileRepo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Data
@Service
@NoArgsConstructor
public class UserService {

    private UserProfileRepo userProfileRepo;

    @Autowired
    public UserService(UserProfileRepo userProfileRepo) {
        this.userProfileRepo = userProfileRepo;
    }

    public UserProfile addUser(UserProfile userProfile, MultipartFile image) throws IOException {

        userProfile.setImageName(image.getOriginalFilename());
        userProfile.setImageType(image.getContentType());
        userProfile.setImageData(image.getBytes());

        return userProfileRepo.save(userProfile);
    }

    public List<UserProfile> getAllUsers() {
        return userProfileRepo.findAll();
    }

}
