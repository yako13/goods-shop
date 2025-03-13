package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.member.PayDto;
import Spring.Goods_Shop.dto.member.PayResponseDto;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.entity.Pay;
import Spring.Goods_Shop.repository.MemberRepository;
import Spring.Goods_Shop.repository.PayRepository;
import Spring.Goods_Shop.util.Formatter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PayService {

    private final PayRepository payRepository;

    private final MemberRepository memberRepository;

    /**
     * 기본 결제 카드 여부
     */
    public boolean checkDefaultCard(PayDto payDto){
        Optional<Pay> optionalPay = payRepository.findByMemberIdAndDefaultCard(payDto.getMemberId(),true);

        //기본 결제 카드가 이미 있으면 true
        return optionalPay.isPresent();
    }

    public void savePay(PayDto payDto, HttpServletRequest request) {
        //등록하려고 하는 member PK와 로그인한 사용자PK 같은지 확인
        HttpSession session = request.getSession(false);

        if (!payDto.getMemberId().equals(session.getAttribute("memberId"))) throw new RuntimeException("올바르지 않은 접근");

        Optional<Member> optionalMember = memberRepository.findById(payDto.getMemberId());

        if (optionalMember.isEmpty()) throw new RuntimeException("해당 회원이 존재하지 않음");

        Member member = optionalMember.get();

        //카드번호 리스트 -> String화
        StringBuilder cardNumber = new StringBuilder();
        for (String cardNum : payDto.getCardNum()) {
            cardNumber.append(cardNum);
        }

        //카드유효기간 리스트-> String화
        StringBuilder cardExpPeriod = new StringBuilder();
        for (String cardExp : payDto.getExpPeriod()) {
            cardExpPeriod.append(cardExp);
        }

        Pay pay = Pay.builder()
                .member(member)
                .nickname(payDto.getNickName())
                .number(cardNumber.toString())
                .expPeriod(cardExpPeriod.toString())
                .cvc(payDto.getCvc())
                .defaultCard(payDto.isDefaultCard())
                .build();

        payRepository.save(pay);
    }

    public List<PayResponseDto> getPayList(Member member){

        List<Pay> payList = payRepository.findAllByMemberId(member.getId());

        List<PayResponseDto> payResponseDtoList = new ArrayList<>();

        for(Pay pay : payList){
            PayResponseDto payResponseDto = PayResponseDto.builder()
                    .id(pay.getId())
                    .nickName(pay.getNickname())
                    .number(Formatter.changeCardNumber(pay.getNumber()))
                    .expPeriod(pay.getExpPeriod())
                    .cvc(pay.getCvc())
                    .defaultCard(pay.isDefaultCard())
                    .build();

            payResponseDtoList.add(payResponseDto);
        }

        return payResponseDtoList;
    }

    public void deletePayCard(Long id){
       Optional<Pay> optionalPay = payRepository.findById(id);
       if(optionalPay.isEmpty()) throw new RuntimeException("올바르지 않은 접근");

       Pay pay = optionalPay.get();

       payRepository.delete(pay);
    }

    public boolean checkCardCount(Member member){
        return member.getPayList().size() < 3;
    }

}
