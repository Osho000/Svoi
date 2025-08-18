package com.example.Svoi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestsRequest {
	
	@NotNull(message = "User ID is required")
	private Long userId;
	
	// Optional: interest names (backward compatible)
	private List<String> interests;

	// Optional: interest IDs
	private List<Long> interestIds;

	public @NotNull(message = "User ID is required") Long getUserId() {
		return userId;
	}

	public void setUserId(@NotNull(message = "User ID is required") Long userId) {
		this.userId = userId;
	}

	public List<String> getInterests() {
		return interests;
	}

	public void setInterests(List<String> interests) {
		this.interests = interests;
	}

	public List<Long> getInterestIds() {
		return interestIds;
	}

	public void setInterestIds(List<Long> interestIds) {
		this.interestIds = interestIds;
	}
}
