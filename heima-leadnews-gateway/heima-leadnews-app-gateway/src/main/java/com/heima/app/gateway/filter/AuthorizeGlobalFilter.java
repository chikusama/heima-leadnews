package com.heima.app.gateway.filter;

import com.heima.app.gateway.utils.AppJwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author 董杰
 * @version 1.0
 */
@Component
public class AuthorizeGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,GatewayFilterChain chain) {
        //1.判断是否是登录请求
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (path.contains("/login")) {
            //登录请求放行
          return   chain.filter(exchange);
        }


        //2.判断token是否存在
        String token = request.getHeaders().getFirst("token");
        if (StringUtils.isEmpty(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        //3.判断token是否有效
        Claims claims = AppJwtUtil.getClaimsBody(token);
        int flag = AppJwtUtil.verifyToken(claims);
        switch (flag) {
            case 1:
            case 2:
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
        }
       return chain.filter(exchange);
    }
}
