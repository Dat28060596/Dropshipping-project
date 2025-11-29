package com.onlinestore.util;
import java.util.Random;
public class dichvuvanchuyen {
    public double tinhPhiShip(String diaChi) {
        if (diaChi == null) return 0;
        
        String diaChiThuong = diaChi.toLowerCase();
        if (diaChiThuong.contains("ha noi") ) {
            return 30000.0; 
        }
        else if (diaChiThuong.contains("ho chi minh") || diaChiThuong.contains("tp.hcm")) {
            return 40000.0;    
        } else {
            return 50000.0; 
        }
    }

    public String taoMavahang() {
        Random rand = new Random();
        int number = rand.nextInt(999999);
        return "VN-" + number;
    }

    
    public String taoNhanIn(Long madh, String tenkh, String diachi, String mavahang) {
        
        return "---------------------------------\n" +
               "| SHIPPING LABEL                |\n" +
               "| ORDER ID: " + madh + "             |\n" +
               "| TRACKING: " + mavahang + "          |\n" +
               "| TO: " + tenkh + "                |\n" +
               "| ADDR: " + diachi + "          |\n" +
               "---------------------------------";
    }
}
