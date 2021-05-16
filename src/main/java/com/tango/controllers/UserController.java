package com.tango.controllers;

import com.tango.DTO.PaginationResponse;
import com.tango.DTO.SignupRequest;
import com.tango.models.films.film.Film;
import com.tango.models.user.User;
import com.tango.services.UserServiceImpl;
import com.tango.utils.JwtUtils;
import com.tango.utils.PictureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/user")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final UserServiceImpl userService;
    private final PictureUtils pictureUtils;
    private final JwtUtils jwtUtils;

    public UserController(@Autowired UserServiceImpl userService,
                          @Autowired PictureUtils pictureUtils,
                          @Autowired JwtUtils jwtUtils) {
        this.userService = userService;
        this.pictureUtils = pictureUtils;
        this.jwtUtils = jwtUtils;
    }


    @PutMapping("{user_id}")
    public ResponseEntity<?> changeUser(@PathVariable("user_id") long userId,
                                        @RequestBody SignupRequest newUser) {
        Map<String, Object> response = new HashMap<>();

        User user = newUser.fromWithoutRoles();

        // Загрузить аватар в imgur, получить ссылку
        if (newUser.getAvatar() != null) {
            try {
                user.setAvatar(pictureUtils.postPictureToImgur(newUser.getAvatar()));
            } catch (Exception ignored) {
                response.put("success", false);
                response.put("message", "[ERROR] Can not upload photo");
                return ResponseEntity.badRequest().body(response);
            }
        }

        try {
            Date issuedAt = new Date();
            Date expiration = new Date((new Date()).getTime() + jwtUtils.getJwtExpirationMs());
            User editedUser = userService.edit(userId, user);
            String jwt = jwtUtils.generateJwtTokenWithUsername(editedUser.getUsername(), issuedAt, expiration);

            response.put("issuedAt", issuedAt);
            response.put("expiration", expiration);
            response.put("jwtToken", jwt);
            response.put("success", true);
            response.put("user", editedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "[ERROR] Username exists");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("{user_id}/favorite")
    public ResponseEntity<?> getFavorite(@PathVariable("user_id") long userId,
                                         @RequestParam int page,
                                         @RequestParam int size) {
        Page<Film> favorite = userService.getFavorite(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(favorite));
    }

    @GetMapping("{user_id}/info")
    public User getUser(@PathVariable("user_id") long userId) {
        return userService.findById(userId);
    }

    @GetMapping("list")
    public ResponseEntity<PaginationResponse<?>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam int page,
            @RequestParam int size) {

        if (search != null) {
            Page<User> users = userService.getUsersSearchByUsername(search, PageRequest.of(page, size));
            return ResponseEntity.ok(new PaginationResponse<>(users));
        }
        Page<User> users = userService.getUsers(PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(users));
    }

    @DeleteMapping("{user_id}")
    public ResponseEntity<?> banHammer(@PathVariable("user_id") long userId) {
        userService.delete(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        return ResponseEntity.ok(response);
    }
}
