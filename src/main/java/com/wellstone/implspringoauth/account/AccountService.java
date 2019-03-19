package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.AccountQueryDTO;
import com.wellstone.implspringoauth.account.DTO.AccountRegisterDTO;
import com.wellstone.implspringoauth.exception.DuplicatedException;
import com.wellstone.implspringoauth.exception.NotFoundException;
import com.wellstone.implspringoauth.util.Utils;
import org.hibernate.query.criteria.internal.predicate.BooleanExpressionPredicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Set;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private ModelMapper modelMapper;

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

    public Account saveAccount(AccountRegisterDTO registerDto) {
        checkDuplicatedAccountId(registerDto.getAccountId());
        checkDuplicatedEmail(registerDto.getEmail());

        Account account = modelMapper.map(registerDto, Account.class);

        account.setPassword(this.passwordEncoder.encode(registerDto.getPassword()));
        account.setRoles(Set.of(AccountRole.USER));

        return this.accountRepository.save(account);
    }

    public Account getAccount(Long idx) {
        return accountRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("Not Found account by idx = " + idx));
    }

    public Account getAccountByQuery(AccountQueryDTO searchDTO) {
        Specifications<Account> spec = Specifications.where(null);

        if(!StringUtils.isEmpty(searchDTO.getAccountId())){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accountId"), searchDTO.getAccountId()));
        }

        if(!StringUtils.isEmpty(searchDTO.getEmail())){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), searchDTO.getEmail()));
        }

        return accountRepository.findOne(spec)
                .orElseThrow(() -> new NotFoundException("Not Found account by query = " + searchDTO.toString()));
    }

    private void checkDuplicatedEmail(String accountEmail) {
        Optional<Account> byAccount;

        byAccount = accountRepository.findByEmail(accountEmail);
        if(byAccount.isPresent()){
            throw new DuplicatedException("duplicated email = " + accountEmail);
        }
    }

    private void checkDuplicatedAccountId(String accountId) {
        Optional<Account> byAccount = accountRepository.findByAccountId(accountId);
        if(byAccount.isPresent()){
            throw new DuplicatedException("duplicated account id = " + accountId);
        }
    }
}
