package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "sismed_report")
public class SismedReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;
    private Integer reg;
    @Column(name = "rownumber")
    private Integer rowNumber;
    @Column(name = "avaliability_code")
    private String avaliabilityCode;
    @Column(name = "invoice_month")
    private String invoiceMonth;
    @Column(name = "role_actor")
    private String roleActor;
    private String description;
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
    @Column(name = "min_price")
    private Double minPrice;
    @Column(name = "max_price")
    private Double maxPrice;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "total_units")
    private Double totalUnits;
    @Column(name = "min_invoice")
    private String minInvoice;
    @Column(name = "max_invoice")
    private String maxInvoice;
    @Column(name = "total_meddicine")
    private Integer totalMeddicine;
    @Column(name = "initial_date")
    private Date initialDate;
    @Column(name = "finish_date")
    private Date finishDate;
    @Column(name = "file_transaction_type")
    private Integer fileTransactionType;

}