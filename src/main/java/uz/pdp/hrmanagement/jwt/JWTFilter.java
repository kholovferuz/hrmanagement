package uz.pdp.hrmanagement.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.pdp.hrmanagement.service.AuthServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    final JWTProvider jwtProvider;
    final AuthServiceImpl authServiceImpl;


    public JWTFilter(JWTProvider jwtProvider, @Lazy AuthServiceImpl authServiceImpl) {
        this.jwtProvider = jwtProvider;
        this.authServiceImpl = authServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //getting token from request
        String token = httpServletRequest.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer")) {
            token = token.substring(7);

                // getting the username of the token
                String usernameFromToken = jwtProvider.getUsernameFromToken(token);

                if (usernameFromToken != null) {
                    //getting userdetail by username
                    UserDetails userDetails = authServiceImpl.loadUserByUsername(usernameFromToken);

                    // creating authentication by userdetails
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

                    // determining who entered the system
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
