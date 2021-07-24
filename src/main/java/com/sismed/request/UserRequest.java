package com.sismed.request;

import com.sismed.domain.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import java.util.Set;

@Getter
@Setter
public class UserRequest {
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
    private Set<RoleType> roles;
    private String documentTypes;
    private Long actor;
    private String avaliabilityCode;

}
