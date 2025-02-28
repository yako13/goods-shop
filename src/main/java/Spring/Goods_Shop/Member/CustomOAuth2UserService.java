package Spring.Goods_Shop.Member;

import Spring.Goods_Shop.common.MemberRole;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * OAuth 로그인 서비스
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        //구글로 로그인 한 경우
        if (provider.equals("google")) {
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        //네이버로 로그인 한 경우
        else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String loginId = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();
        String phoneNumber = oAuth2UserInfo.getPhoneNumber();

        Optional<Member> findMember = memberRepository.findByUserId(loginId);

        Member member;
        if (findMember.isEmpty()) {
            member = Member.builder()
                    .name(name)
                    .userId(loginId)
                    .provider(provider)
                    .providerId(providerId)
                    .profileImage("images/profileImage.png")
                    .role(MemberRole.USER)
                    .phoneNumber(phoneNumber)
                    .privacyAgreement(true)
                    .termsAgreement(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            memberRepository.save(member);
        } else {
            member = findMember.get();
        }

        httpSession.setAttribute("memberId",member.getId());

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());

    }
}
