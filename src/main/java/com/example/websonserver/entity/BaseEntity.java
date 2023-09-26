package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
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
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    protected LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    @LastModifiedDate
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    protected LocalDateTime updateDate;

}
