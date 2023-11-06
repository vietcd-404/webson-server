package com.example.websonserver.service.serviceIpml;

import com.example.websonserver.config.vnpay.VnPayConfig;
import com.example.websonserver.constants.Constants;
import com.example.websonserver.dto.request.ThanhToanRequest;
import com.example.websonserver.dto.response.MessageResponse;
import com.example.websonserver.dto.response.ThanhToanRes;
import com.example.websonserver.entity.HoaDon;
import com.example.websonserver.repository.HoaDonRepository;
import com.example.websonserver.service.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class VnPayServiceImpl implements VnPayService {
    @Autowired
    private HoaDonRepository hoaDonRepository;


    @Override
    public ThanhToanRequest creatPayment() throws UnsupportedEncodingException {
        return null;
//
//        int amount = 10000000;
//
//        Map<String, String> vnp_params = new HashMap<>();
////        vnp_params.put("vnp_Version", VnPayConfig.vnp_Version);
////        vnp_params.put("vnp_Command", VnPayConfig.vnp_Command);
//        vnp_params.put("vnp_TmnCode", VnPayConfig.vnp_TmnCode);
//        vnp_params.put("vnp_Amount", String.valueOf(amount));
////		String bank_code = form.getBankCode();
////		if (bank_code != null && !bank_code.isEmpty()) {
//        vnp_params.put("vnp_BankCode", "NCB");
////		}
//
//        LocalDateTime time = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String vnp_CreateDate = time.format(formatter);
//        vnp_params.put("vnp_CreateDate", vnp_CreateDate);
//
//        String vnp_ExpireDate = time.format(formatter);
//        vnp_params.put("vnp_ExpireDate",vnp_ExpireDate);
//
////        vnp_params.put("vnp_CurrCode", VnPayConfig.vnp_CurrCode);
////        vnp_params.put("vnp_Locale", VnPayConfig.vnp_Locale);
//////        vnp_params.put("vnp_OrderInfo", String.valueOf(form.getOrderId()));
////        vnp_params.put("vnp_OrderInfo", VnPayConfig.getRandomNumber(8));
////        vnp_params.put("vnp_OrderType", VnPayConfig.vnp_OrderType);
////        if (form.isCusOrAdmin()){
////            vnp_params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl2);
////        }else {
////            vnp_params.put("vnp_ReturnUrl", VnPayConfig.vnp_ReturnUrl);
////        }
//        vnp_params.put("vnp_TxnRef", VnPayConfig.getRandomNumber(8));
////		vnp_params.put("vnp_SecureHash",VnPayConfig.vnp_HashSecret);
//
//        List fieldName = new ArrayList(vnp_params.keySet());
//        Collections.sort(fieldName);
//
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//
//        Iterator iterator = fieldName.iterator();
//        while (iterator.hasNext()) {
//            String name = (String) iterator.next();
//            String value = vnp_params.get(name);
//            if ((value != null) && (value.length() > 0)) {
//
//                hashData.append(name);
//                hashData.append("=");
//                hashData.append(URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()));
//
//                query.append(URLEncoder.encode(name, StandardCharsets.US_ASCII.toString()));
//                query.append("=");
//                query.append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
//
//                if (iterator.hasNext()) {
//                    query.append("&");
//                    hashData.append("&");
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = VnPayConfig.hmacSHA512(VnPayConfig.vnp_HashSecret, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = VnPayConfig.vnp_PayUrl + "?" + queryUrl;
//
//        ThanhToanRequest result = new ThanhToanRequest();
//        result.setStatus("00");
//        result.setMessage("success");
//        result.setUrl(paymentUrl);
//
//        return  MessageResponse.success("sss").withData(result);

    }

    public String payWithVNPAY(ThanhToanRes payModel, HttpServletRequest request) throws UnsupportedEncodingException {
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        cld.add(Calendar.MINUTE,15);
        String vnp_ExpireDate = formatter.format(cld.getTime());

        Map<String,String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Constants.VnPayConstant.vnp_Version);
        vnp_Params.put("vnp_Command",Constants.VnPayConstant.vnp_Command);
        vnp_Params.put("vnp_TmnCode",Constants.VnPayConstant.vnp_TmnCode);
        vnp_Params.put("vnp_Amount",String.valueOf(payModel.tongTien));
        vnp_Params.put("vnp_BankCode", Constants.VnPayConstant.vnp_BankCode);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode",Constants.VnPayConstant.vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", VnPayConfig.getIpAddress(request));
        vnp_Params.put("vnp_Locale",Constants.VnPayConstant.vnp_Locale);
        vnp_Params.put("vnp_OrderInfo",payModel.moTa);
        vnp_Params.put("vnp_OrderType",VnPayConfig.getRandomNumber(8));
        vnp_Params.put("vnp_ReturnUrl", Constants.VnPayConstant.vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", VnPayConfig.getRandomNumber(8));
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldList = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldList);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator itr =  fieldList.iterator();
        while (itr.hasNext()){
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if(fieldValue!=null && (fieldValue.length()>0)){
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if(itr.hasNext()){
                    query.append("&");
                    hashData.append("&");
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VnPayConfig.hmacSHA512(Constants.VnPayConstant.vnp_HashSecret,hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Constants.VnPayConstant.vnp_Url + "?" + queryUrl;
        return paymentUrl;
    }

}
