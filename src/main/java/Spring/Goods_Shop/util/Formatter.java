package Spring.Goods_Shop.util;

import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.DeliveryCompany;
import Spring.Goods_Shop.enums.DeliveryState;
import Spring.Goods_Shop.enums.ProductCategory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

    public static String getCheckoutState(CheckoutState checkoutState) {
        return switch (checkoutState) {
            case CONFIRM -> "주문완료";
            case WAIT -> "주문보류";
            case CANCEL -> "주문취소";
        };
    }

    public static String getDeliveryState(DeliveryState deliveryState){
        return switch (deliveryState){
            case PENDING -> "배송준비중";
            case TRANSIT -> "배송중";
            case DELIVERED -> "배송완료";
            case CANCELED -> "배송취소";
            case RETURNED -> "반품";
        };
    }

    public static String getDeliveryCompany(DeliveryCompany deliveryCompany){
        return switch (deliveryCompany){
            case CJ -> "대한통운";
            case POST -> "우체국택배";
            case HANJIN -> "한진택배";
        };
    }
    public static String getProductCategory(ProductCategory productCategory) {
        return switch(productCategory) {
            case PHOTO_CARD -> "포토카드";
            case MEMO -> "메모지";
            case STICKER -> "스티커";
            case POSTCARD -> "엽서";
            case ETC -> "기타";
        };
    }


    public static String changeBigDecimalFormat(BigDecimal bigDecimal){
        DecimalFormat decimalFormat = new DecimalFormat("###,###");
        return decimalFormat.format(bigDecimal) + " 원";
    }


    public static String getLocalDate(LocalDateTime localDateTime){
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static String getCheckoutCode(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_"));
    }

    public static String changePhoneNumber(String phoneNumber){
        if(phoneNumber.length()==11){
            return phoneNumber.substring(0,3)+"-"+phoneNumber.substring(3,7)+"-"+phoneNumber.substring(7,11);
        }
        else if(phoneNumber.length()==10){
            return phoneNumber.substring(0,3)+"-"+phoneNumber.substring(3,6)+"-"+phoneNumber.substring(6,10);
        }
        throw new RuntimeException("잘못된 전화번호");
    }

    public static String changeCardNumber(String cardNumber){
        return cardNumber.substring(0,4)+"-"+cardNumber.substring(4,8)+"-****-****";
    }
}
