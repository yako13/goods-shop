package Spring.Goods_Shop.util;

import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.DeliveryCompany;
import Spring.Goods_Shop.enums.DeliveryState;
import Spring.Goods_Shop.enums.ProductCategory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
            case PHOTO_CARD -> "포토카드홀더";
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




    //--Han Part 시작--


    //카드 번호 포멧 (뒷자리 8자리 * / 4자리마다 - 붙여주기)
    public static String CardNumFormat(String CardNum) {

        if (CardNum == null || CardNum.length() != 16) {
            return "Invalid Card Number";
        }

        // 앞 8자리 유지, 뒷 8자리는 *로 마스킹
        String masked = CardNum.substring(0, 8) + "********";

        // 4자리마다 '-' 추가
        String cardNumFormatString = masked.replaceAll("(.{4})", "$1-").substring(0, 19);

        return cardNumFormatString;
    }



    //주문번호 숫자에 붙여주는 1~1000자리 숫자
    public class IncrementalCounter {
        private static int counter = 0; // 클래스 변수로 유지

        public static String getNextNumber() {
            counter = (counter % 1000) + 1; // 1~1000까지 순환
            return String.format("%04d", counter);
        }
    }

    //주문 번호 만드는 함수
    public static String getCheckoutCode(LocalDateTime localDateTime) {
        String dateTimePart = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String counterPart = IncrementalCounter.getNextNumber(); // 3자리 숫자 추가
        return dateTimePart + counterPart;
    }

    //운송장 번호 만드는 함수
    public static String generateDeliveryCode() {

        Random random = new Random();

        // 4자리씩 3개의 랜덤 숫자 생성
        int part1 = random.nextInt(10000); // 0000 ~ 9999
        int part2 = random.nextInt(10000);
        int part3 = random.nextInt(10000);

        // 각 숫자를 4자리로 맞추기 위해 String.format 사용
        return String.format("%04d-%04d-%04d", part1, part2, part3);
    }


    //-----HanPart 끝------


}
