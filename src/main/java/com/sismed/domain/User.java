package com.sismed.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String avaliabilityCode;
    @ManyToOne
    @JoinColumn(name ="actor_id", nullable = false)
    private Actor actor;
    @ManyToOne
    @JoinColumn(name ="document_type_id", nullable = false)
    private DocumentType documentType;
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createTime;
    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updateTime;
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ImportedFile> importedFileList = new ArrayList<>();


}
