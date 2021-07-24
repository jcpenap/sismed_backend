package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "SISMED_FILE_DETAIL")
public class sismedFileDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name ="sismed_file_id", nullable = false)
    private SismedFile sismedFile;
    @Column(name = "record_type")
    private int recordType;
    private int consecutive;
    @Column(name = "enabling_code")
    private String enablingCode;
    private String month;
    @Column(name = "actor_type")
    private String actorType;
    @Column(name = "file_type")
    private String fileFype;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "first_ium")
    private String firstIum;
    @Column(name = "second_ium")
    private String secondIum;
    @Column(name = "third_ium")
    private String thirdIum;
    private String expedient;
    @Column(name = "consecutive_number")
    private int consecutiveNumber;
    @Column(name = "unit_invoice")
    private String unitInvoice;
    @Column(name = "min_price")
    private float minPrice;
    @Column(name = "max_price")
    private float maxPrice;
    @Column(name = "total_price")
    private float totalPrice;
    @Column(name = "total_amount")
    private float totalAmount;
    @Column(name = "invoice_min_price")
    private String invoiceMinPrice;
    @Column(name = "invoice_max_price")
    private String invoiceMaxPrice;
}
