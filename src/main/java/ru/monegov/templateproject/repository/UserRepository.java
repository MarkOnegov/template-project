package ru.monegov.templateproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.monegov.templateproject.entity.Role;
import ru.monegov.templateproject.entity.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User,String> {
}
