package ru.monegov.templateproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.monegov.templateproject.entity.Role;

import java.util.Collection;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role,String> {
	@Query(value = "select " +
		"role_.authority as authority " +
		"from t_user user_ " +
		"left outer join t_user_authorities authoritie_ " +
		"on user_.username=authoritie_.user_username " +
		"left outer join t_role role_ " +
		"on authoritie_.authorities_authority=role_.authority " +
		"where user_.username=?1", nativeQuery = true)
	List<Role> findAllRolesByUsername(String username);
}
