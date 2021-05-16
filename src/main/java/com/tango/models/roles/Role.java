package com.tango.models.roles;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.tango.models.professional.Professional;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Role")
@Table(name = "roles")
@NoArgsConstructor
@Data
public class Role {

    @JsonBackReference
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "professionals_roles",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private final Set<Professional> professionals = new HashSet<>();

    @JsonProperty(value = "role_id")
    @Id
    @SequenceGenerator(
            name = "role_id",
            sequenceName = "role_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_id"
    )
    @Column(name = "role_id")
    private long roleId;

    @JsonProperty(value = "role_name")
    @Column(name = "role_name", nullable = false)
    private String roleName;

    public Role(String roleName) {
        this.roleName = roleName;
    }

    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return roleId == role.roleId && roleName.equals(role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
