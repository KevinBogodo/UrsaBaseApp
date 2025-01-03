package com.urssa.urssaAppPressing.v2.company;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue( generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", length = 40)
    private String name;

    @Column(name = "registration_no", length = 30)
    private String regNo;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "phone")
    private Long phone;

    @Column(name = "postal_number", length = 25)
    private String postalNo;

    @Column(name = "street", length = 25)
    private String street;

    @Column(name = "city", length = 25)
    private String city;

    @Column(name = "country", length = 25)
    private String country;

    @CreatedBy
    @Column(name = "created_by", updatable = true, nullable = true)
    private UUID createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = true)
    private UUID updatedBy;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false;


    public Company(String name, String regNo, String email, Long phone, String postalNo, String street, String city, String country) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
        this.phone = phone;
        this.postalNo = postalNo;
        this.street = street;
        this.city = city;
        this.country = country;
    }
}
