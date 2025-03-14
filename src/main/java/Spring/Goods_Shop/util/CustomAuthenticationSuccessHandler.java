package Spring.Goods_Shop.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        String role = "";
        for(GrantedAuthority authority : grantedAuthorities){
            role = authority.getAuthority();
        }

        if(role.equals("ROLE_USER")){
            response.sendRedirect("/");
        }
        else if(role.equals("ROLE_ADMIN")){
            response.sendRedirect("/master/checkout/list");
        }
        else{
            response.sendRedirect("/login");
        }
    }
}
