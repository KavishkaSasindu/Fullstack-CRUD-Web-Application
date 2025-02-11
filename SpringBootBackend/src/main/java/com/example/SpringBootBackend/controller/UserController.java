package com.example.SpringBootBackend.controller;

import com.example.SpringBootBackend.model.UserProfile;
import com.example.SpringBootBackend.repo.UserProfileRepo;
import com.example.SpringBootBackend.service.UserService;
import lombok.NoArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:5173/")
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

    @GetMapping("/allUsers")
    public ResponseEntity<?> getALlUsers() {

        List<UserProfile> userProfile = userService.getAllUsers();
        try{

            if(userProfile.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(("No users found in database"));
            }

            return ResponseEntity.status(HttpStatus.OK).body(userProfile);

        }catch(Exception e){

//            return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseEntity.status(500).body("Something went wrong" + e.getMessage());
        }
    }

    @GetMapping("/oneUser/{userIndex}")
    public ResponseEntity<?> getUserById(@PathVariable int userIndex) {


        try{
            UserProfile user = userService.getUserProfile(userIndex);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(user);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong" + e.getMessage());
        }
    }

    @GetMapping("oneUser/{userIndex}/image")
    public ResponseEntity<?> getUserImage(@PathVariable int userIndex) {


        try {
            UserProfile user = userService.getUserProfile(userIndex);
            if(user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            return ResponseEntity.status(200).body(user.getImageData());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }

}
