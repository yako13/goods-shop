package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.inter.ProductImageManager;
import Spring.Goods_Shop.repository.CheckoutRepository;
import Spring.Goods_Shop.entity.CheckoutDetails;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    private final CheckoutDetailsRepository checkoutDetailsRepository;

    private final ProductImageManager productImageManager;

    /**
     * 관리자 주문리스트
     */
    public Page<CheckoutResponseDto> getCheckoutList(Pageable pageable) {
        Page<Checkout> checkoutList = checkoutRepository.findAll(pageable);
        return checkoutList.map((checkout -> {
            int length = checkout.getCheckoutDetailsList().size();
            String productName = checkout.getCheckoutDetailsList().get(0).getProduct().getName();
            String productInfo = "";
            //주문한 상품이 1개일 경우
            if (length == 1) {
                productInfo = productName;
            }
            //2개 이상일 경우
            else {
                productInfo = productName + "외 " + (length - 1) + "종";
            }

            String modifiedAt = "";
            if (checkout.getModifiedAt() != null) {
                modifiedAt = Formatter.getLocalDate(checkout.getModifiedAt());
            }

            return CheckoutResponseDto.builder()
                    .id(checkout.getId())
//                    .checkoutCode()
                    .checkoutProductName(productInfo)
                    .checkoutName(checkout.getCheckoutName())
                    .checkoutDeliveryName(checkout.getCheckoutDeliveryName())
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutTotalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
                    .modifiedAt(modifiedAt)
                    .build();
        }));
    }

    /**
     * 관리자 주문상세
     */
    public CheckoutDetailsResponseDto getCheckoutDetails(Long id) {
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(id);

        if (optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

        Checkout checkout = optionalCheckout.get();

        //해당 주문번호의 주문상세를 리스트로 가져옴
        List<CheckoutDetails> checkoutDetailsList = checkoutDetailsRepository.findALLByCheckoutId(id);

        List<Product> productList = new ArrayList<>();
        for (CheckoutDetails checkoutDetails : checkoutDetailsList) {
            productList.add(checkoutDetails.getProduct());
        }

        //배송지
        String address = "(" + checkout.getCheckoutZipCode() + ") " + checkout.getCheckoutAddress();


        String ordererPhoneNumber = "";

        if (checkout.getMember().getPhoneNumber() != null) {
            ordererPhoneNumber = Formatter.changePhoneNumber(checkout.getMember().getPhoneNumber());
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
     * 관리자 주문 수정
     */
    public void editCheckout(CheckoutDetailsDto checkoutDetailsDto) {
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(checkoutDetailsDto.getId());

        if (optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

        Checkout checkout = optionalCheckout.get();

        checkout.setCheckoutStep(checkoutDetailsDto.getCheckoutState());
        checkout.setCheckoutPostStep(checkoutDetailsDto.getDeliveryState());
        checkout.setCheckoutDeliveryCompany(checkoutDetailsDto.getDeliveryCompany());
        checkout.setCheckoutDeliveryCode(checkoutDetailsDto.getDeliveryCode());

        checkoutRepository.save(checkout);
    }


    /**
     * 관리자 주문 삭제
     */
    public void deleteCheckout(Long id) {
        Optional<Checkout> optionalCheckout = checkoutRepository.findById(id);

        if (optionalCheckout.isEmpty()) throw new RuntimeException("존재하지 않는 주문");

        Checkout checkout = optionalCheckout.get();

        checkoutRepository.delete(checkout);
    }

    /**
     * 회원 주문리스트
     */
    public List<CheckoutResponseDto> getMemberCheckoutList(Member member) {
        List<Checkout> checkoutList = checkoutRepository.findAllByMemberId(member.getId());

        List<CheckoutResponseDto> checkoutResponseDtos = new ArrayList<>();



        //로그인한 회원의 주문목록 가져옴
        for (Checkout checkout : checkoutList) {
            List<ProductListResponseDto> productListResponseDtos = new ArrayList<>();
            //주문 PK로 주문 상세 리스트 찾기
            List<CheckoutDetails> checkoutDetails = checkoutDetailsRepository.findALLByCheckoutId(checkout.getId());

            //주문 상세 리스트의 제품 찾기
            for(CheckoutDetails checkoutDetails1 : checkoutDetails){

                //주문한 상품 수량을 BigDecimal 형변환
                BigDecimal productCount = new BigDecimal(checkoutDetails1.getCheckoutDetailCnt());


                ProductListResponseDto productListResponseDto = ProductListResponseDto.builder()
                        .id(checkoutDetails1.getProduct().getId())
                        .name(checkoutDetails1.getProduct().getName())
                        .totalPrice(Formatter.changeBigDecimalFormat(checkoutDetails1.getProduct().getPrice().multiply(productCount))) //상품 총 가격 = 구매개수 X 상품 개당 가격
                        .mainImagePath(productImageManager.createImageUrl(checkoutDetails1.getProduct().getProductImage().getImageFullName()))
                        .build();

                productListResponseDtos.add(productListResponseDto);
            }

            CheckoutResponseDto checkoutResponseDto = CheckoutResponseDto.builder()
                    .id(checkout.getId())
                    .checkoutSize(checkoutDetails.size())
                    .productList(productListResponseDtos)
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutCode(Formatter.getCheckoutCode(checkout.getCreatedAt()))
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
                    .checkoutTotalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                    .build();


            checkoutResponseDtos.add(checkoutResponseDto);
        }

        return checkoutResponseDtos;
    }
}
