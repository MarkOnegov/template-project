package ru.monegov.templateproject.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.monegov.templateproject.entity.Role;
import ru.monegov.templateproject.entity.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
	public static final String AUTHORIZATION = "Authorization";
	public static final int CREDENTIALS_EXPIRED_SECONDS = 3600;
	public static final long CREDENTIALS_EXPIRED_MILLISECONDS = CREDENTIALS_EXPIRED_SECONDS * 1000L;

	@Value("$(jwt.secret)")
	private String jwtSecret;

	public String generateToken(User user) {
		Date date = new Date(new Date().getTime() + CREDENTIALS_EXPIRED_MILLISECONDS);
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim("roles", String.join(",",
						user.getAuthorities().stream().map(Role::getAuthority).collect(Collectors.toSet())))
				.setExpiration(date)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	public boolean validateToken(HttpServletRequest request) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(getTokenFromRequest(request));
			return true;
		} catch (Throwable throwable) {
		}
		return false;
	}

	public String getUsernameFromToken(HttpServletRequest request) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(getTokenFromRequest(request)).getBody();
		return claims.getSubject();
	}

	private String getTokenFromRequest(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		Optional<Cookie> auth = Arrays.stream(cookies)
				.filter(cookie -> cookie.getName().equalsIgnoreCase(AUTHORIZATION))
				.findAny();
		if (auth.isEmpty()) {
			return null;
		}
		return auth.get().getValue();
	}
}
