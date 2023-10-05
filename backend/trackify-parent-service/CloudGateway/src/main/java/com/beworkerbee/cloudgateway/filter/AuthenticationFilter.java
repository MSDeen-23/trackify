package com.beworkerbee.cloudgateway.filter;

import com.beworkerbee.cloudgateway.constants.GenericConstants;
import com.beworkerbee.cloudgateway.service.JwtService;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter {
    private final JwtService jwtService;


    Gson gson = new Gson();
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        final String requestPath = exchange.getRequest().getPath().toString();
        String userEmail;
        if(GenericConstants.linksWithoutAuthentication.contains(requestPath)){
            return chain.filter(exchange);
        }
        else{
            try{
                String bearerToken = String.valueOf(exchange.getRequest().getHeaders().get("Authorization").get(0));
                if(bearerToken==null|| !bearerToken.startsWith("Bearer")){
                    return chain.filter(exchange);
                }
                String token = bearerToken.substring(7);
                userEmail = jwtService.extractUsername(token);
                if(userEmail!=null){
                    Claims userDetails = jwtService.extractAllClaims(token);
                    if(jwtService.isTokenValid(token)){
                        ServerHttpRequest request = exchange.getRequest().mutate()
                                .header("userDetails", gson.toJson(userDetails))
                                .build();
                        return chain.filter(exchange.mutate().request(request).build());
                    }else{
                        throw new Exception("Invalid token");
                    }
                }
                else{
                    throw new Exception("Username is corrupted");
                }
            }catch (Exception e){
                    ServerHttpResponse response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();

            }
        }

    }
}
