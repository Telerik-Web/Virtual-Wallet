package com.telerikacademy.web.virtual_wallet.mappers;

import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final UserRepository userRepository;

    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User fromUserDto(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhone());
        user.setIsAdmin(false);
        user.setIsBlocked(false);
        return user;
    }

    public UserDTO fromUsertoUserDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(user.getPhoneNumber());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setPasswordConfirm(user.getPassword());
        return userDTO;
    }

//    public User fromRegisterDto(RegisterDto registerDto) {
//        User user = new User();
//        user.setFirstName(registerDto.getFirstName());
//        user.setLastName(registerDto.getLastName());
//        user.setUsername(registerDto.getUsername());
//        user.setPassword(registerDto.getPassword());
//        user.setEmail(registerDto.getEmail());
//        user.setPhoneNumber(registerDto.getPhone());
//        return user;
//    }

    public List<UserDtoOut> toDTOOut(List<User> userList) {
        List<UserDtoOut> userDto = new ArrayList<>();
        for (User user : userList) {
            UserDtoOut userDtoOut = new UserDtoOut();
            userDtoOut.setId(user.getId());
            userDtoOut.setUsername(user.getUsername());
            userDtoOut.setEmail(user.getEmail());
            userDtoOut.setPhone(user.getPhoneNumber());
            userDto.add(userDtoOut);
        }
        return userDto;
    }

    public UserDtoOut toUserDtoOut(UserDTO userDto) {
        UserDtoOut user = new UserDtoOut();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return user;
    }

    public UserDtoOut toUserDtoOut(User user2) {
        UserDtoOut user = new UserDtoOut();
        user.setId(user2.getId());
        user.setUsername(user2.getUsername());
        user.setEmail(user2.getEmail());
        user.setPhone(user2.getPhoneNumber());
        return user;
    }

    public User fromUserDtoUpdateToUser(UserDTOUpdate userDtoUpdate, long id) {
        User user = new User();
        User currentUser = userRepository.getById(id);
        user.setId(id);
        user.setUsername(currentUser.getUsername());
        user.setIsAdmin(currentUser.getIsAdmin());
        user.setIsBlocked(currentUser.getIsBlocked());

        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setPassword(userDtoUpdate.getPassword());
        user.setEmail(userDtoUpdate.getEmail());
        user.setPhoneNumber(userDtoUpdate.getPhone());
        return user;
    }
}
