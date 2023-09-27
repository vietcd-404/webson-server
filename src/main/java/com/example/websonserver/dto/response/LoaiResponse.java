package com.example.websonserver.dto.response;

import com.example.websonserver.entity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoaiResponse {
    private Long maLoai;
    private String tenLoai;
    private Boolean xoa;
    private Integer trangThai;
}
