package com.example.websonserver.config.socket;

import com.example.websonserver.dto.request.HoaDonRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewOrder {
  private HoaDonRequest request;
  private Long maGioHang;
  private List<Long> ma;
}
