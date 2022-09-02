package com.tweetapp.demo.services;

import com.mongodb.client.result.UpdateResult;
import com.tweetapp.demo.dao.CustomRepo;
import com.tweetapp.demo.dao.UserRepo;
import com.tweetapp.demo.dtomodels.UserDto;
import com.tweetapp.demo.dtomodels.UserLoginDto;
import com.tweetapp.demo.dtomodels.UserPasswordDto;
import com.tweetapp.demo.exceptions.ParametersMismatchException;
import com.tweetapp.demo.exceptions.ResourceNotFoundException;
import com.tweetapp.demo.exceptions.UserAlreadyFoundException;
import com.tweetapp.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CustomRepo customRepo;

    @Override
    public User addUser(UserDto userDto) {
       /* if(userRepo.findItemByUserid(userDto.getEmail()).getEmail() != userDto.getEmail()){
            throw new UserAlreadyFoundException("User mail id Already exits");
        }else if(userRepo.findItemByUsername(userDto.getUsername()).getUsername()!=userDto.getUsername()){
            throw new UserAlreadyFoundException("User name Already used");
        }*/

        User user = userRepo.save(getUserFromUserDto(userDto));
        System.out.println(user.getEmail());
        return user;
    }

    private User getUserFromUserDto(UserDto userDto) {
        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());
        user.setMobile(userDto.getMobile());
        user.setNickname(userDto.getNickname());
        user.setPassword(userDto.getPassword());
        user.setDate(LocalDate.now());
        return user;
    }

    @Override //need to return allTweets
    public String loginCheck(UserLoginDto userLoginDto) {
        try {
            User user = userRepo.findItemByUserid(userLoginDto.getEmail());
            if(!user.getPassword().equals(userLoginDto.getPassword())){
                System.out.println(user.getEmail()+ user.getPassword());
                throw new ResourceNotFoundException("user not found");
            }
            return user.getNickname();
        }catch (Exception ex){
            throw new ResourceNotFoundException("user not found");
        }

    }

    @Override
    public void updatePassword(UserPasswordDto userPasswordDto) {
        try {
            User user = userRepo.findItemByUserid(userPasswordDto.getEmail());
            if(!user.getNickname().equals(userPasswordDto.getNickname())){
                System.out.println(user.getEmail()+ user.getPassword());
                throw new ResourceNotFoundException("user not found");
            }
            UpdateResult result =customRepo.updateUserPassword(userPasswordDto.getEmail(),userPasswordDto.getNewPassword());
            if(result == null){
                System.out.println("result null");
                throw new Exception("not updated");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

    @Override
    public User getUsersDetails(String username) {
        try {
            User user = userRepo.findItemByUsername(username);
            return user;
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
