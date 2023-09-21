package md5.end.security.jwt;

// nơi sinh ra token và giải mã token

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import md5.end.security.principal.UserPrincipal;

import java.util.Date;

@Component
public class JwtProvider {
    public static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
    private static final String SECRET_KEY = "nhung274";
    private static final int EXPIRATION_TOKEN = 86400000;
    public String generateToken(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername()) // set chủ đề
                .setIssuedAt(new Date()) // thơ gian bắt đầu
                .setExpiration(new Date(new Date().getTime()+EXPIRATION_TOKEN)) // thời gian kết thúc
                .signWith(SignatureAlgorithm.HS512,SECRET_KEY) // chữ ký vs thuật toán mã hóa, chuỗi bí mật
                .compact();
    }


    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            logger.error("Failed -> Expired Token message {}", e.getMessage());
        }catch (UnsupportedJwtException e){
            logger.error("Failed -> Unsupported JWT Token {}", e.getMessage());
        }catch (MalformedJwtException e){
            logger.error("Failed -> Invalid Format Token message {}", e.getMessage());
        }catch (SignatureException e){
            logger.error("Failed -> Invalid Signature Token message {}", e.getMessage());
        }catch (IllegalArgumentException e){
            logger.error("Failed -> JWT claims String is empty {}", e.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken (String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

}
