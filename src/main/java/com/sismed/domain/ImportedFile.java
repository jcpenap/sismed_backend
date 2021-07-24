package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "IMPORTED_FILE")
@NamedStoredProcedureQueries({
@NamedStoredProcedureQuery(name = "sismed_detaill", procedureName = "sismed_detaill",
        parameters = {
            @StoredProcedureParameter(mode = ParameterMode.IN, name = "initial_date",type=Date.class),
            @StoredProcedureParameter(mode = ParameterMode.IN,name = "finish_date",type= Date.class),
            @StoredProcedureParameter(mode = ParameterMode.IN,name = "file_transaction_type",type=Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.OUT, name = "reg", type = Integer.class),
            @StoredProcedureParameter(mode = ParameterMode.OUT, name = "rownumber", type = BigInteger.class),
            @StoredProcedureParameter(mode = ParameterMode.OUT, name = "avaliability_code", type = String.class)
        })
})
public class ImportedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    @ManyToOne
    @JoinColumn(name ="user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private Date createTime;
    private String name;
    @ManyToOne
    @JoinColumn(name ="file_type_id", nullable = false)
    private FileType fileType;
    @Column(name = "is_valid")
    private boolean isValid;
    @OneToMany(mappedBy = "importedFile", cascade = CascadeType.ALL)
    private List<DetailImportedFile> detailImportedFileList;


}
