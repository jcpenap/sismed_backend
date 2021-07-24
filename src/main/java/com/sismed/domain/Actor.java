package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ACTOR")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String name;
    private String info;
    private boolean status;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL)
    private List<User> userList = new ArrayList<>();
}
