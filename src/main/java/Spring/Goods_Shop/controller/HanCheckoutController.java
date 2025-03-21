package Spring.Goods_Shop.controller;

import Spring.Goods_Shop.dto.cart.CartCheckoutDto;
import Spring.Goods_Shop.dto.checkout.HanPart.*;
import Spring.Goods_Shop.dto.product.Hanpart.ProductCheckoutResDto;
import Spring.Goods_Shop.entity.Delivery;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.service.HanCheckoutService;
import Spring.Goods_Shop.service.MemberService;
import Spring.Goods_Shop.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HanCheckoutController {

    private final HanCheckoutService hanCheckoutService;

    private final MemberService memberService;

    @GetMapping("/checkout/cart")
    public String checkoutCartGo(HttpServletRequest request, Model model, CartCheckoutDto form,
                                 HttpSession session, RedirectAttributes rttr) {

        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId", member.getUserId());
        model.addAttribute("name", member.getName());

        //세션 값 가져옴
        CheckoutDeliveryResponseDto deliveryInfoNew = (CheckoutDeliveryResponseDto) session.getAttribute("deliveryNewCart");
        CheckoutPayResponseDto payInfoNew = (CheckoutPayResponseDto) session.getAttribute("payNewCart");

        CheckoutCartPageResponseDto checkoutCartPageDto = new CheckoutCartPageResponseDto();

        //신규 배송지 등록에서 왔거나 결제 카드에서 왔거나 장바구니에서 왔는지 체크
        int deliveryNew = 0;
        int payNew = 0;


        //배송지 등록을 했을경우
        if (deliveryInfoNew != null && deliveryInfoNew.getId() != null && (form.getCartIdList() == null || form.getCartIdList().isEmpty())) {

            deliveryNew = 1;

        }

        //결제 카드 등록을 했을경우
        if (payInfoNew != null && payInfoNew.getId() != null && (form.getCartIdList() == null || form.getCartIdList().isEmpty())) {

            payNew = 1;

        }


        //결제카드 등록이나 배송지 등록을 했을경우 form에 맞게 보내준다
        if (deliveryNew == 1 || payNew == 1) {

            List<Long> checkList = deliveryInfoNew.getCartIdList();

            if (checkList == null) {

                rttr.addFlashAttribute("data", "잘못된 접근 입니다");

                return "redirect:/";

            }

            log.info("장바구니 pk 개수 : " + checkList.size());

            for (int i = 0; i < checkList.size(); i++) {

                log.info("장바구니 pk  : " + checkList.get(i));
            }

            //서비스에 넣을수있게 형식을 맞춰준다
            CartCheckoutDto cartCheckoutDto = CartCheckoutDto.builder()
                    .cartIdList(checkList)
                    .build();

            //장바구니 목록을 CheckoutCartDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
            checkoutCartPageDto = hanCheckoutService.prepareCheckout(request, cartCheckoutDto);


            //상품 상세 페이지에서 올경우
        } else {

            if (form.getCartIdList() == null || form.getCartIdList().isEmpty()) {

                rttr.addFlashAttribute("data", "잘못된 접근 입니다");

                return "redirect:/";

            }

            List<Long> checkList = form.getCartIdList();


            log.info("장바구니 pk 개수 : " + checkList.size());

            for (int i = 0; i < checkList.size(); i++) {

                log.info("장바구니 pk  : " + checkList.get(i));

            }
            //장바구니 목록을 CheckoutCartDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
            checkoutCartPageDto = hanCheckoutService.prepareCheckout(request, form);

        }


        //오류 발생했을경우
        if (checkoutCartPageDto == null) {

            return "redirect:/";
        }


        //배송지 목록이 있는지 체크
        int deliveryEmptyCheck = 1;

        //결제 목록이 있는지 체크
        int payEmptyCheck = 1;

        //배송지 목록과 결제 목록이 없을경우
        if (checkoutCartPageDto.getDeliveryList().isEmpty() && checkoutCartPageDto.getPayList().isEmpty()) {

            model.addAttribute("deliveryEmpty", 1);

            model.addAttribute("payEmpty", 1);

            deliveryEmptyCheck = 0;
            payEmptyCheck = 0;


            //배송지 목록이 없을경우
        } else if (checkoutCartPageDto.getDeliveryList().isEmpty()) {

            model.addAttribute("deliveryEmpty", 1);

            deliveryEmptyCheck = 0;

            //결제 목록이 없을경우
        } else if (checkoutCartPageDto.getPayList().isEmpty()) {

            model.addAttribute("payEmpty", 1);

            payEmptyCheck = 0;

        }


        if (deliveryEmptyCheck == 1) {


            //신규 배송지 등록을 한상태
            if (deliveryNew == 1) {

                model.addAttribute("deliveryInfo", deliveryInfoNew);
                model.addAttribute("deliveryInfoPhone", deliveryInfoNew.getRecipientPhoneNumber());


                //신규 배송지 등록을 안 한 상태
            } else {

                //배송지 목록
                List<CheckoutDeliveryResponseDto> DeliveryListDto = checkoutCartPageDto.getDeliveryList();

                log.info("2222");


                if (checkoutCartPageDto.getDefaultDelivery() == null) {

                    CheckoutDeliveryResponseDto deliveryInfo = DeliveryListDto.get(0);

                    model.addAttribute("deliveryInfo", deliveryInfo);
                    model.addAttribute("deliveryInfoPhone", deliveryInfo.getRecipientPhoneNumber());

                    log.info("3333");

                    //기본 배송지가 있을경우
                } else {

                    Delivery deliveryInfo = checkoutCartPageDto.getDefaultDelivery();

                    model.addAttribute("deliveryInfo", deliveryInfo);
                    //기본배송지 목록의 휴대폰 번호에 하이폰을 넣어줌
                    model.addAttribute("deliveryInfoPhone",
                            Formatter.changePhoneNumber(deliveryInfo.getRecipientPhoneNumber()));


                    log.info("4444");
                }

            }

        }


        if (payEmptyCheck == 1) {

            //신규 배송지 등록을 한상태
            if (payNew == 1) {

                model.addAttribute("payInfo", payInfoNew);
                model.addAttribute("payInfoCardNum", payInfoNew.getNumber());

            } else {

                //결제 카드 목록
                List<CheckoutPayResponseDto> PayListDto = checkoutCartPageDto.getPayList();

                //기본 결제카드가 없고 결제카드가 하나라도 등록되어있을경우 등록된 결제카드의 맨 처음걸 보여준다
                if (checkoutCartPageDto.getDefaultPay() == null) {

                    model.addAttribute("payInfo", PayListDto.get(0));
                    model.addAttribute("payInfoCardNum", PayListDto.get(0).getNumber());


                    //기본 결제 카드가 있을경우
                } else {

                    model.addAttribute("payInfo", checkoutCartPageDto.getDefaultPay());
                    model.addAttribute("payInfoCardNum", Formatter.CardNumFormat(checkoutCartPageDto.getDefaultPay().getNumber()));

                }
            }

        }

        //장바구니에 담긴 상품 리스트
        List<CheckoutCartDto> checkoutCartDtoList = checkoutCartPageDto.getCheckoutCartDtoList();


        //목록 개수 담기
        int checkoutCartDtoListCnt = checkoutCartDtoList.size();

        model.addAttribute("checkoutCartDtoListCnt", checkoutCartDtoListCnt);

        //배송지 목록, 결제 카드 목록, 배송비, 장바구니의 상품 가격 총합(정가), 장바구니의 상품 가격 총합+ 배송비 (총 결제 가격),맴버 pk
        model.addAttribute("checkoutCartPageDto", checkoutCartPageDto);

//        log.info("배송지 : " + checkoutCartPageDto.getDeliveryList().get(0).getAddress());


        return "checkout/checkout";
    }


    // 장바구니에서 주문 / 결제 페이지 에서 배송지를 등록 했을 경우
    @PostMapping("/checkout/cart/delivery/submit")
    public String checkoutCartDeliverySubmit(HttpServletRequest request, CheckoutDeliveryDto form,
                                             RedirectAttributes rttr, HttpSession session) {

        //장바구니 목록을 CheckoutCartDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
        CheckoutDeliveryResponseDto checkoutCartPageDto = hanCheckoutService.checkoutDeliveryNew(request, form);

        // 다시 배송지 등록할때 먼저 세션에 정보가 있을 경우 삭제
        if (session.getAttribute("deliveryNewCart") != null) {

            session.removeAttribute("deliveryNewCart");
        }


        if (checkoutCartPageDto == null) {

            rttr.addFlashAttribute("data", "배송지 등록에 실패 했습니다");

            return "redirect:/checkout/cart";
        }

        //장바구니에서 주문 / 결제 페이지 호출 컨트롤러에 값을 주기위해 session 값을 넣어둔다
        //세션에 장바구니 pk 리스트 저장
        session.setAttribute("deliveryNewCart", checkoutCartPageDto);


        return "redirect:/checkout/cart";
    }

    // 장바구니에서 주문 / 결제 페이지 에서 결제 카드를 등록 했을 경우
    @PostMapping("/checkout/cart/pay/submit")
    public String checkoutCartPaySubmit(HttpServletRequest request, CheckoutPayDto form,
                                        RedirectAttributes rttr, HttpSession session) {

        //장바구니 목록을 CheckoutCartDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
        CheckoutPayResponseDto payResponseDto = hanCheckoutService.checkoutPayNew(request, form);

        // 다시 배송지 등록할때 먼저 세션에 정보가 있을 경우 삭제
        if (session.getAttribute("payNewCart") != null) {

            session.removeAttribute("payNewCart");
        }

        if (payResponseDto == null) {

            rttr.addFlashAttribute("data", "결제 카드 등록에 실패 했습니다");

            return "redirect:/checkout/cart";
        }

        //장바구니에서 주문 / 결제 페이지 호출 컨트롤러에 값을 주기위해 session 값을 넣어둔다
        //세션에 장바구니 pk 리스트 저장
        session.setAttribute("payNewCart", payResponseDto);

        return "redirect:/checkout/cart";
    }


    //장바구니에서 주문/결제 페이지로 간후 결제하기 버튼 누를때
    @PostMapping("/checkout/cart/submit")
    public String checkoutCartSubmit(HttpServletRequest request, CheckoutSubmitDto form, Model model, HttpSession session) {

        CheckoutCompleteResDto CheckoutCompleteDto = hanCheckoutService.checkoutCartSubmit(request, form);

        model.addAttribute("checkout", CheckoutCompleteDto);

        // 세션에 결제 카드 정보가 있을 경우 삭제
        if (session.getAttribute("payNewCart") != null) {

            session.removeAttribute("payNewCart");
        }

        // 세션에 배송지 정보가 있을 경우 삭제
        if (session.getAttribute("deliveryNewCart") != null) {

            session.removeAttribute("deliveryNewCart");
        }


        return "checkout/checkoutComplete";
    }


    // 상품 상세페이지에서 주문 / 결제 페이지로 이동
    @GetMapping("/checkout")
    public String checkoutGo1(HttpServletRequest request, Model model, ProductCheckoutResDto form
            , HttpSession session, RedirectAttributes rttr) {

        Member member = memberService.getMemberEntity(request);

        model.addAttribute("userId", member.getUserId());
        model.addAttribute("name", member.getName());
        //세션 값 가져옴
        CheckoutDeliveryResponseDto deliveryInfoNew = (CheckoutDeliveryResponseDto) session.getAttribute("deliveryNew");
        CheckoutPayResponseDto payInfoNew = (CheckoutPayResponseDto) session.getAttribute("payNew");

        //리다이렉트를 위한 dto 세션
        ProductCheckoutResDto redirectDto = (ProductCheckoutResDto) session.getAttribute("redirectDto");


        //신규 배송지 등록에서 왔거나 결제 카드에서 왔거나 장바구니에서 왔는지 체크
        int deliveryNew = 0;
        int payNew = 0;

        CheckoutPageResDto checkoutPageDto = new CheckoutPageResDto();     //상품 pk


        //배송지 등록을 했을경우
        if (deliveryInfoNew != null && deliveryInfoNew.getId() != null ) {

            deliveryNew = 1;

        }

        //결제 카드 등록을 했을경우
        if (payInfoNew != null && payInfoNew.getId() != null ) {

            payNew = 1;

        }


        //배송지 등록이나 카드 등록으로 다시 이 페이지로 리다이렉트 할경우 그 컨트롤러에서 상품 pk와 상품 개수를 받아온다
        if (redirectDto != null) {


            //상품을 CheckoutPageResDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
            checkoutPageDto = hanCheckoutService.checkoutPage(request, redirectDto);


        } else {

            //상품을 CheckoutPageResDto 로 변환해주고 기본 배송지와 결제카드를 찾아주고 목록도 찾아준다
            checkoutPageDto = hanCheckoutService.checkoutPage(request, form);


            if (form == null || form.getProductPk() == null) {

                rttr.addFlashAttribute("data", "잘못된 접근 입니다");

                return "redirect:/checkout/cart";

            }


        }

        //배송지 목록이 있는지 체크
        int deliveryEmptyCheck = 1;

        //결제 목록이 있는지 체크
        int payEmptyCheck = 1;


        //배송지 목록과 결제 목록이 없을경우
        if (checkoutPageDto.getDeliveryList().isEmpty() && checkoutPageDto.getPayList().isEmpty()) {

            model.addAttribute("deliveryEmpty", 1);

            model.addAttribute("payEmpty", 1);

            deliveryEmptyCheck = 0;
            payEmptyCheck = 0;


            //배송지 목록이 없을경우
        } else if (checkoutPageDto.getDeliveryList().isEmpty()) {

            model.addAttribute("deliveryEmpty", 1);

            deliveryEmptyCheck = 0;


            //결제 목록이 없을경우
        } else if (checkoutPageDto.getPayList().isEmpty()) {

            model.addAttribute("payEmpty", 1);

            payEmptyCheck = 0;


        }

        if (deliveryEmptyCheck == 1) {

            //배송지 목록
            List<CheckoutDeliveryResponseDto> DeliveryListDto = checkoutPageDto.getDeliveryList();

            log.info("2222");

            //배송지 신규 등록을 했을경우
            if (deliveryNew == 1) {

                model.addAttribute("deliveryInfo", deliveryInfoNew);
                model.addAttribute("deliveryInfoPhone", deliveryInfoNew.getRecipientPhoneNumber());


            } else {

                //기본 배송지가 없고 배송지가 하나라도 등록되어있을경우 등록된 배송지의 맨 처음걸 보여준다
                if (checkoutPageDto.getDefaultDelivery() == null) {

                    CheckoutDeliveryResponseDto deliveryInfo = DeliveryListDto.get(0);

                    model.addAttribute("deliveryInfo", deliveryInfo);
                    model.addAttribute("deliveryInfoPhone", deliveryInfo.getRecipientPhoneNumber());

                    log.info("3333");

                    //기본 배송지가 있을경우
                } else {

                    Delivery deliveryInfo = checkoutPageDto.getDefaultDelivery();

                    model.addAttribute("deliveryInfo", deliveryInfo);
                    //기본배송지 목록의 휴대폰 번호에 하이폰을 넣어줌
                    model.addAttribute("deliveryInfoPhone",
                            Formatter.changePhoneNumber(deliveryInfo.getRecipientPhoneNumber()));


                    log.info("4444");
                }
            }


        }


        if (payEmptyCheck == 1) {

            //결제 카드 목록
            List<CheckoutPayResponseDto> PayListDto = checkoutPageDto.getPayList();

            //결제카드 등록 모달에서 등록했을 경우
            if (payNew == 1) {
                model.addAttribute("payInfo", payInfoNew);
                model.addAttribute("payInfoCardNum", payInfoNew.getNumber());


            } else {

                                //기본 결제카드가 없고 결제카드가 하나라도 등록되어있을경우 등록된 결제카드의 맨 처음걸 보여준다
                if (checkoutPageDto.getDefaultPay() == null) {

                    model.addAttribute("payInfo", PayListDto.get(0));
                    model.addAttribute("payInfoCardNum", PayListDto.get(0).getNumber());


                    //기본 결제 카드가 있을경우
                } else {

                    model.addAttribute("payInfo", checkoutPageDto.getDefaultPay());
                    model.addAttribute("payInfoCardNum", Formatter.CardNumFormat(checkoutPageDto.getDefaultPay().getNumber()));

                }

            }

        }


        //상품 개수 (상세페이지에서 이동이기에 무조건 하나)
        int checkoutCartDtoListCnt = 1;

        model.addAttribute("checkoutDtoCnt", checkoutCartDtoListCnt);

        //배송지 목록, 결제 카드 목록, 배송비, 장바구니의 상품 가격 총합(정가), 장바구니의 상품 가격 총합+ 배송비 (총 결제 가격),맴버 pk
        model.addAttribute("checkoutPageDto", checkoutPageDto);


        return "checkout/checkoutProduct";
    }


    // 주문 / 결제 페이지 에서 배송지를 등록 했을 경우
    @PostMapping("/checkout/delivery/submit")
    public String checkoutDeliverySubmit(HttpServletRequest request, CheckoutDeliveryDto form, RedirectAttributes rttr,
                                         HttpSession session) {

        // 세션에 배송지 정보가 있을 경우 삭제
        if (session.getAttribute("deliveryNew") != null) {

            session.removeAttribute("deliveryNew");
        }

        //배송지 등록 서비스
        CheckoutDeliveryResponseDto checkoutCartPageDto = hanCheckoutService.checkoutDeliveryNew(request, form);

        if (checkoutCartPageDto == null) {

            rttr.addFlashAttribute("data", "배송지 등록에 실패 했습니다");

            return "redirect:/checkout";
        }

        // 주문 / 결제 페이지 호출 컨트롤러에 값을 주기위해 session 값을 넣어둔다
        //세션에 장바구니 pk 리스트 저장
        session.setAttribute("deliveryNew", checkoutCartPageDto);


        if (form.getProductPk() == null || form.getProductCnt() == 0) {

            rttr.addFlashAttribute("data", "오류가 발생했습니다 다시 주문 해주세요");
            return "redirect:/";

        }

        //리다이렉션으로 상품 pk와 상품 개수를 전해주기 위해 form에서 값 추출
        ProductCheckoutResDto redirectDto = ProductCheckoutResDto.builder()
                .productPk(form.getProductPk())
                .productCnt(form.getProductCnt())

                .build();

        //리다이렉션으로 상품 pk와 상품 개수를 전해주기 위해 session에 값을 넣어둔다
        session.setAttribute("redirectDto", redirectDto);


        return "redirect:/checkout";
    }

    // 주문 / 결제 페이지 에서 결제 카드를 등록 했을 경우
    @PostMapping("/checkout/pay/submit")
    public String checkoutPaySubmit(HttpServletRequest request, CheckoutPayDto form, RedirectAttributes rttr,
                                    HttpSession session) {

        // 세션에 결제 카드 정보가 있을 경우 삭제
        if (session.getAttribute("payNew") != null) {

            session.removeAttribute("payNew");
        }

        //주문 /결제에서 카드 등록 서비스
        CheckoutPayResponseDto payResponseDto = hanCheckoutService.checkoutPayNew(request, form);

        if (payResponseDto == null) {

            rttr.addFlashAttribute("data", "결제 카드 등록에 실패 했습니다");

            return "redirect:/checkout";
        }

        //장바구니에서 주문 / 결제 페이지 호출 컨트롤러에 값을 주기위해 session 값을 넣어둔다
        //세션에 장바구니 pk 리스트 저장
        session.setAttribute("payNew", payResponseDto);


        if (form.getProductPk() == null || form.getProductCnt() == 0) {

            rttr.addFlashAttribute("data", "오류가 발생했습니다 다시 주문 해주세요");
            return "redirect:/";

        }

        //리다이렉션으로 상품 pk와 상품 개수를 전해주기 위해 form에서 값 추출
        ProductCheckoutResDto redirectDto = ProductCheckoutResDto.builder()
                .productPk(form.getProductPk())
                .productCnt(form.getProductCnt())

                .build();

        //리다이렉션으로 상품 pk와 상품 개수를 전해주기 위해 session에 값을 넣어둔다
        session.setAttribute("redirectDto", redirectDto);

        return "redirect:/checkout";
    }


    // 주문/결제 페이지로 간후 결제하기 버튼 누를때
    @PostMapping("/checkout/submit")
    public String checkoutSubmit(HttpServletRequest request, CheckoutSubmitDto form, Model model,
                                 HttpSession session) {

        //결제 서비스
        CheckoutCompleteResDto CheckoutCompleteDto = hanCheckoutService.checkoutSubmit(request, form);

        model.addAttribute("checkout", CheckoutCompleteDto);


        // 세션에 배송지 정보가 있을 경우 삭제
        if (session.getAttribute("deliveryNew") != null) {

            session.removeAttribute("deliveryNew");
        }

        // 세션에 결제 카드 정보가 있을 경우 삭제
        if (session.getAttribute("payNew") != null) {

            session.removeAttribute("payNew");
        }

        // 세션에 상품 pk와 개수가 있을 경우 삭제
        if (session.getAttribute("redirectDto") != null) {

            session.removeAttribute("redirectDto");
        }


        return "checkout/checkoutComplete";
    }



}
