package com.telerikacademy.web.virtual_wallet.mappers;

import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTO;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTOOut;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTOUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User fromUserDto(UserDTO userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhone());
        user.setPhoto("assets/img/default-user.jpg");
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
        userDTO.setPassword(passwordEncoder.encode(user.getPassword()));
        userDTO.setPasswordConfirm(user.getPassword());
        return userDTO;
    }

    public List<UserDTOOut> toDTOOut(List<User> userList) {
        List<UserDTOOut> userDto = new ArrayList<>();
        for (User user : userList) {
            UserDTOOut userDtoOut = new UserDTOOut();
            userDtoOut.setId(user.getId());
            userDtoOut.setUsername(user.getUsername());
            userDtoOut.setEmail(user.getEmail());
            userDtoOut.setPhone(user.getPhoneNumber());
            userDto.add(userDtoOut);
        }
        return userDto;
    }

    public UserDTOOut toUserDtoOut(UserDTO userDto) {
        UserDTOOut user = new UserDTOOut();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return user;
    }

    public UserDTOOut toUserDtoOut(User user2) {
        UserDTOOut user = new UserDTOOut();
        user.setId(user2.getId());
        user.setUsername(user2.getUsername());
        user.setEmail(user2.getEmail());
        user.setPhone(user2.getPhoneNumber());
        return user;
    }

    public User fromUserDtoUpdateToUser(UserDTOUpdate userDtoUpdate, User currentUser) {
        User user = new User();
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        user.setIsAdmin(currentUser.getIsAdmin());
        user.setIsBlocked(currentUser.getIsBlocked());
        user.setPhoto(currentUser.getPhoto());
        user.setBalance(currentUser.getBalance());
        user.setAccountVerified(currentUser.isAccountVerified());
        user.setCards(currentUser.getCards());

        user.setFirstName(userDtoUpdate.getFirstName());
        user.setLastName(userDtoUpdate.getLastName());
        user.setPassword(passwordEncoder.encode(userDtoUpdate.getPassword()));
        user.setEmail(userDtoUpdate.getEmail());
        user.setPhoneNumber(userDtoUpdate.getPhone());
        return user;
    }

    public UserDTOUpdate fromUserToUserDtoUpdate(User user) {
        UserDTOUpdate userDtoUpdate = new UserDTOUpdate();
        userDtoUpdate.setFirstName(user.getFirstName());
        userDtoUpdate.setLastName(user.getLastName());
        userDtoUpdate.setPassword(user.getPassword());
        userDtoUpdate.setEmail(user.getEmail());
        userDtoUpdate.setPhone(user.getPhoneNumber());
        return userDtoUpdate;
    }
}
