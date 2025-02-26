package com.telerikacademy.web.virtual_wallet.mappers;

import com.telerikacademy.web.virtual_wallet.models.RegisterDto;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.models.UserDTO;
import com.telerikacademy.web.virtual_wallet.models.UserDtoOut;
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
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return user;
    }

    public User fromRegisterDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone());
        return user;
    }

    public List<UserDtoOut> toDTOOut(List<User> userList) {
        List<UserDtoOut> userDto = new ArrayList<>();
        for (User user : userList) {
            UserDtoOut userDtoOut = new UserDtoOut();
            userDtoOut.setId(user.getId());
            userDtoOut.setUsername(user.getUsername());
            userDtoOut.setEmail(user.getEmail());
            userDtoOut.setPhone(user.getPhone());
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
        user.setUsername(user2.getUsername());
        user.setEmail(user2.getEmail());
        user.setPhone(user2.getPhone());
        return user;
    }

    public User fromUserDtoToUser(UserDTO userDto, int id) {
        User user = new User();
        user.setId(id);
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return user;
    }
}
