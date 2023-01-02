package com.project.doctorhub.auth.config.chat;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.doctorhub.auth.dto.UserCredentials;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.base.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static com.project.doctorhub.auth.config.security.filter.JwtAuthorizationFilter.BEARER_KEYWORD;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig
        implements WebSocketMessageBrokerConfigurer {

    private final JWTUtil jwtUtil;

    @Autowired
    public WebsocketConfig(
            JWTUtil jwtUtil
    ) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/consultation");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_KEYWORD)) {
                        String token = authorizationHeader.substring(BEARER_KEYWORD.length());
                        try {

                            UserCredentials userCredentials = jwtUtil.extractUserCredentials(token);
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    userCredentials.getUuid(),
                                    null,
                                    userCredentials.getAuthorities()
                            );
                            accessor.setUser(usernamePasswordAuthenticationToken);
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                        } catch (SignatureVerificationException ex) {
                            throw new HttpException("فرمت توکن ارسالی صحیح نمی باشد!", HttpStatus.BAD_REQUEST);
                        } catch (TokenExpiredException ex) {
                            throw new HttpException("توکن ارسالی منقضی شده است!", HttpStatus.UNAUTHORIZED);
                        }
                    }
                }
                return message;
            }
        });
    }
}
