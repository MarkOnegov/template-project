package ru.monegov.templateproject.dto;

import lombok.Data;
import ru.monegov.templateproject.entity.Role;
import ru.monegov.templateproject.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO {
	private String username;
	private Set<String> authorities;
	private boolean enabled;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;

	public static UserDTO fromUser(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.accountNonExpired = user.isAccountNonExpired();
		userDTO.accountNonLocked = user.isAccountNonLocked();
		userDTO.username = user.getUsername();
		userDTO.authorities = user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet());
		userDTO.enabled = user.isEnabled();
		userDTO.credentialsNonExpired = user.isCredentialsNonExpired();
		return userDTO;
	}
}
