package com.example.websonserver.dto.response.ThongKeResponse;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ThongTinThongKe {
    private String doanhThu;
    private String slDaBan;
    private Integer year;
    private Integer month;
    private Integer day;

}
