package com.wellstone.implspringoauth.account;

import com.wellstone.implspringoauth.account.DTO.AccountQueryDTO;
import com.wellstone.implspringoauth.account.DTO.AccountInsertDTO;
import com.wellstone.implspringoauth.account.DTO.AccountUpdateDTO;
import com.wellstone.implspringoauth.exception.BadValidationException;
import com.wellstone.implspringoauth.exception.DuplicatedException;
import com.wellstone.implspringoauth.exception.NotFoundException;
import com.wellstone.implspringoauth.oauthclient.OAuthClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OAuthClientRepository oAuthClientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        final Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException(accountId));
        return new AccountAdapter(account);
    }

    public Account insertAccount(AccountInsertDTO registerDto) {
        checkDuplicatedAccountId(registerDto.getAccountId());
        checkDuplicatedEmail(registerDto.getEmail());

        Account account = modelMapper.map(registerDto, Account.class);

        account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        account.setRoles(Set.of(AccountRole.USER));

        return accountRepository.save(account);
    }

    public Page<Account> getAccountList(Pageable pageable, AccountQueryDTO queryDTO) {
        Specifications<Account> spec = generateDynamicSpec(queryDTO);
        return accountRepository.findAll(spec, pageable);
    }

    public Account getAccount(Long idx) {
        return accountRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("Not Found account by idx = " + idx));
    }

    public Account getAccountByQuery(AccountQueryDTO queryDTO) {
        Specifications<Account> spec = generateDynamicSpec(queryDTO);
        return accountRepository.findOne(spec)
                .orElseThrow(() -> new NotFoundException("Not Found account by query = " + queryDTO.toString()));
    }

    public Account updateAccount(Long idx, AccountUpdateDTO updateDTO) {
        Account account = accountRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("Not Found account by idx = " + idx));

        if (!StringUtils.isEmpty(updateDTO.getName())) {
            account.setName(updateDTO.getName());
        }
        if (!StringUtils.isEmpty(updateDTO.getEmail())) {
            account.setEmail(updateDTO.getEmail());
        }
        if (!StringUtils.isEmpty(updateDTO.getCompany())) {
            account.setCompany(updateDTO.getCompany());
        }
        if(!StringUtils.isEmpty(updateDTO.getUpdatePassword())){
            boolean matches = passwordEncoder.matches(updateDTO.getCurrentPassword(), account.getPassword());
            if(matches){
                account.setPassword(passwordEncoder.encode(updateDTO.getUpdatePassword()));
            } else {
                throw new BadValidationException("현재 Password가 일치하지 않습니다.");
            }
        }

        return accountRepository.save(account);
    }

    public Account deleteAccount(Long idx) {
        Account account = accountRepository.findById(idx)
                .orElseThrow(() -> new NotFoundException("Not Found account by idx = " + idx));
        accountRepository.delete(account);
        return account;
    }

    private Specifications<Account> generateDynamicSpec(AccountQueryDTO queryDTO) {
        Specifications<Account> spec = Specifications.where(null);

        if (!StringUtils.isEmpty(queryDTO.getAccountId())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("accountId"), queryDTO.getAccountId()));
        }

        if (!StringUtils.isEmpty(queryDTO.getName())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("name"), queryDTO.getName()));
        }

        if (!StringUtils.isEmpty(queryDTO.getEmail())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("email"), queryDTO.getEmail()));
        }

        if (!StringUtils.isEmpty(queryDTO.getCompany())) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("company"), queryDTO.getCompany()));
        }
        return spec;
    }

    private void checkDuplicatedEmail(String accountEmail) {
        Optional<Account> byAccount;

        byAccount = accountRepository.findByEmail(accountEmail);
        if (byAccount.isPresent()) {
            throw new DuplicatedException("duplicated email = " + accountEmail);
        }
    }

    private void checkDuplicatedAccountId(String accountId) {
        Optional<Account> byAccount = accountRepository.findByAccountId(accountId);
        if (byAccount.isPresent()) {
            throw new DuplicatedException("duplicated account id = " + accountId);
        }
    }


}
