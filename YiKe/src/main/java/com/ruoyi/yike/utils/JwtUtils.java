package com.ruoyi.yike.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Component
public class JwtUtils {

    /**
     * JWT签名密钥
     */
    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String jwtSecretKey;

    /**
     * JWT过期时间（小时）
     */
    @Value("${jwt.expiration:24}")
    private int tokenExpirationHours;

    /**
     * JWT刷新时间（小时）
     */
    @Value("${jwt.refresh:168}")
    private int refreshTokenExpirationHours;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成JWT token
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @return JWT token
     */
    public String generateToken(Long userId, String userName) {
        Date currentTime = new Date();
        Date tokenExpirationTime = new Date(currentTime.getTime() + tokenExpirationHours * 3600000L);

        return Jwts.builder()
                .setSubject(userName)
                .claim("userId", userId)
                .claim("userName", userName)
                .setIssuedAt(currentTime)
                .setExpiration(tokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    /**
     * 生成刷新token
     *
     * @param userId   用户ID
     * @param userName 用户名
     * @return 刷新token
     */
    public String generateRefreshToken(Long userId, String userName) {
        Date currentTime = new Date();
        Date refreshTokenExpirationTime = new Date(currentTime.getTime() + refreshTokenExpirationHours * 3600000L);

        return Jwts.builder()
                .setSubject(userName)
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("tokenType", "refresh")
                .setIssuedAt(currentTime)
                .setExpiration(refreshTokenExpirationTime)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    /**
     * 从token中获取用户ID
     *
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        if (tokenClaims == null) {
            return null;
        }
        Object userIdClaim = tokenClaims.get("userId");
        return userIdClaim != null ? Long.valueOf(userIdClaim.toString()) : null;
    }

    /**
     * 从token中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUserNameFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        return tokenClaims != null ? tokenClaims.getSubject() : null;
    }

    /**
     * 从token中获取过期时间
     *
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims tokenClaims = parseTokenClaims(token);
        return tokenClaims != null ? tokenClaims.getExpiration() : null;
    }

    /**
     * 解析JWT token获取Claims
     *
     * @param token JWT token
     * @return Claims
     */
    private Claims parseTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * 验证token是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 检查token是否在黑名单中
            if (checkTokenInBlacklist(token)) {
                return false;
            }

            Claims tokenClaims = parseTokenClaims(token);
            if (tokenClaims == null) {
                return false;
            }

            // 检查是否过期
            Date tokenExpirationDate = tokenClaims.getExpiration();
            return tokenExpirationDate.after(new Date());
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 判断token是否即将过期（1小时内过期）
     *
     * @param token JWT token
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        Date tokenExpirationDate = getExpirationDateFromToken(token);
        if (tokenExpirationDate == null) {
            return true;
        }

        long millisecondsUntilExpiration = tokenExpirationDate.getTime() - System.currentTimeMillis();
        return millisecondsUntilExpiration < 3600000; // 1小时 = 3600000毫秒
    }

    /**
     * 将token加入黑名单
     *
     * @param token JWT token
     */
    public void invalidateToken(String token) {
        try {
            Date tokenExpirationDate = getExpirationDateFromToken(token);
            if (tokenExpirationDate != null) {
                long timeToLiveMillis = tokenExpirationDate.getTime() - System.currentTimeMillis();
                if (timeToLiveMillis > 0) {
                    String blacklistKey = "blacklist:" + token;
                    redisTemplate.opsForValue().set(blacklistKey, "invalid", timeToLiveMillis, TimeUnit.MILLISECONDS);
                }
            }
        } catch (Exception ex) {
            // 记录日志
            System.err.println("Failed to invalidate token: " + ex.getMessage());
        }
    }

    /**
     * 检查token是否在黑名单中
     *
     * @param token JWT token
     * @return 是否在黑名单中
     */
    private boolean checkTokenInBlacklist(String token) {
        try {
            String blacklistKey = "blacklist:" + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey));
        } catch (Exception ex) {
            // 如果Redis连接失败，为了安全起见返回false（允许通过）
            return false;
        }
    }

    /**
     * 获取token过期时间（秒）
     *
     * @return 过期时间秒数
     */
    public long getExpiration() {
        return tokenExpirationHours * 3600L; // 转换为秒
    }

    /**
     * 获取刷新token过期时间（秒）
     *
     * @return 刷新token过期时间秒数
     */
    public long getRefreshExpiration() {
        return refreshTokenExpirationHours * 3600L; // 转换为秒
    }

    /**
     * 从token中获取剩余有效时间（秒）
     *
     * @param token JWT token
     * @return 剩余有效时间秒数
     */
    public long getRemainingTime(String token) {
        Date tokenExpirationDate = getExpirationDateFromToken(token);
        if (tokenExpirationDate == null) {
            return 0;
        }
        long remainingTimeMillis = tokenExpirationDate.getTime() - System.currentTimeMillis();
        return Math.max(0, remainingTimeMillis / 1000); // 转换为秒，不能为负数
    }
}