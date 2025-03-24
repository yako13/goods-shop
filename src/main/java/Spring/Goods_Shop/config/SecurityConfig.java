package Spring.Goods_Shop.config;

import Spring.Goods_Shop.enums.MemberRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Bean
    protected SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {

        //csrf 사용 안함
        http.csrf(AbstractHttpConfigurer::disable);

        //H2콘솔 연결 위함
        //H2 콘솔은 iframe 을 통해 화면 구성 -> 브라우저는 요청 응답에 있는 X-Frame-Options 헤더의 내용에 따라 iframe 에서의 요청을 허용할지 안할지 판단
        //Spring Security의 X-Frame-Options 기본 설정 : Deny
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        //접근 권한 설정
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/init/member", "/member/**","/checkout/**","/account/**","/cart/**").hasRole(MemberRole.USER.name())
                        .requestMatchers("/master/**").hasRole(MemberRole.ADMIN.name())
//                .requestMatchers( "/my-page","/update", "/delete", "/logout").hasAnyRole(MemberRole.ADMIN.name())
                        .anyRequest().permitAll()
        );

        //폼 로그인
        //필터가 login 처리하므로 컨트롤러 따로 필요 X
        http.formLogin((auth) -> auth
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureUrl("/login?error")
                .usernameParameter("userId")
                .passwordParameter("userPassword")
//                .successHandler(authenticationSuccessHandler)
                // 로그인 전 요청했던 화면이 있으면 해당하는 화면으로 이동 없으면 홈
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                        String redirectUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";
                        response.sendRedirect(redirectUrl);
                    }
                })
                .permitAll()
        );

        //oauth 로그인
        http.oauth2Login((auth) -> auth
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization"))
//                .successHandler(authenticationSuccessHandler)
                        .successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                                String redirectUrl = (savedRequest != null) ? savedRequest.getRedirectUrl() : "/";
                                response.sendRedirect(redirectUrl);
                            }
                        })
                .permitAll()
        );


        //로그아웃
        //필터가 세션 삭제해주므로 컨트롤러 따로 필요 X
        http.logout((auth) -> auth
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
        );


        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
