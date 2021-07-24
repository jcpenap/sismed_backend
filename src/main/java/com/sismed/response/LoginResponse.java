package com.sismed.response;

import com.sismed.domain.RoleType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class LoginResponse {

    private final String accessToken;
    private final Set<RoleType> roles;
    private final String username;

}
