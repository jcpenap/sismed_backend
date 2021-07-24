package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "LOG_REPORT")
public class LogReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    @Column(name = "initial_date")
    private Date initialDate;
    @Column(name = "finish_date")
    private Date finishDate;
    @Column(name = "file_type")
    private Integer fileType;
    private boolean status;
}
