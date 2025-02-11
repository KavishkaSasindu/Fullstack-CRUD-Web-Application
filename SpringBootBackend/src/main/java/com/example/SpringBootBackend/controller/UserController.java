package com.example.SpringBootBackend.controller;

import com.example.SpringBootBackend.model.UserProfile;
import com.example.SpringBootBackend.repo.UserProfileRepo;
import com.example.SpringBootBackend.service.UserService;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;
    private UserProfileRepo userProfileRepo;

    @Autowired
    public UserController(UserService userService, UserProfileRepo userProfileRepo) {
        this.userService = userService;
        this.userProfileRepo = userProfileRepo;
    }

//    add user
    @PostMapping("/addUser")
    public ResponseEntity<?> addUserDb(@RequestPart UserProfile userProfile, @RequestPart MultipartFile image) {

        Optional<UserProfile> existUser = userProfileRepo.findById(userProfile.getUserId());

        try{

            if(existUser.isPresent()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }

            if(image != null) {
                UserProfile user = userService.addUser(userProfile, image);
//                return new ResponseEntity<>(user,HttpStatus.CREATED);
                return ResponseEntity.status(HttpStatus.CREATED).body(user);
            }
            return new ResponseEntity<>("Please fill all required fields",HttpStatus.BAD_REQUEST);

        }catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return  ResponseEntity.status(500).body("Something went wrong" + e.getMessage());
        }
    }

}
