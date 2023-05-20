package com.heima.test;

import com.heima.utils.common.AppJwtUtil;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 董杰
 * @version 1.0
 */
public class UtilsTest {
    @Test
    public void testCreateToken() {
        //生成token
        //1 准备数据
        Map map = new HashMap();
        map.put("id",1);
        map.put("mobile","13800138000");
        //2 使用JWT的工具类生成token
        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, "itcast") //指定加密算法
                .setClaims(map) //写入数据
                .setExpiration(new Date(now + 30000)) //失效时间
                .compact();
        System.out.println(token);
    }
    @Test
    public void testParseToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJtb2JpbGUiOiIxMzgwMDEzODAwMCIsImlkIjoxLCJleHAiOjE2NjY2ODU0MDd9.xtR0T9XCjCqObD9T0oZSKnaquBXFAcPYUdNVdoxXSp5OSAe3lK5ncwtzYBsYw4ntQll1zHq8jdMCCJjRZiCfxA";
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("itcast")
                    .parseClaimsJws(token)
                    .getBody();
            Object id = claims.get("id");
            Object mobile = claims.get("mobile");
            System.out.println(id + "--" + mobile);
        }catch (ExpiredJwtException e) {
            System.out.println("token已过期");
        }catch (SignatureException e) {
            System.out.println("token不合法");
        }

    }
    @Test
    void jwtTokenTest(){
        Long userId = 150L;
        //生成jwt token字符串
        String token = AppJwtUtil.getToken(userId);
        //获取claims(对传递的数据进行封装
        Claims claims = AppJwtUtil.getClaimsBody(token);
        // 如果expire为i,2过期
        System.out.println(AppJwtUtil.verifyToken(claims));
    }
}
