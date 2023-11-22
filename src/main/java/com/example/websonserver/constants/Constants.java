package com.example.websonserver.constants;

public class Constants {
    public static String DATE_FORMAT = "dd-MM-yyyy hh:mm:ss";
    public static class RESPONSE_TYPE {
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
        public static final String WARNING = "WARNING"; //validate
        public static final String ERROR_LOGIN = "ERROR LOGIN"; //validate
        public static final String INVALID_PERMISSION = "INVALID PERMISSION";// check role
    }
    public static class STATUS_ORDER{
        public static final int CHO_XAC_NHAN = 0;
        public static final int XAC_NHAN = 1;
        public static final int DANG_GIAO = 2;
        public static final int HOAN_THANH = 3;
        public static final int DA_HUY = 4;
    }
    public static class STATUS_PAYMENT{
        public static final int CHUA_THANH_TOAN = 0;
        public static final int DA_THANH_TOAN = 1;
    }
    public static class VnPayConstant {
        public static String vnp_Version = "2.1.0";
        public static String vnp_Command = "2.1.0";
        public static String vnp_TmnCode = "R1WW6JYO";
        public static String vnp_HashSecret = "IPXEDLSDMAAKNJRTOJHVSOAVWLBKFSXI";
        public static String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        public static String vnp_BankCode = "NCB";
        public static String vnp_CurrCode = "VND";
        public static String vnp_Locale = "vn";
        public static String vnp_ReturnUrl = "http://localhost:3000/home";

    }
}
