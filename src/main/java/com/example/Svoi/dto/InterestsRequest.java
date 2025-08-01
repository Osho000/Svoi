package com.example.Svoi.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestsRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotEmpty(message = "Interests list cannot be empty")
    private List<String> interests;

    public @NotNull(message = "User ID is required") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID is required") Long userId) {
        this.userId = userId;
    }

    public @NotEmpty(message = "Interests list cannot be empty") List<String> getInterests() {
        return interests;
    }

    public void setInterests(@NotEmpty(message = "Interests list cannot be empty") List<String> interests) {
        this.interests = interests;
    }
}
