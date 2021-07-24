package com.sismed.controller;

import com.sismed.exception.CustomException;
import com.sismed.request.UserRequest;
import com.sismed.response.UserResponse;
import com.sismed.service.UserService;
import com.sismed.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService user;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<List<UserResponse>>> getUsers() {
        return ResponseEntity.ok(new Response<>(user.getAll(), "Procesado"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<String>> editUser(@PathVariable("id") int id, @RequestBody UserRequest request) throws CustomException {
        user.edit(id, request);
        return ResponseEntity.ok(new Response<String>("OK", "Procesado"));
    }

}
