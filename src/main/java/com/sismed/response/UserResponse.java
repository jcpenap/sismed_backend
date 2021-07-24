package com.sismed.response;

import com.sismed.domain.Actor;
import com.sismed.domain.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String avaliabilityCode;
    private Long documentTypeId;
    private String documentType;
    private String documentTypeName;
    private List<String> roles;
    private Long actorId;
    private String actor;
    private String actorName;
}
