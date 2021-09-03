package com.zj.server.config.security.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * JWT工具类
 *
 * @author zhoujian on 2021/8/27 0027 16:28
 */
@Component
public class JwtTokenUtil {
    //用户名
    private static  final String CLAIM_KEY_USERNAME="sub";
    //jwt创建时间
    private static  final String CLAIM_KEY_CREATED="created";
    //jwt秘钥 通过value注解去配置文件中拿
    @Value("${jwt.secret}")
    private String secret;
    //jwt失效时间
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据用户信息生成token
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

    /**
     * 根据荷载生成jwt token
     * @param claims
     * @return
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 生成token失效时间
     * @return
     */
    private Date generateExpirationDate() {
        //当前时间+失效时长=失效的时间
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    /**
     * 从token中获取登陆用户名
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token){
        String username;
        //用户名在荷载中,先通过token获取claims,
        try {
            Claims claims = getClaimsFromToken(token);
            username=claims.getSubject();
        } catch (Exception e) {
            username=null;
        }
        return username;
    }

    /**
     * 从token中获取荷载
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims=null;
        try {
            claims=Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /*验证tokden是否有效*/
    public boolean validateToken(String token,UserDetails userDetails){
        //token是否过期
        //token荷载里的用户名跟UserDetails中的用户名是否一致
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否失效 为true：已失效， 为false: 未失效
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        //从token中获取失效时间
        Date expireDate = getExpireDateFromToken(token);
        // before:d1.before(d2) 只有d1在d2之前才返回true
        return expireDate.before(new Date());
    }

    /**
     * 从token中获取失效时间
     * @param token
     * @return
     */
    private Date getExpireDateFromToken(String token) {
        //从荷载中获取失效时间
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 判断token是否可以被刷新
     * @param token
     * @return
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     * @param token
     * @return
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

}

