package com.wellstone.implspringoauth.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        final Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException(accountId));
        return new AccountAdapter(account);
    }

    public Account saveAccount(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        return this.accountRepository.save(account);
    }

    public Account getAccount(String accountId) {
        return accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException(accountId));
    }
}
