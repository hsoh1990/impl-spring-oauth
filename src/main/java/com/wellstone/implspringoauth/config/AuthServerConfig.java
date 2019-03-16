package com.wellstone.implspringoauth.config;

import com.wellstone.implspringoauth.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    DataSource dataSource;


    /**
     * oauth_client_details table을 사용하기 위해 서비스 정의
     * clients.withClientDetails(JdbcClientDetailsService())로 사용
     */
    @Bean
    @Primary
    public JdbcClientDetailsService JdbcClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 인증한 사용자의 토큰을 저장하는 방식 설정
     * JwtTokenStore로 사용하면 JWT를 사용
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * jwt의 서명을 설정
     * setSigningKey은 직접 String값 입력가능
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("wellstone");
        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenService() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    /**
     * Spring Security OAuth 토큰확인 API - 기본적으로 "denyAll()"
     * /oauth/check_token -> 해당 토큰이 유효한지 확인(토큰 유효성 확인을 인증서버가 처리)
     * /oauth/token_key -> 해당 토큰의 서명을 요청(토큰 유효성 확인 Resource 서버가 처리)
     * tokenKeyAccess("permitAll()"), heckTokenAccess("isAuthenticated()")로 권한 변경
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .passwordEncoder(passwordEncoder)
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * client detail service를 정의하는 구성 클래스
     * Client detail 초기화(withClient), 저장소 참조 정의(withClientDetails)
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .withClientDetails(JdbcClientDetailsService());
    }

    /**
     * 인증 서버 endpoints 정의
     * SecurityConfig 의 authenticationManager 주입
     * SecurityConfig 의 TokenStore 주입 (InMemoryTokenStore)
     * AccountService 주입
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(accountService)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter());

    }
}
