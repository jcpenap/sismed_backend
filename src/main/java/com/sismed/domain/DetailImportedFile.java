package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "DETAIL_IMPORTED_FILE")
public class DetailImportedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name ="imported_file_id", nullable = false)
    private ImportedFile importedFile;
    private String nit;
    @Column(name = "invoice_number")
    private String invoiceNumber;
    @Column(name = "invoice_date")
    private Date invoiceDate;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "first_ium")
    private String firstIum;
    @Column(name = "second_ium")
    private String secondIum;
    @Column(name = "third_ium")
    private String thirdIum;
    private String expedient;
    private Integer consecutive;
    @Column(name = "unit_invoice")
    private String unitInvoice;
    @Column(name = "unit_value")
    private Double unitValue;
    private Double amount;
    @Column(name = "invoice_month")
    private Integer invoiceMonth;
    private BigInteger line;
}
