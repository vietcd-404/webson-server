package com.example.websonserver.api;

import com.example.websonserver.dto.request.AnhSanPhamRequest;
import com.example.websonserver.service.serviceIpml.AnhSanPhamServiceImpl;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UploadFileApi {
    @Autowired
    ServletContext app;
    @Autowired
    private AnhSanPhamServiceImpl anhSanPhamService;

    private final  String uploadRootPath = "D:\\DuAn\\webson-ui\\src\\assets\\image";


    @PostMapping("/upload/save")
    public String save ( @RequestParam MultipartFile file){

        File uploadRootDir = new File(uploadRootPath);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        try {
            String fileName = file.getOriginalFilename();
            File  serverFile = new File(uploadRootDir.getAbsoluteFile() + File.separator+fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
            stream.write(file.getBytes());
            stream.close();
            AnhSanPhamRequest anhSanPhamRequest = new AnhSanPhamRequest();
            anhSanPhamRequest.setAnh(fileName);
//            anhSanPhamService.create(anhSanPhamRequest);
            return "Tải lên và lưu thành công";
        }catch (Exception e){
            e.printStackTrace();
            return "Lỗi khi tải lên và lưu tệp ảnh";
        }

    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            File file = new File(uploadRootPath, fileName);
            if (file.exists() && file.isFile()) {
                Path filePath = Paths.get(file.getAbsolutePath());
                byte[] fileContent = Files.readAllBytes(filePath);

                HttpHeaders headers = new HttpHeaders();
                if (fileName.toLowerCase().indexOf(".png") != -1){
                    headers.setContentType(MediaType.IMAGE_PNG);
                }else if(fileName.toLowerCase().indexOf(".gif") != -1){
                    headers.setContentType(MediaType.IMAGE_GIF);
                }else if(fileName.toLowerCase().indexOf(".jpeg") != -1){
                    headers.setContentType(MediaType.IMAGE_JPEG);
                }
//                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("inline", fileName);
                return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
