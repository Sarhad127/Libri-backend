package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}