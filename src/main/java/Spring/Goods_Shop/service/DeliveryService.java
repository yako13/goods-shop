package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.member.DeliveryDto;
import Spring.Goods_Shop.dto.member.DeliveryResponseDto;
import Spring.Goods_Shop.entity.Delivery;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.repository.DeliveryRepository;
import Spring.Goods_Shop.repository.MemberRepository;
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
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final MemberRepository memberRepository;

    //배송지 개수 체크
    public boolean checkDeliveryCount(Member member){
       return member.getDeliveryList().size()<3;
    }

    //기본배송지 체크
    public boolean checkDefaultDelivery(DeliveryDto deliveryDto) {
        Optional<Delivery> optionalDelivery = deliveryRepository.findByMemberIdAndDefaultDelivery(deliveryDto.getMemberId(),true);

        //기본 배송지가 이미 있으면 true
        return optionalDelivery.isPresent();
    }

    //배송지 저장
    public void saveDelivery(DeliveryDto deliveryDto, HttpServletRequest request){
        //등록하려고 하는 member PK와 로그인한 사용자PK 같은지 확인
        HttpSession session = request.getSession(false);

        if(!deliveryDto.getMemberId().equals(session.getAttribute("memberId"))) throw new RuntimeException("올바르지 않은 접근");

        Optional<Member> optionalMember = memberRepository.findById(deliveryDto.getMemberId());

        if (optionalMember.isEmpty()) throw new RuntimeException("해당 회원이 존재하지 않음");

        Member member = optionalMember.get();

        Delivery delivery = Delivery.builder()
                .member(member)
                .name(deliveryDto.getDeliveryName())
                .postalCode(deliveryDto.getPostCode())
                .address(deliveryDto.getAddress())
                .addressDetail(deliveryDto.getDetailAddress())
                .recipientName(deliveryDto.getRecipientName())
                .recipientPhoneNumber(deliveryDto.getRecipientPhoneNumber())
                .memo(deliveryDto.getMemo())
                .defaultDelivery(deliveryDto.isDefaultDelivery())
                .build();

        deliveryRepository.save(delivery);
    }

    public List<DeliveryResponseDto> getDeliveryList(Member member){
        List<Delivery> deliveryList = deliveryRepository.findAllByMemberId(member.getId());

        List<DeliveryResponseDto> deliveryResponseDtos = new ArrayList<>();

        for(Delivery delivery : deliveryList){
            DeliveryResponseDto deliveryResponseDto = DeliveryResponseDto.builder()
                    .id(delivery.getId())
                    .deliveryName(delivery.getName())
                    .recipientName(delivery.getRecipientName())
                    .recipientPhoneNumber(Formatter.changePhoneNumber(delivery.getRecipientPhoneNumber()))
                    .postCode("("+delivery.getPostalCode()+")")
                    .address(delivery.getAddress())
                    .detailAddress(delivery.getAddressDetail())
                    .memo(delivery.getMemo())
                    .defaultDelivery(delivery.isDefaultDelivery())
                    .build();
            deliveryResponseDtos.add(deliveryResponseDto);
        }
        return deliveryResponseDtos;
    }

    public void deleteDelivery(Long id){
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(id);

        if(optionalDelivery.isEmpty()) throw new RuntimeException("올바르지 않은 접근");

        Delivery delivery = optionalDelivery.get();

        deliveryRepository.delete(delivery);
    }
}
