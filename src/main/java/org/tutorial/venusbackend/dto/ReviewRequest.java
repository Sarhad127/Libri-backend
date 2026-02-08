package org.tutorial.venusbackend.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long bookId;
    private int rating;
    private String comment;
}