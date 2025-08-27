package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserRegistrationRequest;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider JwtTokenProvider;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User registerUser(UserRegistrationRequest request) {
        User user = new User();
        user.setTitle(request.getTitle());
        user.setFirstName(request.getFirst_name());
        user.setLastName(request.getLast_name());
        user.setEmail(request.getEmail());
        user.setMobileCode(request.getMobile_code());
        user.setMobileNumber(request.getMobile_number());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // assuming

        Role defaultRole = roleRepository.findByRole("User")
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Default role not found"));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        return mapToLoginResponse(user);
    }

    private LoginResponse mapToLoginResponse(User user) {
        String token = JwtTokenProvider.generateToken(user); // Assuming jwtTokenProvider is available
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setTitle(user.getTitle());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobile_code(user.getMobileCode());
        response.setMobile_number(user.getMobileNumber());
        response.setGender(user.getGender().name());
        response.setToken(token);
        response.setStatus(user.getStatus().name());
        // / Set role details
        Role role = user.getRole();
        if (role != null) {
            response.setRoleId(role.getId());
            response.setRoleName(role.getRole()); // This matches the 'role' field in Role entity
        }
        return response;
    }

    public Optional<User> findByemail(String email) {
        return userRepository.findByEmail(email);
    }

}
