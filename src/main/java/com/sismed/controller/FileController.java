package com.sismed.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sismed.exception.CustomException;
import com.sismed.request.FileInfoRequest;
import com.sismed.request.FileRequest;
import com.sismed.request.ReportRequest;
import com.sismed.response.FileResponse;
import com.sismed.service.FileService;
import com.sismed.service.ReportService;
import com.sismed.util.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final ReportService reportService;

    @PostMapping(value = "/upload", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE
    })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                       @RequestPart("request") String request) {
        try {
            String storedFile = fileService.storeFile(file);
            fileService.upload(storedFile, getJson(request));
            return ResponseEntity.ok(new Response<String>("OK", "Encriptado"));
        } catch (CustomException e) {
            return ResponseEntity.status(Integer.valueOf(e.getErrorCode())).body(new Response<>(null, e.getMessage()));
        }
    }

    private FileRequest getJson(String request) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(request, FileRequest.class);
        } catch (JsonProcessingException e) {
            throw new CustomException("Error al recibir parametros", e, "500");
        }
    }

    @PostMapping(value = "/download")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<String>> downloadFile() {
        try {
            return ResponseEntity.ok(new Response<String>(fileService.download(), "Encriptado"));
        } catch (CustomException e) {
            return ResponseEntity.status(Integer.valueOf(e.getErrorCode())).body(new Response<>(null, e.getMessage()));
        }
    }

    @PostMapping(value = "/report")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<FileResponse>> generateReport(@RequestBody ReportRequest request) {
        try {
            return ResponseEntity.ok(new Response<FileResponse>(reportService.generateReport(request), "Encriptado"));
        } catch (CustomException e) {
            return ResponseEntity.status(Integer.valueOf(e.getErrorCode())).body(new Response<>(null, e.getMessage()));
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Response<List<FileInfoRequest>>> getFiles() {
        return ResponseEntity.ok(new Response<List<FileInfoRequest>>(fileService.getFiles(), "OK"));
    }


}
