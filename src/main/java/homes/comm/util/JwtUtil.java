package homes.comm.util;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import homes.comm.vo.AccessTokenVo;
import homes.security.dto.HomesUserInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Logger Log = LogManager.getLogger(JwtUtil.class); 
	
    private final Key key;
    private final long accessTokenExpTime;
    
    private final String EXTERN_BASE_PATH   = "C:\\workspace\\project\\homes-java\\extern\\resource" ; 
    private final String PROPERTY_FILE_NAME = "jwt.properties" ; 
    
    public JwtUtil() {
    	PropertyUtil.getProperty(EXTERN_BASE_PATH, PROPERTY_FILE_NAME) ; 
    	String secretKey = PropertyUtil.getStringVal("secret") ;        /* Secret Key */
    	long   expTime   = PropertyUtil.getLongVal("expiration_time") ; /* 토큰 만료시간 */ 

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = expTime ; 
    }
    
    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(HomesUserInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }

    /**
     * JWT 생성
     * @param member
     * @param expireTime
     * @return JWT String
     */
    private String createToken(HomesUserInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("userno", member.getUserno());
        claims.put("email" , member.getEmail());
        claims.put("role"  , member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("userno", Long.class);
    }
    /**
     * JWT 검증
     * @param token
     * @return IsValidate
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
        	Log.error("Invalid JWT Token", e);
        	return false ;
//        	throw new HomesException(EnumError.INVALID_TOKEN.getSttusCd()) ; 
        } catch (ExpiredJwtException e) {
        	Log.error("Expired JWT Token", e);
        	return false ;
//        	throw new HomesException(EnumError.TOKEN_EXPIRED.getSttusCd()) ;
        } catch (UnsupportedJwtException e) {
        	Log.error("Unsupported JWT Token", e);
        	return false ;
//        	throw new HomesException(EnumError.UNSUPPORTED_TOKEN.getSttusCd()) ;
        } catch (IllegalArgumentException e) {
        	Log.error("JWT claims string is empty.", e);
        	return false ;
//        	throw new HomesException(EnumError.UNSUPPORTED_TOKEN.getSttusCd()) ;
        }
    }
    

    /**
     * JWT Claims 추출
     * @param accessToken
     * @return JWT Claims
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
    
    public AccessTokenVo getTokenInfo(String accessToken) {
    	Date issuedAt   = parseClaims(accessToken).getIssuedAt() ; 
    	Date expiration = parseClaims(accessToken).getExpiration() ; 
    	
    	Log.debug("*** issuedAt  : {}", issuedAt);
    	Log.debug("*** expiration: {}", expiration);
    	
    	Claims claim  = parseClaims( accessToken ) ; 
    	long   userno = claim.get("userno", Long.class) ; 
    	String email  = claim.get("email" , String.class) ;
    	String role   = claim.get("role"  , String.class) ; 
    	
    	AccessTokenVo tokenVo = new AccessTokenVo() ; 

    	tokenVo.setUserno(userno);
    	tokenVo.setEmail(email);
    	tokenVo.setRole(role);
    	tokenVo.setIssuedAt(issuedAt);
    	tokenVo.setExpiration(expiration);
    	tokenVo.setAccessToken(accessToken);
    	
    	return tokenVo ; 
    }
    
}
