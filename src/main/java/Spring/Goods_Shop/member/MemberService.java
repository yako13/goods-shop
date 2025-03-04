package Spring.Goods_Shop.member;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

//    public MemberResponseDto join(MemberJoinDto memberJoinDto){
//        String userId = memberJoinDto.getUserId();
//
//        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
//        if(optionalMember.isEmpty()) return null;
//
//
//    }
}
