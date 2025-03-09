package Spring.Goods_Shop.checkout;

import Spring.Goods_Shop.util.Formatter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    public Page<CheckoutResponseDto> getCheckoutList(Pageable pageable){
        Page<Checkout> checkoutList = checkoutRepository.findAll(pageable);
        return checkoutList.map((checkout -> {
            int length = checkout.getCheckoutDetailsList().size();
            String productName = checkout.getCheckoutDetailsList().get(0).getProduct().getName();
            String productInfo = "";
            //주문한 상품이 1개일 경우
            if(length==1){
                productInfo=productName;
            }
            //2개 이상일 경우
            else {
                productInfo=productName+"외 "+ (length-1) +"종";
            }

            return CheckoutResponseDto.builder()
                    .id(checkout.getId())
                    .checkoutProductName(productInfo)
                    .checkoutName(checkout.getCheckoutName())
                    .checkoutDeliveryName(checkout.getCheckoutDeliveryName())
                    .checkoutZipCode(checkout.getCheckoutZipCode())
                    .checkoutAddress(checkout.getCheckoutAddress())
                    .checkoutDeliveryMemo(checkout.getCheckoutDeliveryMemo())
                    .checkoutCardName(checkout.getCheckoutCardName())
                    .checkoutCardNum(checkout.getCheckoutCardNum())
                    .checkoutCardCvc(checkout.getCheckoutCardCvc())
                    .checkoutExpPeriod(checkout.getCheckoutExpPeriod())
                    .checkoutPostName(checkout.getCheckoutPostName())
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutTotalPay(checkout.getCheckoutTotalPay())
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
//                    .modifiedAt(Formatter.getLocalDate(checkout.getModifiedAt()))
                    .build();
        }));
    }


}
