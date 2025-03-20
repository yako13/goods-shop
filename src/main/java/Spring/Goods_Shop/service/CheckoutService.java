package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.product.ProductListResponseDto;
import Spring.Goods_Shop.entity.Checkout;
import Spring.Goods_Shop.dto.checkout.CheckoutDetailsResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.enums.CheckoutState;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
    public Page<CheckoutResponseDto> getCheckoutList(int page, int size, String sort, String checkoutState) {

        //페이지당 주문 수 50개이상 불러올 때
        if (size >= 50) {
            throw new RuntimeException("올바르지 않은 접근");
        }


        Pageable pageable;

        //가격으로 sort
        if ("price_asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "checkoutTotalPay"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "checkoutTotalPay"));

            //주문 날짜별 sort
        } else if ("past".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        //주문 상태 sort
        Page<Checkout> checkoutList = null;
        if (checkoutState.equals("CONFIRM")) {
            checkoutList = checkoutRepository.findByCheckoutStep(CheckoutState.CONFIRM, pageable);
        } else if (checkoutState.equals("CANCEL")) {
            checkoutList = checkoutRepository.findByCheckoutStep(CheckoutState.CANCEL, pageable);
        } else if (checkoutState.equals("WAIT")) {
            checkoutList = checkoutRepository.findByCheckoutStep(CheckoutState.WAIT, pageable);
        } else {
            checkoutList = checkoutRepository.findAll(pageable);
        }

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
                productInfo = productName + "외 " + (length - 1) + "건";
            }


            return CheckoutResponseDto.builder()
                    .id(checkout.getId())
                    .checkoutCode(checkout.getCheckoutCode())
                    .checkoutProductName(productInfo)
                    .checkoutName(checkout.getMember().getName())
                    .checkoutRecipientName(checkout.getCheckoutName())
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutTotalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
                    .modifiedAt(Formatter.getLocalDate(checkout.getModifiedAt()))
                    .build();
        }));
    }


    /**
     * 관리자 검색 주문 리스트
     */
    public Page<CheckoutResponseDto> getCheckoutSearchList(String search, String keyword, int page, int size, String sort, String checkoutState) {

        //페이지당 주문 수 50개이상 불러올 때
        if (size >= 50) {
            throw new RuntimeException("올바르지 않은 접근");
        }


        Pageable pageable;

        if ("price_asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "checkoutTotalPay"));
        } else if ("price_desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "checkoutTotalPay"));
        } else if ("past".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        }

        if (keyword == null || keyword.isBlank()) return Page.empty();

        String trimKeyword = keyword.trim();

        Page<Checkout> checkoutList = null;

        //검색 기준이 주문자일때
        if ("ordererName".equals(search)) {
            //주문상태로 다시 sort
            if (checkoutState.equals("CONFIRM")) {
                checkoutList = checkoutRepository.findByMemberNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CONFIRM);
            } else if (checkoutState.equals("CANCEL")) {
                checkoutList = checkoutRepository.findByMemberNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CANCEL);
            } else if (checkoutState.equals("WAIT")) {
                checkoutList = checkoutRepository.findByMemberNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.WAIT);
            } else {
                checkoutList = checkoutRepository.findByMemberNameContainingWithoutSpace(trimKeyword, pageable);
            }
        }
        //검색 기준이 받는 분일때
        else if ("recipientName".equals(search)) {
            //주문상태로 다시 sort
            if (checkoutState.equals("CONFIRM")) {
                checkoutList = checkoutRepository.findByCheckoutNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CONFIRM);
            } else if (checkoutState.equals("CANCEL")) {
                checkoutList = checkoutRepository.findByCheckoutNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CANCEL);
            } else if (checkoutState.equals("WAIT")) {
                checkoutList = checkoutRepository.findByCheckoutNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.WAIT);
            } else {
                checkoutList = checkoutRepository.findByCheckoutNameContainingWithoutSpace(trimKeyword, pageable);
            }

        }
        //검색 기준이 상품명일때
        else if ("productName".equals(search)) {
            //주문상태로 다시 sort
            if (checkoutState.equals("CONFIRM")) {
                checkoutList = checkoutRepository.findByProductNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CONFIRM);
            } else if (checkoutState.equals("CANCEL")) {
                checkoutList = checkoutRepository.findByProductNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.CANCEL);
            } else if (checkoutState.equals("WAIT")) {
                checkoutList = checkoutRepository.findByProductNameContainingWithoutSpaceAndCheckoutStep(trimKeyword, pageable, CheckoutState.WAIT);
            } else {
                checkoutList = checkoutRepository.findByProductNameContainingWithoutSpace(trimKeyword, pageable);
            }

        } else {
            throw new RuntimeException("잘못된 접근");
        }


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
                productInfo = productName + "외 " + (length - 1) + "건";
            }


            return CheckoutResponseDto.builder()
                    .id(checkout.getId())
                    .checkoutCode(checkout.getCheckoutCode())
                    .checkoutProductName(productInfo)
                    .checkoutName(checkout.getMember().getName())
                    .checkoutRecipientName(checkout.getCheckoutName())
                    .checkoutPostStep(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                    .checkoutStep(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                    .checkoutTotalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                    .createdAt(Formatter.getLocalDate(checkout.getCreatedAt()))
                    .modifiedAt(Formatter.getLocalDate(checkout.getModifiedAt()))
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

        List<ProductListResponseDto> productListResponseDtos = new ArrayList<>();

        //주문 상세 리스트의 제품 찾기
        for (CheckoutDetails checkoutDetails1 : checkoutDetailsList) {

            //주문한 상품 수량을 BigDecimal 형변환
            BigDecimal productCount = new BigDecimal(checkoutDetails1.getCheckoutDetailCnt());


            ProductListResponseDto productListResponseDto = ProductListResponseDto.builder()
                    .id(checkoutDetails1.getProduct().getId())
                    .name(checkoutDetails1.getProduct().getName())
                    .count(checkoutDetails1.getCheckoutDetailCnt())
                    .price(Formatter.changeBigDecimalFormat(checkoutDetails1.getProduct().getPrice()))
                    .totalPrice(Formatter.changeBigDecimalFormat(checkoutDetails1.getProduct().getPrice().multiply(productCount))) //상품 총 가격 = 구매개수 X 상품 개당 가격
                    .mainImagePath(productImageManager.createImageUrl(checkoutDetails1.getProduct().getProductImage().getImageFullName()))
                    .build();

            productListResponseDtos.add(productListResponseDto);
        }


        return CheckoutDetailsResponseDto.builder()
                .id(checkout.getId())
                .checkoutCode(checkout.getCheckoutCode())
                .checkoutState(Formatter.getCheckoutState(checkout.getCheckoutStep()))
                .deliveryCompany(Formatter.getDeliveryCompany(checkout.getCheckoutDeliveryCompany()))
                .deliveryState(Formatter.getDeliveryState(checkout.getCheckoutPostStep()))
                .checkoutDate(Formatter.getLocalDate(checkout.getCreatedAt()))
                .deliveryCode(checkout.getCheckoutDeliveryCode())
                .totalPay(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay()))
                .totalProductCost(Formatter.changeBigDecimalFormat(checkout.getCheckoutTotalPay().subtract(checkout.getCheckoutDeliveryCost())))
                .deliveryCost(Formatter.changeBigDecimalFormat(checkout.getCheckoutDeliveryCost()))
                .cardCode(Formatter.changeCardNumber(checkout.getCheckoutCardNum()))
                .productList(productListResponseDtos)
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
            for (CheckoutDetails checkoutDetails1 : checkoutDetails) {

                //주문한 상품 수량을 BigDecimal 형변환
                BigDecimal productCount = new BigDecimal(checkoutDetails1.getCheckoutDetailCnt());


                ProductListResponseDto productListResponseDto = ProductListResponseDto.builder()
                        .id(checkoutDetails1.getProduct().getId())
                        .name(checkoutDetails1.getProduct().getName())
                        .price(Formatter.changeBigDecimalFormat(checkoutDetails1.getProduct().getPrice()))
                        .count(checkoutDetails1.getCheckoutDetailCnt())
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
        //id기준 내림차순
        checkoutResponseDtos.sort(Comparator.comparing(CheckoutResponseDto::getId).reversed());
        return checkoutResponseDtos;
    }
}
