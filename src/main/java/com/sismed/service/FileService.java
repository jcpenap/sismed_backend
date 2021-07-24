package com.sismed.service;

import com.sismed.exception.CustomException;
import com.sismed.request.FileInfoRequest;
import com.sismed.request.FileRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileService {

    String storeFile(MultipartFile file) throws CustomException;
    void upload(String storeFile, FileRequest fileTypeId) throws CustomException;
    String download() throws CustomException;
    List<FileInfoRequest> getFiles();



}
