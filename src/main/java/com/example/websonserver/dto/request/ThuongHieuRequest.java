package com.example.websonserver.dto.request;
import com.example.websonserver.entity.ThuongHieu;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ThuongHieuRequest {
    private Integer maThuongHieu;

    @NotBlank(message = "Không bỏ trống tên thương hiệu")
    @NotNull
    private String tenThuongHieu;

    private Boolean xoa = false;

    private Integer trangThai=0;


    public ThuongHieu map(ThuongHieu thuongHieu){
        thuongHieu.setTenThuongHieu(this.getTenThuongHieu());
        thuongHieu.setTrangThai(this.getTrangThai());
        thuongHieu.setXoa(this.getXoa());
        return thuongHieu;
    }
}
