package com.tango.security.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {
	Optional<AuthRole> findByName(ERole name);
}
