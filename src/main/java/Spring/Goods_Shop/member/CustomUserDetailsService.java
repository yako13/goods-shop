package Spring.Goods_Shop.member;

import Spring.Goods_Shop.common.MemberRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 폼 로그인 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    private final HttpServletRequest httpServletRequest;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optionalMember = memberRepository.findByUserId(username);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            //탈퇴한 회원인 경우
            if(member.getRole().equals(MemberRole.CANCELLATION)) return null;

            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("memberId",member.getId());
            return new CustomOauth2UserDetails(member);
        }

        return null;
    }
}
