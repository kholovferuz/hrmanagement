package uz.pdp.hrmanagement.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.hrmanagement.Service.AuthService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    JWTProvider jwtProvider;
    @Autowired
    AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //REQUEST dan tokenni olish
        String token = httpServletRequest.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);



                // tokenning username ni olish
                String usernameFromToken = jwtProvider.getUsernameFromToken(token);

                if (usernameFromToken != null) {
                    // username orqali userdetailsni olish
                    UserDetails userDetails = authService.loadUserByUsername(usernameFromToken);

                    // userdetails orqali authentication yaratish
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    // sistemaga kim kirganligini o'rnatish
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
