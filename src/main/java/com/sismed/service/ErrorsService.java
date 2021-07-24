package com.sismed.service;

import com.sismed.exception.CustomException;

public interface ErrorsService {
    String getErrorsByFileId(int fileId) throws CustomException;
}
