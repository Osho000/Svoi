package com.example.Svoi.dto;

import java.util.List;

public class InterestsRequest {
    private Long userId; // или token/jwt, если не передаёшь id
    private List<String> interests;


    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }


}
