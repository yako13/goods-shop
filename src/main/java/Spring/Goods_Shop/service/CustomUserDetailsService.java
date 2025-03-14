package Spring.Goods_Shop.service;

import Spring.Goods_Shop.dto.member.CustomOauth2UserDetails;
import Spring.Goods_Shop.entity.Member;
import Spring.Goods_Shop.repository.MemberRepository;
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
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("memberId",member.getId());
            return new CustomOauth2UserDetails(member);
        }

        return null;
    }
}
