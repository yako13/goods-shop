package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.cart.CartCheckoutDto;
import Spring.Goods_Shop.dto.checkout.HanPart.*;
import Spring.Goods_Shop.dto.product.Hanpart.ProductCheckoutResDto;
import Spring.Goods_Shop.entity.*;
import Spring.Goods_Shop.enums.CheckoutState;
import Spring.Goods_Shop.enums.DeliveryCompany;
import Spring.Goods_Shop.enums.DeliveryState;
import Spring.Goods_Shop.repository.*;
import Spring.Goods_Shop.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HanCheckoutService {

    private final MemberService memberService;
    private final HanDeliveryRepository hanDeliveryRepository;
    private final HanPayRepository hanPayRepository;
    private final HanCheckoutRepository hanCheckoutRepository;
    private final ProductRepository productRepository;
    private final CheckoutDetailsRepository checkoutDetailsRepository;
    private final HanCartRepository hanCartRepository;


    //장바구니에서 주문 결제로
    public CheckoutCartPageResponseDto prepareCheckout(HttpServletRequest request, CartCheckoutDto form) {

        Member memberEntity = memberService.getMemberEntity(request);

        //맴버 pk추출
        Long memberPk = memberEntity.getId();

        List<Cart> CartEntityCheckList = new ArrayList<>();

        List<Long> idList = form.getCartIdList();

        for (int i = 0; i < idList.size(); i++) {

            Cart cart = hanCartRepository.findById(idList.get(i)).orElse(null);

            if (cart == null) {

                return null;
            }

            CartEntityCheckList.add(cart);

        }

        List<CheckoutCartDto> CheckoutCartDtoList = new ArrayList<>();

        //페이지에서 장바구니에 담았던 물건의 총 가격 합
        BigDecimal totalCartSumPrise = BigDecimal.ZERO;


        //장바구니 엔티티를 주문 결제에서 사용할수 있게 변환
        for (Cart cart : CartEntityCheckList) {

            //장바구니의 구매 개수보다 물건 재고가 작을경우 실패
            int count = cart.getProduct().getCount() - cart.getCartCnt();

            if (count <= 0) {

                return null;
            }

            //상품 가격
            BigDecimal price = cart.getProduct().getPrice();

            //상품 합계 가격 ( 상품 가격 * 상품 개수)
            BigDecimal sumPrice = price.multiply(BigDecimal.valueOf(cart.getCartCnt()));

//            //기본 이미지 경로
//            String productImgUrl = "noImage.png";
//
//            if(cart.getProduct().getProductImage() !=null) {
//
//
//                //대표 상품 이미지 uuid 와 파일 확장자를 합쳐줌
//              productImgUrl = ( cart.getProduct().getProductImage().getUuid()) +
//                      (cart.getProduct().getProductImage().getFileExtension()) ;
//
//            }

            CheckoutCartDto checkoutCartDto = CheckoutCartDto.builder()
                    //장바구니 pk
                    .CartId(cart.getId())
                    //상품 url
                    .productImage(cart.getProduct().getProductImage())
                    //상품 이름
                    .productName(cart.getProduct().getName())
                    //상품 가격  (Formatter를 통해 원단위 랑 원 을 붙여준다)
                    .productPrise(Formatter.changeBigDecimalFormat(price))
                    //상품 합계 가격 ( 상품 가격 * 상품 개수) (Formatter를 통해 원단위 랑 원 을 붙여준다)
                    .productSumPrise(Formatter.changeBigDecimalFormat(sumPrice))
                    //상품 개수
                    .cartCnt(cart.getCartCnt())

                    .build();

            CheckoutCartDtoList.add(checkoutCartDto);

            totalCartSumPrise = totalCartSumPrise.add(sumPrice);


        }
//        compareTo() 설명
//        a.compareTo(b)의 반환값:
//        -1 → a < b (작음)
//        0 → a == b (같음)
//        1 → a > b (큼)

        //배송비 0원 으로 선원
        BigDecimal DeliveryCost = BigDecimal.ZERO;

        //배송비 무료 기준 (5만원)
        BigDecimal FreeDelivery = BigDecimal.valueOf(50000);

        //장바구니에 담았던 물건의 총 가격 합 5만원 미만일 경우 배송비 3천원 부과
        if (totalCartSumPrise.compareTo(FreeDelivery) < 0) {

            DeliveryCost = BigDecimal.valueOf(3000);
        }

        //총 결제 가격
        BigDecimal totalPaySumPrise = totalCartSumPrise.add(DeliveryCost);

        //배송비에 Formatter 로 원 단위 붙여줌
        String DeliveryCostString = Formatter.changeBigDecimalFormat(DeliveryCost);

        //페이지에서 장바구니에 담았던 물건의 총 가격 합 에  Formatter 로 원 단위 붙여줌
        String totalCartSumPriseString = Formatter.changeBigDecimalFormat(totalCartSumPrise);

        //총 결제 가격에 Formatter 로 원 단위 붙여줌
        String totalPaySumPriseString = Formatter.changeBigDecimalFormat(totalPaySumPrise);


        //memberEntity.getDeliveryList() 가 null경우 안전하게 빈리스트로 넘겨줌
        List<Delivery> deliveryList = Optional.ofNullable(memberEntity.getDeliveryList()).orElse(Collections.emptyList());

        List<CheckoutDeliveryResponseDto> tempDeliveryDtoList = new ArrayList<>();

        //배송지 목록을 dto 리스트로 변환시킴
        if (!deliveryList.isEmpty()) {

            //폰번호에 하이폰을 붙여주기위해 모든 배송지목록을 dto로 변환
            for (Delivery delivery : deliveryList) {

                CheckoutDeliveryResponseDto tempDeliveryDto = CheckoutDeliveryResponseDto.builder()

                        .id(delivery.getId())
                        .member(delivery.getMember())
                        .name(delivery.getName())
                        .postalCode(delivery.getPostalCode())
                        .address(delivery.getAddress())
                        .addressDetail(delivery.getAddressDetail())
                        .recipientName(delivery.getRecipientName())

                        //폰번호에 하이폰을 붙여줌
                        .recipientPhoneNumber(Formatter.changePhoneNumber(delivery.getRecipientPhoneNumber()))
                        .memo(delivery.getMemo())
                        .defaultDelivery(delivery.isDefaultDelivery())

                        .build();

                tempDeliveryDtoList.add(tempDeliveryDto);

            }

        }


        //기본배송지(defaultAdd = true) 인 주소록을 검색후 담아준다
        Delivery defaultDelivery = hanDeliveryRepository.findByMemberIdAndDefaultDelivery(memberEntity.getId(), true);


        //memberEntity.getPayList() 가 null경우 안전하게 빈리스트로 넘겨줌
        List<Pay> PayList = Optional.ofNullable(memberEntity.getPayList()).orElse(Collections.emptyList());

        List<CheckoutPayResponseDto> tempPayDtoList = new ArrayList<>();

        //결제 카드 목록을 dto 리스트로 변환시킴
        if (!PayList.isEmpty()) {


            for (Pay pay : PayList) {

                CheckoutPayResponseDto tempPayDto = CheckoutPayResponseDto.builder()

                        .id(pay.getId())
                        .member(pay.getMember())
                        .nickname(pay.getNickname())
                        //카드 번호 뒷자리 *하고 하이폰 붙여줌
                        .number(Formatter.CardNumFormat(pay.getNumber()))
                        .expPeriod(pay.getExpPeriod())
                        .cvc(pay.getCvc())
                        .defaultCard(pay.isDefaultCard())

                        .build();

                tempPayDtoList.add(tempPayDto);

            }

        }

        //기본 결제카드(defaultCard = true) 인 결제카드를 검색후 담아준다
        Pay defaultPay = hanPayRepository.findByMemberIdAndDefaultCard(memberEntity.getId(), true);


        return new CheckoutCartPageResponseDto(CheckoutCartDtoList, tempDeliveryDtoList, tempPayDtoList, defaultDelivery, defaultPay, DeliveryCostString, totalCartSumPriseString, totalPaySumPriseString, memberPk);
    }

    // 주문 /결제 페이지 왔을때 배송지 등록
    public CheckoutDeliveryResponseDto checkoutDeliveryNew(HttpServletRequest request, CheckoutDeliveryDto form) {

        Member memberEntity = memberService.getMemberEntity(request);

        int defaultDelivery = 0;

        //배송지 목록이 있을경우 기본 배송지가 있는지 유무 검사
        if (!memberEntity.getDeliveryList().isEmpty()) {

            List<Delivery> deliveryList = memberEntity.getDeliveryList();

            for (Delivery delivery : deliveryList) {

                if (delivery.isDefaultDelivery()) {

                    defaultDelivery = 1;
                }

            }

            //배송지 목록이 3이상일 경우 등록 못하게 막음
            if (deliveryList.size() > 2) {

                return null;

            }


        }

        //form이 null일 경우
        if (form == null) {

            return null;

        }
        //기본 배송지가 중복될경우
        if (defaultDelivery == 1 && form.isDefaultDelivery()) {

            return null;

        }

        Delivery deliveryEntity = Delivery.builder()
                //맴버 객체
                .member(memberEntity)
                //배송지 명
                .name(form.getName())
                //우편번호
                .postalCode(form.getPostalCode())
                //주소
                .address(form.getAddress())
                //상세주소
                .addressDetail(form.getAddressDetail())
                //수신자 이름
                .recipientName(form.getRecipientName())
                //수신자 전화번호
                .recipientPhoneNumber(form.getRecipientPhoneNumber())
                //배송 요청 사항
                .memo(form.getMemo())
                //기본 배송지 여부
                .defaultDelivery(form.isDefaultDelivery())


                .build();

        Delivery delivery = hanDeliveryRepository.save(deliveryEntity);

        if (delivery == null) {

            return null;

        }

        //메인페이지에 등록한 배송지를 보여주기위해 dto로 변환
        CheckoutDeliveryResponseDto deliveryResponseDto = CheckoutDeliveryResponseDto.builder()
                //배송지 pk
                .id(delivery.getId())
                //맴버 객체
                .member(delivery.getMember())
                //배송지 명
                .name(delivery.getName())
                //우편번호
                .postalCode(delivery.getPostalCode())
                //주소
                .address(delivery.getAddress())
                //상세주소
                .addressDetail(delivery.getAddressDetail())
                //수신자 이름
                .recipientName(delivery.getRecipientName())
                //수신자 전화번호에 하이폰 붙여줌
                .recipientPhoneNumber(Formatter.changePhoneNumber(delivery.getRecipientPhoneNumber()))
                //배송 요청 사항
                .memo(delivery.getMemo())
                //기본 배송지 여부
                .defaultDelivery(delivery.isDefaultDelivery())


                .build();

        //장바구니에서 배송지를 등록할시 장바구니 pk를 넣어준다
        if (form.getCartIdList() != null && !form.getCartIdList().isEmpty()) {
            //장바구니 pk 리스트 (주문결제 페이지로 리다이렉트 하기위해 넣어준다)
            deliveryResponseDto.setCartIdList(form.getCartIdList());
        }


        return deliveryResponseDto;
    }

    // 주문 결제 페이지 왔을때 카드 등록
    public CheckoutPayResponseDto checkoutPayNew(HttpServletRequest request, CheckoutPayDto form) {

        Member memberEntity = memberService.getMemberEntity(request);

        if (form == null) {

            return null;
        }

        if (form.getCardNum().isEmpty()) {

            return null;
        }

        if (form.getExpPeriod().isEmpty()) {

            return null;
        }

        int defaultCard = 0;

        //결제 카드 목록에 기본 결제카드가 있는지 확인
        if (!memberEntity.getPayList().isEmpty()) {

            List<Pay> payList = memberEntity.getPayList();

            for (Pay tmpPay : payList) {

                if (tmpPay.isDefaultCard()) {

                    defaultCard = 1;
                }

            }

            //결제 카드가 3개 이상인데 등록을 시도할 경우 실패
            if (payList.size() > 2) {

                return null;

            }


        }
        //기본 결제카드가 이미 목록에 있는데 기본 결제카드를 추가할려고한경우 실패처리
        if (defaultCard == 1 && form.isDefaultCard()) {

            return null;
        }


        //카드번호 리스트 -> String화
        StringBuilder cardNumber = new StringBuilder();

        for (String cardNum : form.getCardNum()) {
            cardNumber.append(cardNum);
        }


        //카드유효기간 리스트-> String화
        StringBuilder cardExpPeriod = new StringBuilder();

        for (String cardExp : form.getExpPeriod()) {
            cardExpPeriod.append(cardExp);
        }

        Pay payEntity = Pay.builder()
                //맴버 객체
                .member(memberEntity)
                //카드 별칭
                .nickname(form.getNickName())
                //카드 번호
                .number(String.valueOf(cardNumber))
                //카드 유효기간
                .expPeriod(String.valueOf(cardExpPeriod))
                //카드 cvc
                .cvc(form.getCvc())
                //기본 결제 카드 여부
                .defaultCard(form.isDefaultCard())

                .build();

        Pay pay = hanPayRepository.save(payEntity);

        if (pay == null) {

            return null;

        }

        CheckoutPayResponseDto payResponseDto = CheckoutPayResponseDto.builder()
                //결제 카드 pk
                .id(pay.getId())
                //맴버 정보
                .member(pay.getMember())
                //카드 별칭
                .nickname(pay.getNickname())
                //카드 번호 (Formatter로 하이폰 과 뒷자리 *로 바꾼다)
                .number(Formatter.CardNumFormat(pay.getNumber()))
                //카드 유효기간
                .expPeriod(pay.getExpPeriod())
                //카드 CVC 코드
                .cvc(pay.getCvc())
                //기본 결제 카드 여부
                .defaultCard(pay.isDefaultCard())

                .build();

        //장바구니에서 배송지를 등록할시 장바구니 pk를 넣어준다
        if (form.getCartIdList() != null && !form.getCartIdList().isEmpty()) {
            //장바구니 pk 리스트 (주문결제 페이지로 리다이렉트 하기위해 넣어준다)
            payResponseDto.setCartIdList(form.getCartIdList());
        }


        return payResponseDto;
    }

    //장바구니에서 주문 / 결제 엔티티 만들어주는 서비스
    public CheckoutCompleteResDto checkoutCartSubmit(HttpServletRequest request, CheckoutSubmitDto form) {

        Member memberEntity = memberService.getMemberEntity(request);

        if (form == null) {

            return null;
        }

        //form 에서 온 맴버 pk와 현재 세션 의 맴버 pk가 같은지 검사
        if (!Objects.equals(memberEntity.getId(), form.getMemberPk())) {

            return null;
        }

        //pk로 배송지와 결제 정보를 가져옴
        Delivery deliveryEntity = hanDeliveryRepository.findById(form.getDeliveryPk()).orElse(null);
        Pay payEntity = hanPayRepository.findById(form.getPayPk()).orElse(null);

        //둘중 하나라도 정보가 없을경우 실패
        if (deliveryEntity == null || payEntity == null) {

            return null;
        }

        if (memberEntity.getCartList().isEmpty()) {

            return null;

        }

        //form에서 온 장바구니 pk로 장바구니 엔티티를 가져온다
        List<Long> cartPkList = form.getCartPk();
        List<Cart> cartList = new ArrayList<>();


        for (int i = 0; i < cartPkList.size(); i++) {

            Cart cart = hanCartRepository.findById(cartPkList.get(i)).orElse(null);

            if (cart == null) {

                return null;
            }

            cartList.add(cart);

        }


        //합계가격 0원 선언
        BigDecimal checkoutTotalPay = BigDecimal.ZERO;

        //배송비 0원 으로 선언
        BigDecimal DeliveryCost = BigDecimal.ZERO;


        Checkout checkout = Checkout.builder()

                //맴버 정보
                .member(memberEntity)
                //주문번호
                .checkoutCode(Formatter.getCheckoutCode(LocalDateTime.now()))
                //주문자 명
                .checkoutName(deliveryEntity.getRecipientName())
                //주문자 배송지명
                .checkoutDeliveryName(deliveryEntity.getName())
                //주문자 우편번호
                .checkoutZipCode(deliveryEntity.getPostalCode())
                //주문자 도로명,지번 주소 + 상세주소
                .checkoutAddress(deliveryEntity.getAddress() + deliveryEntity.getAddressDetail())
                //주문자 배송 메모
                .checkoutDeliveryMemo(deliveryEntity.getMemo())
                //결제한 카드명
                .checkoutCardName(payEntity.getNickname())
                //결제한 카드 번호
                .checkoutCardNum(payEntity.getNumber())
                //결제한 카드 cvc
                .checkoutCardCvc(payEntity.getCvc())
                //결제한 카드 유효기간
                .checkoutExpPeriod(payEntity.getExpPeriod())
                //택배사 명 (대한통운이 기본값)
                .checkoutDeliveryCompany(DeliveryCompany.CJ)
                //배송 상태
                .checkoutPostStep(DeliveryState.PENDING)
                //운송장 번호
                .checkoutDeliveryCode(Formatter.generateDeliveryCode())
                //주문 상태
                .checkoutStep(CheckoutState.CONFIRM)
                //총 결제 금액
                .checkoutTotalPay(checkoutTotalPay)
                //배송비
                .checkoutDeliveryCost(DeliveryCost)
                //주문자 연락처
                .checkoutPhoneNumber(deliveryEntity.getRecipientPhoneNumber())

                .build();

        List<CheckoutDetails> checkoutDetailList = new ArrayList<>();

        for (Cart cart : cartList) {

            Product productEntity = cart.getProduct();

            BigDecimal productPrice = productEntity.getPrice();

            int cartCnt = cart.getCartCnt();

            //구매한 만큼 상품 재고 감소시키는 메서드
            boolean checkValue = checkoutCountMinus(productEntity, cartCnt);

            //상품재고 부족시
            if (!checkValue) {
                log.info("상품 재고가 부족합니다");

                return null;
            }


            CheckoutDetails checkoutDetail = CheckoutDetails.builder()

                    .product(productEntity)
                    .checkout(checkout)
                    .member(memberEntity)
                    .checkoutDetailPrice(productPrice)
                    .checkoutDetailCnt(cartCnt)
                    .build();

            //상품 판매개수 추가
            checkoutDetail.getProduct().setSellingCount(checkoutDetail.getProduct().getSellingCount() + cart.getCartCnt());

            BigDecimal productCount = new BigDecimal(cartCnt);

            checkoutTotalPay = checkoutTotalPay.add(productPrice.multiply(productCount));

            checkoutDetailList.add(checkoutDetail);

        }


        //배송비 무료 기준 (5만원)
        BigDecimal FreeDelivery = BigDecimal.valueOf(50000);

        //장바구니에 담았던 물건의 총 가격 합 5만원 미만일 경우 배송비 3천원 부과
        if (checkoutTotalPay.compareTo(FreeDelivery) < 0) {

            DeliveryCost = BigDecimal.valueOf(3000);

        }
        //상품 총가격에 배송비를 더해준다
        checkoutTotalPay = checkoutTotalPay.add(DeliveryCost);


        //연관관계 맵핑으로 알아서 저장
        checkout.setCheckoutDetailsList(checkoutDetailList);

        //총 결제 금액 다시 넣어줌
        checkout.setCheckoutTotalPay(checkoutTotalPay);

        //배송비 다시 넣어줌
        checkout.setCheckoutDeliveryCost(DeliveryCost);

        //주문 결제 저장
        Checkout checkoutEntity = hanCheckoutRepository.save(checkout);

        //주문 결제 dto 만든뒤 넣어준다
        CheckoutCompleteResDto checkoutCompleteResDto = new CheckoutCompleteResDto();

        checkoutCompleteResDto.setCheckout(checkoutEntity);

        //카드 번호 뒷자리 *로 처리해서 담아준다
        checkoutCompleteResDto.setCheckoutCardNum(Formatter.CardNumFormat(payEntity.getNumber()));

        //전화번호를 하이폰 처리해서 담아준다
        checkoutCompleteResDto.setCheckoutPhoneNumber(Formatter.changePhoneNumber(checkoutEntity.getCheckoutPhoneNumber()));

        //주문할때 사용한 장바구니 비우기
        hanCartRepository.checkoutSubmitCartDelete(cartPkList);

        return checkoutCompleteResDto;
    }


    //상품 상세페이지에서 주문 / 결제 페이지로 가는 서비스
    public CheckoutPageResDto checkoutPage(HttpServletRequest request, ProductCheckoutResDto form) {


        Member memberEntity = memberService.getMemberEntity(request);

        Long memberPk = memberEntity.getId();

        if (form == null) {

            return null;
        }


        if (form.getProductPk() == null) {

            return null;
        }

        Product product = productRepository.findById(form.getProductPk()).orElse(null);

        if (product == null) {
            return null;
        }

        //구매할려는 상품개수가 상품 재고보다 클경우 실패
        int count = product.getCount() - form.getProductCnt();

        if (count < 0) {

            return null;
        }


        //상품 가격
        BigDecimal price = product.getPrice();

        //상품 합계 가격 ( 상품 가격 * 상품 개수)
        BigDecimal sumPrice = price.multiply(BigDecimal.valueOf(form.getProductCnt()));


        CheckoutCartDto checkoutCartDto = CheckoutCartDto.builder()
                //상품 url
                .productImage(product.getProductImage())
                //상품 이름
                .productName(product.getName())
                //상품 가격  (Formatter를 통해 원단위 랑 원 을 붙여준다)
                .productPrise(Formatter.changeBigDecimalFormat(price))
                //상품 합계 가격 ( 상품 가격 * 상품 개수) (Formatter를 통해 원단위 랑 원 을 붙여준다)
                .productSumPrise(Formatter.changeBigDecimalFormat(sumPrice))
                //상품 개수
                .cartCnt(form.getProductCnt())


                .build();


//        compareTo() 설명
//        a.compareTo(b)의 반환값:
//        -1 → a < b (작음)
//        0 → a == b (같음)
//        1 → a > b (큼)

        //배송비 0원 으로 선원
        BigDecimal DeliveryCost = BigDecimal.ZERO;

        //배송비 무료 기준 (5만원)
        BigDecimal FreeDelivery = BigDecimal.valueOf(50000);

        // 물건의 총 가격 합 5만원 미만일 경우 배송비 3천원 부과
        if (sumPrice.compareTo(FreeDelivery) < 0) {

            DeliveryCost = BigDecimal.valueOf(3000);
        }

        //총 결제 가격
        BigDecimal totalPaySumPrise = sumPrice.add(DeliveryCost);

        //배송비에 Formatter 로 원 단위 붙여줌
        String DeliveryCostString = Formatter.changeBigDecimalFormat(DeliveryCost);

        //상품 합계 가격 에  Formatter 로 원 단위 붙여줌
        String totalCartSumPriseString = Formatter.changeBigDecimalFormat(sumPrice);

        //총 결제 가격에 Formatter 로 원 단위 붙여줌
        String totalPaySumPriseString = Formatter.changeBigDecimalFormat(totalPaySumPrise);


        //memberEntity.getDeliveryList() 가 null경우 안전하게 빈리스트로 넘겨줌
        List<Delivery> deliveryList = Optional.ofNullable(memberEntity.getDeliveryList()).orElse(Collections.emptyList());

        List<CheckoutDeliveryResponseDto> tempDeliveryDtoList = new ArrayList<>();

        //배송지 목록을 dto 리스트로 변환시킴
        if (!deliveryList.isEmpty()) {

            //폰번호에 하이폰을 붙여주기위해 모든 배송지목록을 dto로 변환
            for (Delivery delivery : deliveryList) {

                CheckoutDeliveryResponseDto tempDeliveryDto = CheckoutDeliveryResponseDto.builder()

                        .id(delivery.getId())
                        .member(delivery.getMember())
                        .name(delivery.getName())
                        .postalCode(delivery.getPostalCode())
                        .address(delivery.getAddress())
                        .addressDetail(delivery.getAddressDetail())
                        .recipientName(delivery.getRecipientName())

                        //폰번호에 하이폰을 붙여줌
                        .recipientPhoneNumber(Formatter.changePhoneNumber(delivery.getRecipientPhoneNumber()))
                        .memo(delivery.getMemo())
                        .defaultDelivery(delivery.isDefaultDelivery())

                        .build();

                tempDeliveryDtoList.add(tempDeliveryDto);

            }

        }


        //기본배송지(defaultAdd = true) 인 주소록을 검색후 담아준다
        Delivery defaultDelivery = hanDeliveryRepository.findByMemberIdAndDefaultDelivery(memberEntity.getId(), true);


        //memberEntity.getPayList() 가 null경우 안전하게 빈리스트로 넘겨줌
        List<Pay> PayList = Optional.ofNullable(memberEntity.getPayList()).orElse(Collections.emptyList());

        List<CheckoutPayResponseDto> tempPayDtoList = new ArrayList<>();

        //결제 카드 목록을 dto 리스트로 변환시킴
        if (!PayList.isEmpty()) {


            for (Pay pay : PayList) {

                CheckoutPayResponseDto tempPayDto = CheckoutPayResponseDto.builder()

                        .id(pay.getId())
                        .member(pay.getMember())
                        .nickname(pay.getNickname())
                        //카드 번호 뒷자리 *하고 하이폰 붙여줌
                        .number(Formatter.CardNumFormat(pay.getNumber()))
                        .expPeriod(pay.getExpPeriod())
                        .cvc(pay.getCvc())
                        .defaultCard(pay.isDefaultCard())

                        .build();

                tempPayDtoList.add(tempPayDto);

            }

        }

        //기본 결제카드(defaultCard = true) 인 결제카드를 검색후 담아준다
        Pay defaultPay = hanPayRepository.findByMemberIdAndDefaultCard(memberEntity.getId(), true);


        return new CheckoutPageResDto(checkoutCartDto, tempDeliveryDtoList, tempPayDtoList, defaultDelivery, defaultPay, DeliveryCostString, totalCartSumPriseString, totalPaySumPriseString, memberPk, form);
    }


    //(상품상세페이지에서 온) 주문 / 결제 엔티티 만들어주는 서비스
    public CheckoutCompleteResDto checkoutSubmit(HttpServletRequest request, CheckoutSubmitDto form) {

        Member memberEntity = memberService.getMemberEntity(request);

        if (form == null) {

            return null;
        }

        //form 에서 온 맴버 pk와 현재 세션 의 맴버 pk가 같은지 검사
        if (!Objects.equals(memberEntity.getId(), form.getMemberPk())) {

            return null;
        }

        //pk로 배송지와 결제 정보를 가져옴
        Delivery deliveryEntity = hanDeliveryRepository.findById(form.getDeliveryPk()).orElse(null);
        Pay payEntity = hanPayRepository.findById(form.getPayPk()).orElse(null);

        //둘중 하나라도 정보가 없을경우 실패
        if (deliveryEntity == null || payEntity == null) {

            return null;
        }


        //상품 총 가격 0원 선언
        BigDecimal checkoutTotalPay = BigDecimal.ZERO;

        //배송비 0원 으로 선언
        BigDecimal DeliveryCost = BigDecimal.ZERO;


        //상품  pk와 개수가 0일시 실패
        if (form.getProductPk() == null || form.getProductCnt() == 0) {

            return null;
        }

        //상품 정보
        Product product = productRepository.findById(form.getProductPk()).orElse(null);

        if (product == null) {

            return null;
        }

        //상품 가격
        BigDecimal productPrice = product.getPrice();

        //상품개수
        int productCnt = form.getProductCnt();

        //구매한 만큼 상품 재고 감소시키는 메서드
        boolean checkValue = checkoutCountMinus(product, productCnt);

        //상품재고 부족시
        if (!checkValue) {
            log.info("상품 재고가 부족합니다");

            return null;
        }

        //상품 총 가격(총 결제 가격) = 상품 가격 * 상품개수 + (배송비)
        checkoutTotalPay = productPrice.multiply(BigDecimal.valueOf(productCnt));


        //배송비 무료 기준 (5만원)
        BigDecimal FreeDelivery = BigDecimal.valueOf(50000);

        //장바구니에 담았던 물건의 총 가격 합 5만원 미만일 경우 배송비 3천원 부과
        if (checkoutTotalPay.compareTo(FreeDelivery) < 0) {

            DeliveryCost = BigDecimal.valueOf(3000);

        }
        //상품 총가격에 배송비를 더해준다
        checkoutTotalPay = checkoutTotalPay.add(DeliveryCost);


        Checkout checkout = Checkout.builder()

                //맴버 정보
                .member(memberEntity)
                //주문번호
                .checkoutCode(Formatter.getCheckoutCode(LocalDateTime.now()))
                //주문자 명
                .checkoutName(deliveryEntity.getRecipientName())
                //주문자 배송지명
                .checkoutDeliveryName(deliveryEntity.getName())
                //주문자 우편번호
                .checkoutZipCode(deliveryEntity.getPostalCode())
                //주문자 도로명,지번 주소 + 상세주소
                .checkoutAddress(deliveryEntity.getAddress() + deliveryEntity.getAddressDetail())
                //주문자 배송 메모
                .checkoutDeliveryMemo(deliveryEntity.getMemo())
                //결제한 카드명
                .checkoutCardName(payEntity.getNickname())
                //결제한 카드 번호
                .checkoutCardNum(payEntity.getNumber())
                //결제한 카드 cvc
                .checkoutCardCvc(payEntity.getCvc())
                //결제한 카드 유효기간
                .checkoutExpPeriod(payEntity.getExpPeriod())
                //택배사 명 (대한통운이 기본값)
                .checkoutDeliveryCompany(DeliveryCompany.CJ)
                //배송 상태
                .checkoutPostStep(DeliveryState.PENDING)
                //운송장 번호
                .checkoutDeliveryCode(Formatter.generateDeliveryCode())
                //주문 상태
                .checkoutStep(CheckoutState.CONFIRM)
                //총 결제 금액 (상품 총 가격 + 배송비)
                .checkoutTotalPay(checkoutTotalPay)
                //배송비
                .checkoutDeliveryCost(DeliveryCost)
                //주문자 연락처
                .checkoutPhoneNumber(deliveryEntity.getRecipientPhoneNumber())

                .build();


        CheckoutDetails checkoutDetail = CheckoutDetails.builder()

                .product(product)
                .checkout(checkout)
                .member(memberEntity)
                .checkoutDetailPrice(productPrice)
                .checkoutDetailCnt(productCnt)
                .build();
        //상품 판매개수 추가
        checkoutDetail.getProduct().setSellingCount(checkoutDetail.getProduct().getSellingCount() + productCnt);

        //주문 결제 저장
        Checkout checkoutEntity = hanCheckoutRepository.save(checkout);

        //주문 디테일 저장
        checkoutDetailsRepository.save(checkoutDetail);

        //주문 결제 dto 만든뒤 넣어준다
        CheckoutCompleteResDto checkoutCompleteResDto = new CheckoutCompleteResDto();

        checkoutCompleteResDto.setCheckout(checkoutEntity);

        //카드 번호 뒷자리 *로 처리해서 담아준다
        checkoutCompleteResDto.setCheckoutCardNum(Formatter.CardNumFormat(payEntity.getNumber()));

        //전화번호를 하이폰 처리해서 담아준다
        checkoutCompleteResDto.setCheckoutPhoneNumber(Formatter.changePhoneNumber(checkoutEntity.getCheckoutPhoneNumber()));

        log.info("전화번호" + checkoutCompleteResDto.getCheckoutPhoneNumber());


        return checkoutCompleteResDto;

    }

    //주문 목록 상세 페이지로 가는 서비스
    public CheckoutListDetailDto hanCheckoutListDetail(HttpServletRequest request, Long id) {

        Member memberEntity = memberService.getMemberEntity(request);

        if (memberEntity.getCheckoutList().isEmpty()) {

            return null;
        }

        //주문 pk로 주문 엔티티 들고오기
        Checkout checkout = hanCheckoutRepository.findById(id).orElse(null);

        if (checkout == null) {

            return null;
        }


        //주문에서 주문번호 배송지 결제 정보 추출

        CheckoutListDetailDto checkoutListDetailDto = CheckoutListDetailDto.builder()
                .checkout(checkout)

                .build();

        Checkout checkoutDto = checkoutListDetailDto.getCheckout();


        //주문 상세 리스트 추출
        List<CheckoutDetails> CheckoutDetailList = checkoutDto.getCheckoutDetailsList();

        //변환된 주문상세 dto를 담을 리스트
        List<CheckoutCartDto> checkoutCartDtoList = new ArrayList<>();

        //주문 상세의 상품 가격 전부 원단위 적어주기
        for (CheckoutDetails checkoutDetails : CheckoutDetailList) {

            //상품 가격
            BigDecimal price = checkoutDetails.getCheckoutDetailPrice();

            //상품 합계 가격 ( 상품 가격 * 상품 개수)
            BigDecimal sumPrice = price.multiply(BigDecimal.valueOf(checkoutDetails.getCheckoutDetailCnt()));

            //checkoutCartDto로 변환
            CheckoutCartDto checkoutCartDto = CheckoutCartDto.builder()
                    //상품 url
                    .productImage(checkoutDetails.getProduct().getProductImage())
                    //상품 이름
                    .productName(checkoutDetails.getProduct().getName())

                    //상품 합계 가격 ( 상품 가격 * 상품 개수) (Formatter를 통해 원단위 랑 원 을 붙여준다)
                    .productSumPrise(Formatter.changeBigDecimalFormat(sumPrice))
                    //상품 개수
                    .cartCnt(checkoutDetails.getCheckoutDetailCnt())


                    .build();

            checkoutCartDtoList.add(checkoutCartDto);

        }

        //주문 리스트 상세페이지에 보여줄 주문상세 dto리스트를 checkoutListDetailDto에 담아준다
        checkoutListDetailDto.setCheckoutCartDtoList(checkoutCartDtoList);

        //배송비 추출
        BigDecimal deliveryCost = checkoutDto.getCheckoutDeliveryCost();

        //총 결제 금액 추출
        BigDecimal totalPay = checkoutDto.getCheckoutTotalPay();

        //상품의 총 금액 = 총 결제 금액 - 배송비
        BigDecimal productSumPay = totalPay.subtract(deliveryCost);


        //배송비를 원단위로 변환해서 넣는다
        checkoutListDetailDto.setCheckoutDeliveryCost(Formatter.changeBigDecimalFormat(checkoutDto.getCheckoutDeliveryCost()));

        //총 상품 금액을 원단위로 변환해서 넣는다
        checkoutListDetailDto.setCheckoutProductSum(Formatter.changeBigDecimalFormat(productSumPay));

        //총 결제 금액을 원단위로 변환해서 넣는다
        checkoutListDetailDto.setCheckoutTotalPay(Formatter.changeBigDecimalFormat(totalPay));

        //전화번호 변환
        checkoutListDetailDto.setCheckoutPhoneNumber(Formatter.changePhoneNumber(checkoutDto.getCheckoutPhoneNumber()));


        //EnumType 변환

        //주문 상태
        checkoutListDetailDto.setCheckoutStep(Formatter.getDeliveryState(checkoutDto.getCheckoutPostStep()));
        //배송 상태
        checkoutListDetailDto.setCheckoutPostStep(Formatter.getCheckoutState(checkoutDto.getCheckoutStep()));
        //배송 회사
        checkoutListDetailDto.setDeliveryCompany(Formatter.getDeliveryCompany(checkoutDto.getCheckoutDeliveryCompany()));

        //주문 시간 뒷부분 잘라주기

        //주문 시간
        checkoutListDetailDto.setCheckoutCreatedAt(Formatter.getLocalDate(checkoutDto.getCreatedAt()));


        return checkoutListDetailDto;
    }


    //-----------------주문할때 재고 감소시키는 서비스------------------------

    public boolean checkoutCountMinus(Product product, int cnt) {

        //상품의 재고 - 주문 개수
        int count = product.getCount() - cnt;

        if (count < 0) {

            return false;
        }

        product.setCount(count);

        productRepository.save(product);


        return true;


    }


    //-------------------------배송지 삭제 api---------------------------------

    public void deleteDelivery(Long id) {
        if (!hanDeliveryRepository.existsById(id)) {
            throw new IllegalArgumentException("배송지가 존재하지 않습니다.");
        }
        hanDeliveryRepository.deleteById(id);
    }


    //-------------------------카드 삭제 api---------------------------------

    public void deletePayCard(Long id) {
        if (!hanPayRepository.existsById(id)) {
            throw new IllegalArgumentException("카드 정보가 존재하지 않습니다.");
        }
        hanPayRepository.deleteById(id);
    }


}
