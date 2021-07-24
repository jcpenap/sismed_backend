package com.sismed.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Getter
@Setter
public class FileInfoRequest {

    private int id;
    private String user;
    @DateTimeFormat(iso = ISO.DATE)
    private String createTime;
    private String name;
    private int fileTypeId;
    private String fileType;
    private boolean isValid;

}
