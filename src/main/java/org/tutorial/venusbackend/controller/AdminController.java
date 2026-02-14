package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.exceptions.UnauthorizedException;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.repository.MyUserRepository;
import org.tutorial.venusbackend.service.AuthHelperService;
import org.tutorial.venusbackend.service.BookImportService;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookImportService bookImportService;
    private final AuthHelperService authHelperService;
    private final MyUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/import-books")
    public ResponseEntity<Map<String, Integer>> importBooks(@RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        if (user.getRole() != MyUser.Role.ADMIN) {
            throw new UnauthorizedException();
        }

        Map<String, Integer> result = bookImportService.importBooksFromJson("/books.json");

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{userId}/active")
    public ResponseEntity<Map<String, Object>> setActiveStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long userId,
            @RequestParam boolean active
    ) {
        MyUser admin = authHelperService.authenticateUser(authHeader);

        if (admin.getRole() != MyUser.Role.ADMIN) {
            throw new UnauthorizedException();
        }

        if (admin.getId().equals(userId) && !active) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "You cannot disable your own account."));
        }

        MyUser targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        targetUser.setActive(active);
        MyUser savedUser = userRepository.save(targetUser);

        return ResponseEntity.ok(Map.of(
                "userId", savedUser.getId(),
                "active", savedUser.isActive()
        ));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<Map<String, Object>> createAdminUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> requestBody
    ) {
        MyUser admin = authHelperService.authenticateUser(authHeader);

        if (admin.getRole() != MyUser.Role.ADMIN) {
            throw new UnauthorizedException();
        }

        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String firstName = requestBody.get("firstName");
        String lastName = requestBody.get("lastName");

        if (email == null || email.isBlank() ||
                password == null || password.isBlank() ||
                firstName == null || firstName.isBlank() ||
                lastName == null || lastName.isBlank()) {

            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email, password, first name, and last name are required."));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "User with this email already exists."));
        }

        MyUser newAdmin = new MyUser();
        newAdmin.setEmail(email);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setRole(MyUser.Role.ADMIN);
        newAdmin.setFirstName(firstName);
        newAdmin.setLastName(lastName);
        newAdmin.setActive(true);

        userRepository.save(newAdmin);

        return ResponseEntity.ok(Map.of(
                "userId", newAdmin.getId(),
                "email", newAdmin.getEmail(),
                "firstName", newAdmin.getFirstName(),
                "lastName", newAdmin.getLastName(),
                "role", newAdmin.getRole().name(),
                "active", newAdmin.isActive()
        ));
    }
}