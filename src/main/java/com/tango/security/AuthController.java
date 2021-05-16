package com.tango.security;

import com.tango.DTO.LoginRequest;
import com.tango.DTO.SignupRequest;
import com.tango.models.user.User;
import com.tango.models.user.UserDetailsImpl;
import com.tango.models.user.UserRepository;
import com.tango.security.roles.AuthRole;
import com.tango.security.roles.AuthRoleRepository;
import com.tango.security.roles.ERole;
import com.tango.utils.JwtUtils;
import com.tango.utils.PictureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthRoleRepository authRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PictureUtils pictureUtils;

    public AuthController(@Autowired AuthenticationManager authenticationManager,
                          @Autowired UserRepository userRepository,
                          @Autowired AuthRoleRepository authRoleRepository,
                          @Autowired PasswordEncoder passwordEncoder,
                          @Autowired JwtUtils jwtUtils,
                          @Autowired PictureUtils pictureUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authRoleRepository = authRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.pictureUtils = pictureUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authUser(@RequestBody LoginRequest loginRequest) {

        // АУТЕНТИФИКАЦИЯ
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ДАННЫЕ ПО ПОЛЬЗОВАТЕЛЮ
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Date issuedAt = new Date();
        Date expiration = new Date((new Date()).getTime() + jwtUtils.getJwtExpirationMs());
        String jwt = jwtUtils.generateJwtTokenWithUsername(
                userDetails.getUsername(),
                issuedAt,
                expiration);

        // ОТВЕТ СЕРВЕРА
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User CREATED");
        response.put("issuedAt", issuedAt);
        response.put("expiration", expiration);
        response.put("jwtToken", jwt);
        response.put("roles", roles);
        response.put("user", userDetails);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        Map<String, Object> response = new HashMap<>();

        // ПРОВЕРКА СУЩЕСТВОВАНИЯ
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            response.put("success", false);
            response.put("message", "[ERROR] Username exists");
            return ResponseEntity.badRequest().body(response);
        }

        // СОЗДАНИЕ СУЩНОСТИ
        User user = new User(
                signupRequest.getEmail(),
                signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getSub_deadline(),
                signupRequest.getDate_of_birth());

        if (signupRequest.getAvatar() != null) {
            // Загрузить аватар в imgur, получить ссылку
            user.setAvatar(pictureUtils.postPictureToImgur(signupRequest.getAvatar()));
        }

        // ДОБАВЛЕНИЕ РОЛЕЙ
        Set<String> reqRoles = signupRequest.getRoles();
        Set<AuthRole> roles = new HashSet<>();

        if (reqRoles == null) {
            AuthRole userRole = authRoleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("[ERROR]\tROLE_USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                if ("ROLE_ADMIN".equals(r)) {
                    AuthRole modRole = authRoleRepository
                            .findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("[ERROR]\tROLE_ADMIN is not found"));
                    roles.add(modRole);
                } else {
                    AuthRole userRole = authRoleRepository
                            .findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("[ERROR]\tROLE_USER is not found"));
                    roles.add(userRole);
                }
            });
        }
        user.setAuthRoles(roles);

        // СОХРАНЕНИЕ
        User userDB = userRepository.save(user);

        // ДАННЫЕ ПО ПОЛЬЗОВАТЕЛЮ
        Date issuedAt = new Date();
        Date expiration = new Date((new Date()).getTime() + jwtUtils.getJwtExpirationMs());
        String jwt = jwtUtils.generateJwtTokenWithUsername(
                userDB.getUsername(),
                issuedAt,
                expiration);

        response.put("success", true);
        response.put("message", "User CREATED");
        response.put("user", userDB);
        response.put("issuedAt", issuedAt);
        response.put("expiration", expiration);
        response.put("jwtToken", jwt);
        return ResponseEntity.ok(response);
    }
}
