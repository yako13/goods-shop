package Spring.Goods_Shop.service;

import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.repository.CheckoutRepository;
import Spring.Goods_Shop.dto.checkout.CheckoutDetails;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsDto;
import Spring.Goods_Shop.repository.CheckoutDetailsRepository;
import Spring.Goods_Shop.dto.checkout.CheckoutResponseDto;
import Spring.Goods_Shop.entity.Product;
import Spring.Goods_Shop.util.Formatter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    private final CheckoutDetailsRepository checkoutDetailsRepository;

    /**
     * 주문리스트 
     */
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

            String modifiedAt = "";
            if(checkout.getModifiedAt() != null){
                modifiedAt=Formatter.getLocalDate(checkout.getModifiedAt());
            }

            return CheckoutResponseDto.builder()
                    .id(checkout.getId())
//                    .checkoutCode()
                    .checkoutProductName(productInfo)
                    .checkoutDeliveryCompany(Formatter.getDeliveryCompany(checkout.getCheckoutDeliveryCompany()))
                    .checkoutName(checkout.getCheckoutName())
                    .checkoutDeliveryName(checkout.getCheckoutDeliveryName())
                    .checkoutZipCode(checkout.getCheckoutZipCode())
                    .checkoutAddress(checkout.getCheckoutAddress())
                    .checkoutDeliveryMemo(checkout.getCheckoutDeliveryMemo())
                    .checkoutCardName(checkout.getCheckoutCardName())
                    .checkoutCardNum(checkout.getCheckoutCardNum())
                    .checkoutCardCvc(checkout.getCheckoutCardCvc())
                    .checkoutExpPeriod(checkout.getCheckoutExpPeriod())
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutTotalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
                    .modifiedAt(modifiedAt)
                    .build();
        }));
    }

    /**
     * 주문상세
     */
    public CheckoutDetailsResponseDto getCheckoutDetails(Long id){
       Optional<Checkout> optionalCheckout = checkoutRepository.findById(id);

       if(optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

       Checkout checkout = optionalCheckout.get();

       //해당 주문번호의 주문상세를 리스트로 가져옴
       List<CheckoutDetails> checkoutDetailsList = checkoutDetailsRepository.findALLByCheckoutId(id);

        List<Product> productList = new ArrayList<>();
        for(CheckoutDetails checkoutDetails :checkoutDetailsList){
           productList.add(checkoutDetails.getProduct());
       }

        //배송지
        String address = "("+ checkout.getCheckoutZipCode()+") " + checkout.getCheckoutAddress();


        String ordererPhoneNumber="";

        if(checkout.getMember().getPhoneNumber() !=null){
            ordererPhoneNumber=Formatter.changePhoneNumber(checkout.getMember().getPhoneNumber());
        }


       return CheckoutDetailsResponseDto.builder()
               .id(checkout.getId())
               .checkoutState(Formatter.getCheckoutState(checkout.getCheckoutStep()))
               .deliveryCompany(Formatter.getDeliveryCompany(checkout.getCheckoutDeliveryCompany()))
               .deliveryState(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
               .checkoutCode(Formatter.getCheckoutCode(checkout.getCreatedAt()))
               .checkoutDate(Formatter.getLocalDate(checkout.getCreatedAt()))
               .deliveryCode(checkout.getCheckoutDeliveryCode())
               .totalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
               .totalProductCost(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay().subtract(checkout.getCheckoutDeliveryCost())))
               .deliveryCost(Formatter.changeBigDecimalFormat(checkout.getCheckoutDeliveryCost()))
               .cardCode(Formatter.changeCardNumber(checkout.getCheckoutCardNum()))
               .productList(productList)
               .ordererName(checkout.getMember().getName())
               .ordererPhoneNumber(ordererPhoneNumber)
               .ordererId(checkout.getMember().getUserId())
               .recipientName(checkout.getCheckoutName())
               .recipientPhoneNumber(Formatter.changePhoneNumber(checkout.getCheckoutPhoneNumber()))
               .address(address)
               .deliveryMemo(checkout.getCheckoutDeliveryMemo())
               .build();
    }

    /**
     * 주문 수정
     */
    public void editCheckout(CheckoutDetailsDto checkoutDetailsDto){
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutDetailsDto.getId());

        if(optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

        Checkout checkout = optionalCheckout.get();

        checkout.setCheckoutStep(checkoutDetailsDto.getCheckoutState());
        checkout.setCheckoutPostStep(checkoutDetailsDto.getDeliveryState());
        checkout.setCheckoutDeliveryCompany(checkoutDetailsDto.getDeliveryCompany());
        checkout.setCheckoutDeliveryCode(checkoutDetailsDto.getDeliveryCode());

        checkoutRepository.save(checkout);
    }


    /**
     * 주문 삭제
     */
    public void deleteCheckout(Long id){
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(id);

        if(optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

        Checkout checkout = optionalCheckout.get();

        checkoutRepository.delete(checkout);
    }
}
