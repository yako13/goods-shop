package Spring.Goods_Shop.member;


import Spring.Goods_Shop.common.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    /**
     * 회원가입
     */
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

    /**
     *세션 확인 
     */
    public Member getMemberEntity(HttpServletRequest request){
        //세션이 없을경우
        HttpSession session = request.getSession(false);
        if(session==null) return null;

        Long id = (Long) session.getAttribute("memberId");

        Optional<Member> optionalMember = memberRepository.findById(id);

        return optionalMember.orElse(null);

    }

    /**
     * 세션을 통해 MemberResponseDto를 가져옴
     */
    public MemberResponseDto getMemberResponseDto(HttpServletRequest request){

        Member member = getMemberEntity(request);
        
        return MemberResponseDto.builder()
                .memberPK(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .userId(member.getUserId())
                .provider(member.getProvider())
                .build();
    }

    public MemberResponseDto checkOauthLogin(HttpServletRequest request){
        Member member = getMemberEntity(request);

        //Oauth 로그인 사용자가 아닌경우
        if(member.getProvider()==null) return null;

        return MemberResponseDto.builder()
                .memberPK(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .userId(member.getUserId())
                .provider(member.getProvider())
                .build();
    }


    /**
     * 마이페이지로 들어가기 위하여 비밀번호 일치 여부 확인
     */
    public MemberResponseDto initMyPage(MemberDto memberDto,HttpServletRequest request){
       Member member = getMemberEntity(request);

       // 로그인 체크를 해줬기 때문에 member null 체크 X

        String userPassword = memberDto.getUserPassword();
        
        if(!passwordEncoder.matches(userPassword,member.getUserPassword())) return null;
        
        return MemberResponseDto.builder()
                .memberPK(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .userId(member.getUserId())
                .build();
    }

    public void tryMemberEdit(MemberJoinDto memberJoinDto){
        Optional<Member> optionalMember = memberRepository.findById(memberJoinDto.getId());

        if(optionalMember.isEmpty()) throw new RuntimeException("비정상 접근입니다.");

        Member member = optionalMember.get();

        member.setUserPassword(passwordEncoder.encode(memberJoinDto.getUserPassword()));
        member.setPhoneNumber(memberJoinDto.getPhoneNumber());
        member.setName(memberJoinDto.getName());

        memberRepository.save(member);

    }
}
