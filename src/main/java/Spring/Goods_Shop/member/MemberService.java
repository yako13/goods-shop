package Spring.Goods_Shop.member;


import Spring.Goods_Shop.common.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean join(MemberJoinDto memberJoinDto){
        String userId = memberJoinDto.getUserId();

        //중복된 아이디일 경우
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if(optionalMember.isPresent()) return false;

        Member member = Member.builder()
                .name(memberJoinDto.getName())
                .userId(userId)
                .userPassword(passwordEncoder.encode(memberJoinDto.getUserPassword()))
                .phoneNumber(memberJoinDto.getPhoneNumber())
                .role(MemberRole.USER)
                .termsAgreement(true)
                .privacyAgreement(true)
                .build();

        memberRepository.save(member);
        return true;
    }
}
