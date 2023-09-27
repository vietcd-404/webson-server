package com.example.websonserver.entity;

import com.example.websonserver.constants.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Column(name = "ngay_tao")
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @CreationTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    protected LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    @LastModifiedDate
    @DateTimeFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @UpdateTimestamp
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss")
    @Transient
    protected LocalDateTime ngaySua;

}
