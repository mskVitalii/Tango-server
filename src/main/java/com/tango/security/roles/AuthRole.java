package com.tango.security.roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "auth_roles")
@Data
@NoArgsConstructor
public class AuthRole {

	@JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public AuthRole(ERole name) {
        this.name = name;
    }

	@Override
	public String toString() {
		return "Role{" +
				"name=" + name +
				'}';
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthRole authRole = (AuthRole) o;
        return id.equals(authRole.id) && name == authRole.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
