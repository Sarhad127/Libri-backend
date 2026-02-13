package org.tutorial.venusbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutorial.venusbackend.exceptions.UnauthorizedException;
import org.tutorial.venusbackend.model.MyUser;
import org.tutorial.venusbackend.service.AuthHelperService;
import org.tutorial.venusbackend.service.BookImportService;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookImportService bookImportService;
    private final AuthHelperService authHelperService;

    @PostMapping("/import-books")
    public ResponseEntity<Map<String, Integer>> importBooks(@RequestHeader("Authorization") String authHeader) {

        MyUser user = authHelperService.authenticateUser(authHeader);

        if (user.getRole() != MyUser.Role.ADMIN) {
            throw new UnauthorizedException();
        }

        Map<String, Integer> result = bookImportService.importBooksFromJson("/books.json");

        return ResponseEntity.ok(result);
    }
}