package Spring.Goods_Shop.service;


import Spring.Goods_Shop.dto.member.*;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.enums.MemberRole;
import Spring.Goods_Shop.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;
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
    public boolean join(MemberAuthDto memberAuthDto) {

        Member member = Member.builder()
                .name(memberAuthDto.getName())
                .userId(memberAuthDto.getUserId())
                .userPassword(passwordEncoder.encode(memberAuthDto.getUserPassword()))
                .phoneNumber(memberAuthDto.getPhoneNumber())
                .role(MemberRole.USER)
                .termsAgreement(true)
                .privacyAgreement(true)
                .build();

        memberRepository.save(member);
        return true;
    }

    /**
     * 중복 아이디 체크
     */
    public String checkId(MemberAuthDto memberAuthDto) {
        String userId = memberAuthDto.getUserId();
        //중복된 아이디일 경우
        Optional<Member> optionalMember = memberRepository.findByUserId(userId);
        if (optionalMember.isPresent()) return null;

        return userId;
    }

    /**
     * 세션 확인
     */
    public Member getMemberEntity(HttpServletRequest request) {
        //세션이 없을경우
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        Long id = (Long) session.getAttribute("memberId");

        if(id==null) return null;

        Optional<Member> optionalMember = memberRepository.findById(id);

        return optionalMember.orElse(null);

    }

    /**
     * 세션을 통해 MemberResponseDto를 가져옴
     */
    public MemberResponseDto getMemberResponseDto(HttpServletRequest request) {

        Member member = getMemberEntity(request);

        return MemberResponseDto.builder()
                .memberPK(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .userId(member.getUserId())
                .provider(member.getProvider())
                .build();
    }

    public MemberResponseDto checkOauthLogin(HttpServletRequest request) {
        Member member = getMemberEntity(request);

        //Oauth 로그인 사용자가 아닌경우
        if (member.getProvider() == null) return null;

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
    public MemberResponseDto initMyPage(MemberDto memberDto, HttpServletRequest request) {
        Member member = getMemberEntity(request);

        // 로그인 체크를 해줬기 때문에 member null 체크 X

        String userPassword = memberDto.getUserPassword();

        if (!passwordEncoder.matches(userPassword, member.getUserPassword())) return null;

        return MemberResponseDto.builder()
                .memberPK(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .userId(member.getUserId())
                .build();
    }

    /**
     * 회원 정보 수정
     */
    public void tryToEditMember(MemberEditDto memberEditDto, HttpServletRequest request) {
        Optional<Member> optionalMember = memberRepository.findByUserId(memberEditDto.getUserId());

        if (optionalMember.isEmpty()) throw new RuntimeException("비정상 접근입니다.");

        Member member = optionalMember.get();

        Member loginMember = getMemberEntity(request);
        //로그인 한 회원과 업데이트 시도한 회원이 일치하지 않을 경우
        if (!member.getUserId().equals(loginMember.getUserId())) throw new RuntimeException("비정상 접근입니다.");

        //소셜 로그인 회원의 경우 비밀번호 입력 X -> null
        if (memberEditDto.getUserPassword() != null)
            member.setUserPassword(passwordEncoder.encode(memberEditDto.getUserPassword()));

        member.setPhoneNumber(memberEditDto.getPhoneNumber());
        member.setName(memberEditDto.getName());

        memberRepository.save(member);

    }

    /**
     * 아이디 찾기
     */
    public String tryToFindId(MemberDto memberDto) {
        String name = memberDto.getName();
        String phoneNumber = memberDto.getPhoneNumber();

        Optional<Member> optionalMember = memberRepository.findByNameAndPhoneNumber(name, phoneNumber);

        if (optionalMember.isEmpty()) return null;

        Member member = optionalMember.get();

        //탈퇴한 회원 또는 관리자일 경우
        if(member.getRole().equals(MemberRole.CANCELLATION) || member.getRole().equals(MemberRole.ADMIN))return null;

        //소셜로그인 사용자의 경우
        if (member.getProvider() != null) return null;

        int length = member.getUserId().length();

        //아이디의 뒷 3자리는 *로 표시
        return  member.getUserId().substring(0, length - 3) + "***";

    }

    /**
     * 비밀번호 찾기
     */
    public String tryToFindPassword(MemberDto memberDto) {
        String userId = memberDto.getUserId();
        String phoneNumber = memberDto.getPhoneNumber();

        Optional<Member> optionalMember = memberRepository.findByUserIdAndPhoneNumber(userId, phoneNumber);

        if (optionalMember.isEmpty()) return null;

        Member member = optionalMember.get();

        //탈퇴한 회원 또는 관리자일 경우
        if(member.getRole().equals(MemberRole.CANCELLATION) || member.getRole().equals(MemberRole.ADMIN))return null;

        //소셜로그인 사용자의 경우
        if (member.getProvider() != null) return null;

        return member.getUserId();
    }

    /**
     * 비밀번호 재설정
     */
    public void tryToEditPassword(MemberPasswordDto memberPasswordDto) {
        Optional<Member> optionalMember = memberRepository.findByUserId(memberPasswordDto.getUserId());

        if (optionalMember.isEmpty()) throw new RuntimeException("비정상 접근입니다.");

        Member member = optionalMember.get();

        member.setUserPassword(passwordEncoder.encode(memberPasswordDto.getUserPassword()));

        memberRepository.save(member);

    }

    /**
     * SNS계정 가입자 탈퇴
     */
    public void tryToCancellationSNSAccount(Member member) {
        //권한 : 탈퇴
        member.setRole(MemberRole.CANCELLATION);
        memberRepository.save(member);
    }

    /**
     * 폼 가입자 탈퇴
     */
    public boolean tryToCancellationAccount(MemberAuthDto memberDto, HttpServletRequest request) {
        Member member = getMemberEntity(request);

        String userPassword = memberDto.getUserPassword();

        if (!passwordEncoder.matches(userPassword, member.getUserPassword())) return false;

        member.setRole(MemberRole.CANCELLATION);

        memberRepository.save(member);

        return true;
    }

}
