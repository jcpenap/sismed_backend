package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType name;
    @Column(nullable = false)
    private String description;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updateTime;

}
