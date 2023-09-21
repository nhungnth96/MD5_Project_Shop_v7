package md5.end.security.jwt;

import md5.end.exception.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import md5.end.security.principal.UserDetailService;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// nơi lọc token
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    public final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    public UserDetailsService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            try {
                String token = getTokenFromRequest(request);
                if(token!=null&& jwtProvider.validateToken(token)){
                // lấy ra đối tượng userDetail
                String username = jwtProvider.getUserNameFromToken(token);
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                logger.error("Can't set user authentication {} ->",e.getMessage());
            }
            filterChain.doFilter(request,response);

    }

    // lấy token từ phía header
    public String getTokenFromRequest(HttpServletRequest request){
        String header = request.getHeader("Authorization"); // postMan - header - type Bearer Token
        if(header!=null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
