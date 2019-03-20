package com.wellstone.implspringoauth.config;

import com.wellstone.implspringoauth.account.Account;
import com.wellstone.implspringoauth.account.AccountRepository;
import com.wellstone.implspringoauth.account.AccountRole;
import com.wellstone.implspringoauth.oauthclient.OAuthClient;
import com.wellstone.implspringoauth.oauthclient.OAuthClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;


@Slf4j
@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
//        스프링5 업데이트하면서 생긴 encoder factories(client_secret -> {noop}client_secret 로 수정)
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountRepository accountRepository;

            @Autowired
            OAuthClientRepository oAuthClientRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<Account> accountRepositoryAll = accountRepository.findAll();
                if(accountRepositoryAll.isEmpty()){
                    Account admin = Account.builder()
                            .accountId("admin").password(passwordEncoder().encode("admin")).name("admin").email("admin@test.io")
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                            .build();
                    Account account = accountRepository.save(admin);
                    log.info(account.toString());
                }

                List<OAuthClient> oAuthClientRepositoryAll = oAuthClientRepository.findAll();
                if (oAuthClientRepositoryAll.isEmpty()){
                    OAuthClient oAuthClient = OAuthClient.builder()
                            .clientId("client_id")
                            .resourceIds("oauth")
                            .clientSecret("{noop}client_secret")
                            .scope("read,write")
                            .authGrantTypes("password,refresh_token")
                            .authorities("ROLE_YOUR_CLIENT")
                            .accessTokenValidity(36000)
                            .refreshTokenValidity(2592000)
                            .account(accountRepository.findByAccountId("admin").get())
                            .build();
                    OAuthClient saveOAuthClient = oAuthClientRepository.save(oAuthClient);
                    log.info(saveOAuthClient.toString());
                }
            }
        };
    }
}
