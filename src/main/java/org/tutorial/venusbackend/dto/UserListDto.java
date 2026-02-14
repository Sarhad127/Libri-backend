package org.tutorial.venusbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserListDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
}