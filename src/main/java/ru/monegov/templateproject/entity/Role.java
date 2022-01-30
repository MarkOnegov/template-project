package ru.monegov.templateproject.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "t_role")
public class Role implements GrantedAuthority {
	@Id
	private String authority;
	@Transient
	@ManyToMany(mappedBy = "authorities")
	@ToString.Exclude
	private Set<User> users;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Role that = (Role) o;
		return authority != null && Objects.equals(authority, that.authority);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
