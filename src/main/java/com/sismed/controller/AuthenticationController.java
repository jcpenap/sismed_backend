package com.sismed.controller;

import com.sismed.exception.CustomException;
import com.sismed.request.LoginRequest;
import com.sismed.request.RegisterRequest;
import com.sismed.response.LoginResponse;
import com.sismed.response.UserResponse;
import com.sismed.service.AuthenticationService;
import com.sismed.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Response<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(new Response<>(authenticationService.register(request), "Autenticado"));
        } catch (CustomException e) {
            return ResponseEntity.status(Integer.valueOf(e.getErrorCode())).body(new Response<>(null, e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(new Response<>(authenticationService.login(request), "Autenticado"));
    }

}
