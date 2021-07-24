package com.sismed.service;

import com.sismed.domain.DocumentType;
import com.sismed.domain.RoleType;
import com.sismed.exception.CustomException;
import com.sismed.repository.ActorRepository;
import com.sismed.repository.DocumentTypeRepository;
import com.sismed.repository.RoleRepository;
import com.sismed.repository.UserRepository;
import com.sismed.request.LoginRequest;
import com.sismed.request.RegisterRequest;
import com.sismed.response.LoginResponse;
import com.sismed.response.UserResponse;
import com.sismed.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final DocumentTypeRepository documentTypeRepository;
    private final ActorRepository actorRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserResponse register(RegisterRequest request) throws CustomException {
        try {
            Optional<com.sismed.domain.User> optional = userRepository.findByEmail(request.getEmail());
            if(!optional.isPresent()) {
                return save(request);
            }
            throw new CustomException("El usuario ya se encuentra registrado", "500");
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), e, "500");
        }
    }

    private UserResponse save(RegisterRequest request) {
        com.sismed.domain.User user = new com.sismed.domain.User();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAvaliabilityCode(request.getAvaliabilityCode());
        user.setRoles(request.getRoles().stream().map(roleRepository::findByName).collect(Collectors.toSet()));
        DocumentType documentType = documentTypeRepository.findByName(request.getDocumentTypes());
        user.setDocumentType(documentType);
        user.setActor(actorRepository.findByName(request.getActor()));
        com.sismed.domain.User newUser = userRepository.save(user);
        return getResponse(newUser);
    }

    private UserResponse getResponse(com.sismed.domain.User user) {
        UserResponse response = new UserResponse();
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setId(user.getId());
        response.setDocumentType(user.getDocumentType().getInfo());
        response.setDocumentTypeId(user.getDocumentType().getId());
        response.setAvaliabilityCode(user.getAvaliabilityCode());
        return response;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateToken(authentication);

        User principal = (User) authentication.getPrincipal();
        Set<RoleType> roles = principal.getAuthorities().stream()
                .map(item -> RoleType.valueOf(item.getAuthority()))
                .collect(Collectors.toSet());
        String username = principal.getUsername();
        return new LoginResponse(accessToken, roles, username);
    }

    public User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
