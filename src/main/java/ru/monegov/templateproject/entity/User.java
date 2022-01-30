package ru.monegov.templateproject.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "t_user")
public class User implements UserDetails {
	@Id
	private String username;

	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> authorities;

	private Date accountExpiredDate;

	private boolean accountNonLocked;

	private Date credentialsExpiredDate;

	private boolean enabled;

	@Override
	public boolean isAccountNonExpired() {
		if (accountExpiredDate == null) {
			return true;
		}
		return isNotExpired(accountExpiredDate);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isNotExpired(credentialsExpiredDate);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		User that = (User) o;
		return username != null && Objects.equals(username, that.username);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	private boolean isNotExpired(Date expiredDate) {
		if (expiredDate == null) {
			return true;
		}
		return new Date().compareTo(expiredDate) < 0;
	}
}
