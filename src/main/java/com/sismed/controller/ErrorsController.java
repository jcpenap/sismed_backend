package com.sismed.controller;

import com.sismed.exception.CustomException;
import com.sismed.request.ErrorRequest;
import com.sismed.service.ErrorsService;
import com.sismed.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/errors")
@RequiredArgsConstructor
public class ErrorsController {

    private final ErrorsService errorsService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<String>> downloadErrors(@RequestBody ErrorRequest request) {
        try {
            return ResponseEntity.ok(new Response<>(errorsService.getErrorsByFileId(request.getFileId()), "Encriptado"));
        } catch (CustomException e) {
            return ResponseEntity.status(Integer.valueOf(e.getErrorCode())).body(new Response<>(null, e.getMessage()));
        }
    }

}
