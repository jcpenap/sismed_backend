package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "DOCUMENT_TYPE")
public class DocumentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;
    private String name;
    private String info;
    private boolean status;
    @OneToMany(mappedBy = "documentType", cascade = CascadeType.ALL)
    private List<User> userList = new ArrayList<>();
}