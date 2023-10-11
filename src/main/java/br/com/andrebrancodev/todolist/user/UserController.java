package br.com.andrebrancodev.todolist.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity getUsers() {
        return ResponseEntity.status(200).body(userService.getUsers());
    }

    @GetMapping("{uuid}")
    public ResponseEntity getUsersById(@PathParam("uuid") UUID uuid) {
        UserModel user = userService.getUsersByUuid(uuid);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel user) {
        boolean isCreated = userService.existsByUsernameOrEmail(user.getUsername(), user.getEmail());

        if (isCreated) {
            return ResponseEntity.status(400).body("User Already exists");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashed);

        UserModel createdUser = userService.create(user);

        return ResponseEntity.status(201).body(createdUser);
    }
    
}
