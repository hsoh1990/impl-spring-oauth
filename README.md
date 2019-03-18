# 인증 서버
OAuth2.0를 이용한 인증서버 저장소 

### key gen
```
keytool -genkey -v -keystore impl_oauth.jks -alias impl_oauth_private -keyalg RSA -sigalg MD5withRSA -keysize 1024 -validity 365
keytool -export -alias impl_oauth_private -keystore impl_oauth.jks -rfc -file impl_oauth.cer
openssl x509 -in impl_oauth.cer -pubkey -noout > impl_oauth.pub
```

### Resource server config

pom.xml
```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.security.oauth.boot</groupId>
            <artifactId>spring-security-oauth2-autoconfigure</artifactId>
            <version>2.1.0.RELEASE</version>
        </dependency>
    </dependencies>
```

application.properties
```text
security.oauth2.client.client-id=client_id
security.oauth2.client.client-secret=client_secret
security.oauth2.resource.jwt.key-uri=http://localhost:8081/oauth/token_key
security.oauth2.resource.jwt.key-value=-----BEGIN PUBLIC KEY-----\
  MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCTDS4uMpGXhxDkkueDgMy/Jr9a\
  Gyy3lgklhi271ZKAi+PmLEKTEyAzK07MxmuXC9bAajOMYXAzf8KDuU0gx/pf1tTz\
  EJx5V2Nkqe1TZE4rHrBiyN2a5LAf0GD6owwkk0nLUA0iehiNgDDT70CtqPov9tME\
  tj0hGydV1JTltEtqMQIDAQAB\
  -----END PUBLIC KEY-----
```

ResourceServerConfig.java
```java
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("oauth");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
//        jwtAccessTokenConverter.setSigningKey("wellstone");
        jwtAccessTokenConverter.setVerifierKey(publicKey);
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

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.anonymous()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
```