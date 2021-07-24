package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "SISMED_FILE")
public class SismedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "register_type")
    private int registerType;
    @Column(name = "identification_type")
    private String identificationType;
    private String identification;
    @Column(name = "start_date", columnDefinition = "Date")
    private Date start_date;
    @Column(name = "end_date", columnDefinition = "Date")
    private Date end_date;
    @Column(name = "total_records")
    private int totalRecords;
    @Column(name = "total_medications")
    private int totalMedications;
    @Column(name = "file_name")
    private String fileName;
    @OneToMany(mappedBy = "sismedFile", cascade = CascadeType.ALL)
    private List<sismedFileDetail> sismedFileDetailList;

}
