package com.backend.service.infrastructure;

import com.backend.service.core.User;
import org.springframework.context.annotation.Configuration;
import java.util.UUID;

@Configuration
public class UserMapper {

    public record UserDTO(String username,String firstName,String lastName,String middleName,String password,int age){}

    public UserDTO convertUser(User user){
      return new UserDTO(
              user.username,user.firstName,
              user.lastName,user.middleName,
              user.getPassword(),user.age
      );
    }

    public User convertUserDTO(UserDTO userDTO){
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.username = userDTO.username;
        user.age = userDTO.age;
        user.setPassword(userDTO.password);
        user.firstName = userDTO.firstName;
        user.lastName = userDTO.lastName;
        user.middleName = userDTO.middleName;
        return user;
    }
}
