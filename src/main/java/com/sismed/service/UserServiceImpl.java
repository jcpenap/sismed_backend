package com.sismed.service;

import com.sismed.domain.Actor;
import com.sismed.domain.DocumentType;
import com.sismed.domain.User;
import com.sismed.exception.CustomException;
import com.sismed.repository.ActorRepository;
import com.sismed.repository.DocumentTypeRepository;
import com.sismed.repository.RoleRepository;
import com.sismed.repository.UserRepository;
import com.sismed.request.UserRequest;
import com.sismed.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final ActorRepository actorRepository;
    private final RoleRepository roleRepository;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(users())
                .collect(Collectors.toList());
    }

    private Function<User, UserResponse> users() {
        return user -> {
            UserResponse response = new UserResponse();
            response.setAvaliabilityCode(user.getAvaliabilityCode());
            response.setEmail(user.getEmail());
            response.setId(user.getId());
            response.setDocumentType(user.getDocumentType().getInfo());
            response.setDocumentTypeId(user.getDocumentType().getId());
            response.setDocumentTypeName(user.getDocumentType().getName());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());
            List<String> roles = userRepository.rolesByUserId(user.getId());
            response.setRoles(roles);
            response.setActor(user.getActor().getInfo());
            response.setActorId(user.getActor().getId());
            response.setActorName(user.getActor().getName());
            return response;
        };
    }

    @Override
    public void edit(int id, UserRequest request) throws CustomException {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("Usuario no encontrado","500"));
        DocumentType documentType = documentTypeRepository.findByName(request.getDocumentTypes());
        Actor actor = actorRepository.findById(request.getActor()).orElseThrow(() -> new CustomException("Actor no encontrado","500"));
        user.setAvaliabilityCode(request.getAvaliabilityCode());
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setPassword(request.getPassword());
        user.setUpdateTime(LocalDateTime.now());
        user.setDocumentType(documentType);
        user.setActor(actor);
        user.setRoles(request.getRoles().stream().map(roleRepository::findByName).collect(Collectors.toSet()));
        userRepository.save(user);
    }
}
