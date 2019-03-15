package com.wellstone.implspringoauth.config;

import com.wellstone.implspringoauth.account.Account;
import com.wellstone.implspringoauth.account.AccountRepository;
import com.wellstone.implspringoauth.account.AccountRole;
import com.wellstone.implspringoauth.account.AccountService;
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

@Configuration
public class ApplicationConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
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
            AccountService accountService;

            @Autowired
            AccountRepository accountRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                List<Account> accountRepositoryAll = accountRepository.findAll();
                if(accountRepositoryAll.isEmpty()){
                    Account admin = Account.builder()
                            .accountId("admin")
                            .password("admin")
                            .email("admin@test.io")
                            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                            .build();
                    accountRepository.save(admin);
                }
            }
        };
    }
}
