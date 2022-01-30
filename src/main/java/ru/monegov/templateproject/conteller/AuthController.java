package ru.monegov.templateproject.conteller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.monegov.templateproject.dto.UserAuthDTO;
import ru.monegov.templateproject.dto.UserDTO;
import ru.monegov.templateproject.entity.User;
import ru.monegov.templateproject.provider.JwtProvider;
import ru.monegov.templateproject.service.UserService;

import javax.security.auth.login.CredentialException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
	private final UserService userService;
	private final JwtProvider jwtProvider;

	public AuthController(UserService userService, JwtProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	@PostMapping("/api/auth")
	public UserDTO login(@RequestBody UserAuthDTO request, HttpServletResponse response) {
		try{
			User user = userService.findUserByUsernameAndPassword(request.getUsername(), request.getPassword());
			setAuthCookie(response, user);
			return UserDTO.fromUser(user);
		}catch (Throwable throwable){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
	}

	@GetMapping("/api/auth")
	public UserDTO getAuth(HttpServletRequest request, HttpServletResponse response) throws CredentialException {
		if (!jwtProvider.validateToken(request)) {
			return null;
		}
		String username = jwtProvider.getUsernameFromToken(request);
		User user = userService.getUserByUsername(username);
		setAuthCookie(response, user);
		return UserDTO.fromUser(user);
	}

	@GetMapping("/api/users")
	public User test() {
		return userService.getUserByUsername("admin");
	}

	private void setAuthCookie(HttpServletResponse response, User user) {
		String token = jwtProvider.generateToken(user);
		Cookie cookie = new Cookie(JwtProvider.AUTHORIZATION, token);
		cookie.setPath("/");
		cookie.setMaxAge(JwtProvider.CREDENTIALS_EXPIRED_SECONDS);
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}
}
