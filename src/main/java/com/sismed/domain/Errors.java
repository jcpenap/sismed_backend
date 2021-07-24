package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "ERRORS")
public class Errors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    private String messa;
    private BigInteger rownumber;
    @Column(name = "file_id")
    private Integer fileId;
}
