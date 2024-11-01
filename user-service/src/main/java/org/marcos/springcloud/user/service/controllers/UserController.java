package org.marcos.springcloud.user.service.controllers;

import jakarta.validation.Valid;
import org.marcos.springcloud.user.service.models.entity.User;
import org.marcos.springcloud.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Map<String, List<User>> getAllUsers() {
        return Collections.singletonMap("user", userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> userById = userService.getUserById(id);
        if (userById.isPresent()) {
            return ResponseEntity.ok(userById.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validateResult(bindingResult);
        }
        if (!user.getEmail().isEmpty() && userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email already exists"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable Long id, @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return validateResult(bindingResult);
        }

        Optional<User> userById = userService.getUserById(id);
        if (userById.isPresent()) {
            User userToUpdate = userById.get();

            if (!user.getEmail().isEmpty() && !user.getEmail().equalsIgnoreCase(userToUpdate.getEmail()) && userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Email already exists now"));
            }

            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            userToUpdate.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userToUpdate));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> userById = userService.getUserById(id);
        if (userById.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-course")
    public ResponseEntity<?> getUserCourse(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(userService.finAllById(ids));
    }

    private static ResponseEntity<Map<String, String>> validateResult(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


}
