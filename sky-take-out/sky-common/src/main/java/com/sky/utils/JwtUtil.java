package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JSON Web Token (JWT) 是一种用于在网络应用间安全地传递声明的开放标准（RFC 7519）。JWT被广泛用于身份验证和信息交换，特别是在单点登录（SSO）场景中。
 * JWT 使用场景
 * 身份验证：用户登录成功后，服务器生成一个JWT，在将其发送给客户端。客户端以后的每次请求都携带JWT，服务器通过验证JWT的签名信息来验证用户身份。
 * 用户授权：JWT也经常用于传递用户的授权信息，以决定用户能否访问某些资源。
 * 优点
 * 可靠的跨域传输：JWT可以通过 URL、POST参数或者HTTP Header的方式传递
 * 安全性：JWT通常使用HMAC算法或者RSA/ECDSA等公钥/私钥对来签名，保证了信息的安全传输。
 */

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥，用于对 JWT 进行签名和验证。在使用时，应当使用一个足够安全的随机字符串作为秘钥。
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息，这是一个Map对象，其中包含了要在JWT中设置的自定义信息。
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分，，SignatureAlgorithm.HS256是JSON Web Token (JWT)库的一个枚举值，用于表示使用HMAC-SHA256的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥，在使用字符串作为秘钥时，需要将其转换为字节数组形式才能在使用加密或签名算法时进行处理。
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        // 生成JWT：所有属性都设置完成后，调用 builder.compact() 将JWT构建器中的信息压缩成一个JWT字符串表示形式。
        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
