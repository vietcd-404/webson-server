package com.example.websonserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum VaiTroNguoiDung{
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_STAFF
}
