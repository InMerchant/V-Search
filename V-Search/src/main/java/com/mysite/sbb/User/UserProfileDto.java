package com.mysite.sbb.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	private boolean videoOwnerState;
	private boolean subscribeState;
	private int subscribeCount;
	private SiteUser user;
}
