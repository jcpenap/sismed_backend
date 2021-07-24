package com.sismed.service;

import com.sismed.exception.CustomException;
import com.sismed.request.UserRequest;
import com.sismed.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAll();
    void edit(int id, UserRequest user) throws CustomException;
}
