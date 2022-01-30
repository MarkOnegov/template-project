package ru.monegov.templateproject.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.monegov.templateproject.provider.JwtProvider;
import ru.monegov.templateproject.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;

@Component
public class JwtFilter extends GenericFilterBean {

	private final UserService userService;
	private final JwtProvider jwtProvider;

	public JwtFilter(UserService userService, JwtProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		if (jwtProvider.validateToken((HttpServletRequest) servletRequest)) {
			String username = jwtProvider.getUsernameFromToken((HttpServletRequest) servletRequest);
			UserDetails userDetails = userService.getUserByUsername(username);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
					null, userService.getUserRoles(username));
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}


}
