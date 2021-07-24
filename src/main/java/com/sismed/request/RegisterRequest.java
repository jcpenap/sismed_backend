package com.sismed.request;

import com.sismed.domain.RoleType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Set<RoleType> roles;
    @NotNull
    private String documentTypes;
    @NotNull
    private String actor;
    @NotNull
    private String avaliabilityCode;

}
