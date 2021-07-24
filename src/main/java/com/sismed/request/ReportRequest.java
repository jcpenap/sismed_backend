package com.sismed.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
public class ReportRequest {
    @Email
    private String email;
    @DateTimeFormat(iso = ISO.DATE)
    private String finishDate;
    @DateTimeFormat(iso = ISO.DATE)
    private String initialDate;
    private Integer fileType;
}
