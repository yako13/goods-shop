package Spring.Goods_Shop.util;

import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.ShipmentState;

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

    public static String getDeliveryState(ShipmentState deliveryState){
        return switch (deliveryState){
            case PENDING -> "배송준비중";
            case TRANSIT -> "배송중";
            case DELIVERED -> "배송완료";
            case CANCELED -> "배송취소";
            case RETURNED -> "반품";
        };
    }

    public static String getLocalDate(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
