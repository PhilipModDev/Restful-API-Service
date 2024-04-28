package com.backend.service;

import com.backend.service.core.User;
import com.backend.service.infrastructure.UserMapper;
import com.backend.service.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

//The structured post, put and get and delete.
@RestController
@RequestMapping("service/data")
public class RestServicesController {
    //Gets reference to the database.
    @Autowired
    private MongoTemplate database;
    //Reference to the repository.
    @Autowired
    private UserRepository repository;
    //Creates a new mapper.
    private final UserMapper mapper = new UserMapper();

    //Gets the user by the username.
    @GetMapping("users/{name}")
    public ResponseEntity<?> handleGet(@PathVariable String username){
        if (username == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        Optional<User> userDTO = repository.findUserByUsername(username);

        if (userDTO.isEmpty())
            throw new NullPointerException("User can't be found in database from "+ username);

        return ResponseEntity.ok(userDTO.stream().map(mapper::convertUser));
    }

    //Gets all users.
    @GetMapping("users")
    public ResponseEntity<?> handleGetAllUsers(){
        ServiceApplication.LOGGER.info("Finding users...");
        return ResponseEntity.ok(database.findAll(User.class).stream().map(mapper::convertUser));
    }

    //Maps a post request.
    @PostMapping("users/newUser")
    public ResponseEntity<?> handlePostUser(@RequestBody UserMapper.UserDTO userDTO){
        if (userDTO == null || Objects.equals(userDTO.username(), "")) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        Optional<User> userOptional = repository.findUserByUsername(userDTO.username());
        Optional<User> userOptionalPassword = repository.findUserByPassword(userDTO.password());

        if (userOptional.isPresent()){
            var user = userOptional.get();
            ServiceApplication.LOGGER.info("Already defined user.");
            return ResponseEntity.created(URI.create("users/" + user.username)).body(userOptional.stream().map(mapper::convertUser));
        }
        if (userOptionalPassword.isPresent()){
            var user = userOptionalPassword.get();
            ServiceApplication.LOGGER.info("Already defined and password is already taken.");
            return ResponseEntity.created(URI.create("users/" + user.username)).body(userOptionalPassword.stream().map(mapper::convertUser));
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setAge(userDTO.age());
        user.setMiddleName(userDTO.middleName());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());
        repository.save(user);

        return ResponseEntity.created(URI.create("users/" + user.username)).body(mapper.convertUser(user));
    }
    //Maps a put request.
    @PutMapping("users/{username}")
    public ResponseEntity<?> handlePutUser(@PathVariable String username, @RequestBody UserMapper.UserDTO userDTO){
        if (username == null || userDTO == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        Optional<User> userOptional = repository.findUserByUsername(username);
        if (userOptional.isEmpty()){
            ServiceApplication.LOGGER.info("No user found in database.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var user = userOptional.get();
        user.setUsername(userDTO.username());
        user.setPassword(userDTO.password());
        user.setAge(userDTO.age());
        user.setMiddleName(userDTO.middleName());
        user.setFirstName(userDTO.firstName());
        user.setLastName(userDTO.lastName());

        user = repository.save(user);
        return ResponseEntity.ok(mapper.convertUser(user));
    }
    //Maps a delete request.
    @DeleteMapping("users/{name}")
    public void handleDeleteUser(@PathVariable String username, @RequestBody UserMapper.UserDTO userDTO){
        if (username == null || userDTO == null) return;
        Optional<User> userOptional = repository.findUserByUsername(username);
        if (userOptional.isEmpty()){
            ServiceApplication.LOGGER.info("No user found in database.");
            return;
        }
        var user = userOptional.get();
        if (!Objects.equals(user.getPassword(), userDTO.password())){
            ServiceApplication.LOGGER.info("User could not be deleted because invalid passcode.");
            return;
        }
        repository.delete(user);
        ServiceApplication.LOGGER.info("User has been terminated.");
    }
}
