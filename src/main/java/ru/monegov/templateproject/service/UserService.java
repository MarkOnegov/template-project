package ru.monegov.templateproject.service;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.monegov.templateproject.entity.Role;
import ru.monegov.templateproject.entity.User;
import ru.monegov.templateproject.repository.RoleRepository;
import ru.monegov.templateproject.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.security.auth.login.CredentialException;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostConstruct
	@Transactional
	public void init() {
		if (userRepository.existsById("admin")) {
			return;
		}
		HashSet<Role> roles = new HashSet<>();
		Role role = new Role();
		role.setAuthority("ROLE_ADMIN");
		roles.add(role);
		role = new Role();
		role.setAuthority("ROLE_USER");
		roles.add(role);
		roleRepository.saveAll(roles);
		User user = new User();
		user.setUsername("admin");
		user.setAccountNonLocked(true);
		user.setAuthorities(roles);
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode("admin"));
		userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return userRepository.getById(username);
		} catch (Throwable throwable) {
			throw new UsernameNotFoundException(username, throwable);
		}
	}

	public User getUserByUsername(String username) {
		return userRepository.getById(username);
	}

	public List<Role> getUserRoles(String username){
		return roleRepository.findAllRolesByUsername(username);
	}

	public User findUserByUsernameAndPassword(String username, String password) {
		try {
			User user = userRepository.getById(username);
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new CredentialException();
			}
			return user;
		} catch (Throwable throwable) {
			throw new AuthenticationCredentialsNotFoundException("Bad username or password");
		}
	}
}
