package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "FILE_TYPE")
public class FileType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    private String description;
    private String info;
    private boolean status;
    @OneToMany(mappedBy = "fileType", cascade = CascadeType.ALL)
    private List<ImportedFile> importedFileList = new ArrayList<>();

}
