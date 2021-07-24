package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "MEDICINE")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int expedient;
    private int consecutive;
    private String description;
    private boolean status;
}
