package com.tango.DTO;

import com.tango.models.user.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SignupRequest {

    private String email;
    private String username;
    private String password;
    private LocalDate date_of_birth;
    private LocalDate sub_deadline;
    private Set<String> roles;
    private String avatar;

    public User fromWithoutRoles() {
        return new User(email, username, password, date_of_birth, sub_deadline);
    }
}
