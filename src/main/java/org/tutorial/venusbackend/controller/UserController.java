package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.dto.UserDto;
import org.tutorial.venusbackend.dto.UserListDto;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.MyUserRepository;
import org.tutorial.venusbackend.service.AuthHelperService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final AuthHelperService authHelperService;
    private final MyUserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        UserDto dto = new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getRole().name(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserListDto>> getAllUsers(@RequestHeader("Authorization") String authHeader) {

        MyUser admin = authHelperService.authenticateUser(authHeader);
        if (admin.getRole() != MyUser.Role.ADMIN) {
            return ResponseEntity.status(403).build();
        }

        List<UserListDto> users = userRepository.findAll().stream()
                .map(user -> new UserListDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.isActive()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}