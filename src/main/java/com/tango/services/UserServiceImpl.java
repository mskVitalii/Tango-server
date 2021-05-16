package com.tango.services;

import com.tango.models.films.film.Film;
import com.tango.models.user.User;
import com.tango.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserServiceImpl(@Autowired PasswordEncoder passwordEncoder,
                           @Autowired UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User findById(long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new NoSuchElementException("USER with id='" + userId + "' does not exist"));
    }

    public User edit(long id, User newUser) {
        User oldUser = userRepository.getOne(id);
        if (newUser.getEmail() != null) oldUser.setEmail(newUser.getEmail());
        if (newUser.getUsername() != null) oldUser.setUsername(newUser.getUsername());
        if (newUser.getPassword() != null) oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getDateOfBirth() != null) oldUser.setDateOfBirth(newUser.getDateOfBirth());
        if (newUser.getAvatar() != null) oldUser.setAvatar(newUser.getAvatar());
        return userRepository.save(oldUser);
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public Page<Film> getFavorite(long userId, Pageable pageable) {
        return userRepository.findByIdFetch(userId, PageRequest.of(0, 2));
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getUsersSearchByUsername(String username, Pageable pageable) {
        return userRepository.findAllByUsernameLike("%" + username + "%", pageable);
    }
}
