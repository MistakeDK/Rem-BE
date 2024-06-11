package com.datnguyen.rem.configuration.payment;

import com.datnguyen.rem.utils.VNPayUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {
    @Value("${VNPay.vnpPayUrl}")
    @Getter
    String vnpPayUrl;
    @Value("${VNPay.vnpReturnUrl}")
    String vnpReturnUrl;
    @Value("${VNPay.vnpTmnCode}")
    String vnpTmnCode;
    @Getter
    @Value("${VNPay.secretKey}")
    String secretkey;
    @Value("${VNPay.vnpVersion}")
    String vnpVersion;
    @Value("${VNPay.vnpCommand}")
    String vnpCommand;
    @Value("${VNPay.orderType}")
    String orderType;
    public Map<String,String> getVNPayConfig(String id){
        Map<String, String> vnpParamsMap = new HashMap<>();
        vnpParamsMap.put("vnp_Version", this.vnpVersion);
        vnpParamsMap.put("vnp_Command", this.vnpCommand);
        vnpParamsMap.put("vnp_TmnCode", this.vnpTmnCode);
        vnpParamsMap.put("vnp_CurrCode", "VND");
        vnpParamsMap.put("vnp_TxnRef", id);
        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" +  VNPayUtils.getRandomNumber(8));
        vnpParamsMap.put("vnp_OrderType", this.orderType);
        vnpParamsMap.put("vnp_Locale", "vn");
        vnpParamsMap.put("vnp_ReturnUrl", this.vnpReturnUrl);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
        return vnpParamsMap;
    }

}
